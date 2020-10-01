import $ivy.`com.lihaoyi::ammonite-ops:1.8.1`, ammonite.ops._

import $file.common, common._, Args._

def postProcess(implicit args: Args) = {
    println("Processing results...")
    
    mkdir! resultsDir

    // Copy header from measurements CSV
    write.over(parseTableMeasurementsPath, "language," + read.lines(args.languages(0).measurementsDir / "parsetable.csv")(0))
    write.over(parsingMeasurementsPath,    "language," + read.lines(args.languages(0).measurementsDir / "parsing.csv")(0))

    // Add header to benchmarks CSV
    write.over(batchBenchmarksPath,             "language,variant,score,error,low,high\n")
    write.over(batchBenchmarksNormalizedPath,   "language,variant,score,low,high\n")
    write.over(perFileBenchmarksPath,           "language,variant,score,error,low,high,size\n")
    write.over(perFileBenchmarksNormalizedPath, "language,variant,score,low,high,size\n")

    args.languages.foreach { language =>
        println(" " + language.name)

        // Measurements

        write.append(parseTableMeasurementsPath, "\n" + language.id + "," + read.lines(language.measurementsDir / "parsetable.csv")(1))
        write.append(parsingMeasurementsPath,    "\n" + language.id + "," + read.lines(language.measurementsDir / "parsing.csv")(1))

        // Benchmarks (batch)
        
        // Normalization: chars / ms == 1000 chars / s
        val characters = BigDecimal(CSV.parse(language.measurementsDir / "parsing.csv").rows.head("characters"))
        val normalizeBatch: BigDecimal => BigDecimal = score => characters / score

        def processBenchmarkCSV(benchmarkCSV: CSV, variant: CSVRow => String, destinationPath: Path, destinationPathNormalized: Path, normalize: BigDecimal => BigDecimal, append: String = "") = {
            benchmarkCSV.rows.foreach { row =>
                val rawScore = row("\"Score\"")
                val rawError = row("\"Score Error (99.9%)\"")

                val score = BigDecimal(rawScore)
                val error = if (rawError != "NaN") BigDecimal(rawError) else BigDecimal(0)

                write.append(destinationPath, language.id + "," + variant(row) + "," + round(score) + "," + round(error) + "," + round(score - error) + "," + round(score + error) + append + "\n")
                write.append(destinationPathNormalized, language.id + "," + variant(row) + "," + round(normalize(score)) + "," + round(normalize(score + error)) + "," + round(normalize(score - error)) + append + "\n")
            }
        }

        processBenchmarkCSV(CSV.parse(language.benchmarksDir / "jsglr2.csv"), row => row("\"Param: variant\""), batchBenchmarksPath, batchBenchmarksNormalizedPath, normalizeBatch)
        processBenchmarkCSV(CSV.parse(language.benchmarksDir / "jsglr1.csv"), _   => "jsglr1"                 , batchBenchmarksPath, batchBenchmarksNormalizedPath, normalizeBatch)

        language.antlrBenchmarks.foreach { antlrBenchmark =>
            processBenchmarkCSV(CSV.parse(language.benchmarksDir / s"${antlrBenchmark.id}.csv"), _ => antlrBenchmark.id, batchBenchmarksPath, batchBenchmarksNormalizedPath, normalizeBatch)
        }

        // Benchmarks (per file)

        language.sourceFilesPerFileBenchmark.foreach { file =>
            val characters = (read! file).length
            val normalizePerFile: BigDecimal => BigDecimal = score => characters / score

            processBenchmarkCSV(CSV.parse(language.benchmarksDir / "perFile" / s"${file.last.toString}.csv"), row => row("\"Param: variant\""), perFileBenchmarksPath, perFileBenchmarksNormalizedPath, normalizePerFile, "," + characters)
        }
    }
}

@main
def ini(args: String*) = withArgs(args :_ *)(postProcess(_))
