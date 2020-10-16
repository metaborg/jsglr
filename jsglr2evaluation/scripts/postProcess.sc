import $ivy.`com.lihaoyi::ammonite-ops:1.8.1`, ammonite.ops._

import $file.spoofaxDeps

import $ivy.`org.metaborg:org.spoofax.jsglr2:2.6.0-SNAPSHOT`

import org.spoofax.jsglr2.incremental.EditorUpdate
import org.spoofax.jsglr2.incremental.diff.IStringDiff
import org.spoofax.jsglr2.incremental.diff.JGitHistogramDiff

import scala.collection.JavaConverters._

import $file.common, common._, Suite._

val diff: IStringDiff = new JGitHistogramDiff();

println("Processing results...")

mkdir! resultsDir

val languagesWithBatchSources = suite.languages.filter(_.sources.batch.nonEmpty)
if (languagesWithBatchSources.nonEmpty) {
    val dir = languagesWithBatchSources(0).measurementsDir

    // Copy header from measurements CSV
    write.over(parseTableMeasurementsPath, "language," + read.lines(dir / "parsetable.csv")(0))
    write.over(parsingMeasurementsPath,    "language," + read.lines(dir / "parsing.csv")(0))

    // Add header to benchmarks CSV
    write.over(batchBenchmarksPath,             "language,variant,score,error,low,high\n")
    write.over(batchBenchmarksNormalizedPath,   "language,variant,score,low,high\n")
    write.over(perFileBenchmarksPath,           "language,variant,score,error,low,high,size\n")
    write.over(perFileBenchmarksNormalizedPath, "language,variant,score,low,high,size\n")
}

suite.languages.foreach { language =>
    println(" " + language.name)

    if (language.sources.batch.nonEmpty) {

        // Measurements

        write.append(parseTableMeasurementsPath, "\n" + language.id + "," + read.lines(language.measurementsDir / "parsetable.csv")(1))
        write.append(parsingMeasurementsPath, "\n" + language.id + "," + read.lines(language.measurementsDir / "parsing.csv")(1))

        // Benchmarks (batch)

        // Normalization: chars / ms == 1000 chars / s
        val characters = BigDecimal(CSV.parse(language.measurementsDir / "parsing.csv").rows.head("characters"))
        val normalizeBatch: BigDecimal => BigDecimal = score => characters / score

        def processBenchmarkCSV(benchmarkCSV: CSV, variant: CSVRow => String, destinationPath: Path, destinationPathNormalized: Path, normalize: BigDecimal => BigDecimal, append: String = "") = {
            benchmarkCSV.rows.foreach { row =>
                val rawScore = row("Score")
                val rawError = row("Score Error (99.9%)")

                val score = BigDecimal(rawScore)
                val error = if (rawError != "NaN") BigDecimal(rawError) else BigDecimal(0)

                write.append(destinationPath, language.id + "," + variant(row) + "," + round(score) + "," + round(error) + "," + round(score - error) + "," + round(score + error) + append + "\n")
                write.append(destinationPathNormalized, language.id + "," + variant(row) + "," + round(normalize(score)) + "," + round(normalize(score + error)) + "," + round(normalize(score - error)) + append + "\n")
            }
        }

        processBenchmarkCSV(CSV.parse(language.benchmarksDir / "jsglr2.csv"), row => row("Param: variant"), batchBenchmarksPath, batchBenchmarksNormalizedPath, normalizeBatch)
        processBenchmarkCSV(CSV.parse(language.benchmarksDir / "jsglr1.csv"), _ => "jsglr1", batchBenchmarksPath, batchBenchmarksNormalizedPath, normalizeBatch)

        language.antlrBenchmarks.foreach { antlrBenchmark =>
            processBenchmarkCSV(CSV.parse(language.benchmarksDir / s"${antlrBenchmark.id}.csv"), _ => antlrBenchmark.id, batchBenchmarksPath, batchBenchmarksNormalizedPath, normalizeBatch)
        }

        // Benchmarks (per file)

        language.sourceFilesPerFileBenchmark.foreach { file =>
            val characters = (read ! file).length
            val normalizePerFile: BigDecimal => BigDecimal = score => characters / score

            processBenchmarkCSV(CSV.parse(language.benchmarksDir / "perFile" / s"${file.last.toString}.csv"), row => row("Param: variant"), perFileBenchmarksPath, perFileBenchmarksNormalizedPath, normalizePerFile, "," + characters)
        }
    }

    // Benchmarks (incremental)

    val parserTypes = Seq("Batch", "Incremental", "IncrementalNoCache")
    language.sources.incremental.foreach { source => {
        Map(false -> "parse", true -> "parse+implode").foreach { case (implode, parseImplodeStr) =>
            mkdir! incrementalResultsDir / language.id
            val resultPath = incrementalResultsDir / language.id / s"${source.id}-${parseImplodeStr}.csv"

            // CSV header
            write.over(resultPath, """"i"""")
            parserTypes.foreach { parserType =>
                write.append(resultPath, s""","$parserType","$parserType Error (99.9%)"""")
            }
            write.append(resultPath, ""","Size (bytes)","Removed","Added","Changes"""" + "\n")

            val sourceDir = language.sourcesDir / "incremental" / source.id
            for (i <- 0 until (ls! sourceDir).length) {
                val csv = try {
                    CSV.parse(language.benchmarksDir / "jsglr2incremental" / source.id / s"$i.csv")
                } catch {
                    case _ => CSV(Seq.empty, Seq.empty)
                }
                val rows = csv.rows.filter(_("Param: implode") == implode.toString)

                write.append(resultPath, i.toString)

                parserTypes.foreach { parserType => {
                    write.append(resultPath, rows.find(_("Param: parserType") == parserType) match {
                        case Some(row) => "," + row("Score") + "," + row("Score Error (99.9%)").replace("NaN", "")
                        case None => ",,"
                    })
                }}

                val totalSize = ((ls! sourceDir / s"$i") | stat | (_.size)).sum
                write.append(resultPath, "," + totalSize)

                val diffs: Seq[java.util.List[EditorUpdate]] = (ls! sourceDir / s"$i").map(file => diff.diff(
                    try {
                        read! file / up / up / s"${i-1}" / file.last
                    } catch {
                        case _ => "" // This case is reached when i == 0 or when a file is new in this iteration
                    },
                    read! file)
                )
                val deleted = diffs.map(diff => diff.asScala.map(_.deletedLength).sum).sum
                val inserted = diffs.map(diff => diff.asScala.map(_.insertedLength).sum).sum
                val numChanges = diffs.map(diff => diff.size).sum
                write.append(resultPath, "," + deleted + "," + inserted + "," + numChanges + "\n")
            }
        }
    }}
}
