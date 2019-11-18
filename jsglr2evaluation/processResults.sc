import $ivy.`com.lihaoyi::ammonite-ops:1.8.1`, ammonite.ops._

import $file.common, common._, Args._

import $file.spoofax, spoofax._
import org.spoofax.jsglr2.JSGLR2Variant
import org.spoofax.jsglr2.integration.IntegrationVariant
import org.spoofax.jsglr2.integration.ParseTableVariant

def processResults(implicit args: Args) = {
    println("Processing results...")
    
    mkdir! resultsDir

    write.over(parseTableMeasurementsPath,      "")
    write.over(parsingMeasurementsPath,         "")
    write.over(batchBenchmarksPath,             "")
    write.over(batchBenchmarksNormalizedPath,   "")
    write.over(perFileBenchmarksPath,           "")
    write.over(perFileBenchmarksNormalizedPath, "")

    config.languages.zipWithIndex.foreach { case(language, index) =>
        println(" " + language.name)

        if (index == 0) {
            // Copy header from measurements CSV
            write.append(parseTableMeasurementsPath, "language," + read.lines(language.measurementsDir / "parsetable.csv")(0))
            write.append(parsingMeasurementsPath,    "language," + read.lines(language.measurementsDir / "parsing.csv")(0))

            // Add header to benchmarks CSV
            write.append(batchBenchmarksPath,             "language,variant,score,error")
            write.append(batchBenchmarksNormalizedPath,   "language,variant,score,error")
            write.append(perFileBenchmarksPath,           "language,variant,score,error,size")
            write.append(perFileBenchmarksNormalizedPath, "language,variant,score,error,size")
        }

        // Measurements

        write.append(parseTableMeasurementsPath, "\n" + language.id + "," + read.lines(language.measurementsDir / "parsetable.csv")(1))
        write.append(parsingMeasurementsPath,    "\n" + language.id + "," + read.lines(language.measurementsDir / "parsing.csv")(1))

        // Benchmarks (batch)

        val batchBenchmarkCSV = CSV.parse(language.benchmarksPath)

        batchBenchmarkCSV.rows.foreach { row =>
            val score = BigDecimal(row("\"Score\""))
            val characters = BigDecimal(CSV.parse(language.measurementsDir / "parsing.csv").rows.head("characters"))
            val normalized = characters / score // chars / ms == k chars / s

            write.append(batchBenchmarksPath, "\n" + language.id + "," + row("\"Param: jsglr2Variant\"") + "," + round(score) + "," + row("\"Score Error (99.9%)\""))
            write.append(batchBenchmarksNormalizedPath, "\n" + language.id + "," + row("\"Param: jsglr2Variant\"") + "," + round(normalized) + "," + row("\"Score Error (99.9%)\""))
        }

        // Benchmarks (per file)

        val files = ls.rec! language.sourcesDir

        files.filterNot(_.last.toString.startsWith(".")).foreach { file =>
            val perFileBenchmarkCSV = CSV.parse(language.benchmarksPath(file.last.toString))

            perFileBenchmarkCSV.rows.foreach { row =>
                val score = BigDecimal(row("\"Score\""))
                
                val characters = (read! file).length
                val normalized = characters / score // chars / ms == k chars / s

                write.append(perFileBenchmarksPath, "\n" + language.id + "," + row("\"Param: jsglr2Variant\"") + "," + round(score) + "," + row("\"Score Error (99.9%)\"") + "," + characters)
                write.append(perFileBenchmarksNormalizedPath, "\n" + language.id + "," + row("\"Param: jsglr2Variant\"") + "," + round(normalized) + "," + row("\"Score Error (99.9%)\"") + "," + characters)
            }
        }
    }
}

@main
def ini(args: String*) = withArgs(args :_ *)(processResults(_))