import $ivy.`com.lihaoyi::ammonite-ops:1.8.1`, ammonite.ops._

import $file.common, common._, Args._

import $file.spoofax, spoofax._
import org.spoofax.jsglr2.JSGLR2Variant
import org.spoofax.jsglr2.integration.IntegrationVariant
import org.spoofax.jsglr2.integration.ParseTableVariant

def processResults(implicit args: Args) = {
    println("Processing results...")
    
    mkdir! resultsDir

    val parsingMeasurementsPath    = resultsDir / "measurements-parsing.csv"
    val parseTableMeasurementsPath = resultsDir / "measurements-parsetable.csv"
    val benchmarksPath             = resultsDir / "benchmarks.csv"

    write.over(parsingMeasurementsPath,    "")
    write.over(parseTableMeasurementsPath, "")
    write.over(benchmarksPath,             "")

    config.languages.zipWithIndex.foreach { case(language, index) =>
        println(" " + language.id)

        if (index == 0) {
            // Copy header from measurements CSV
            write.append(parseTableMeasurementsPath, "language," + read.lines(language.measurementsDir / "parsetable.csv")(0))
            write.append(parsingMeasurementsPath,    "language," + read.lines(language.measurementsDir / "parsing.csv")(0))

            // Add header to benchmarks CSV
            write.append(benchmarksPath, "language,variant,score,error")
        }

        write.append(parseTableMeasurementsPath, "\n" + language.id + "," + read.lines(language.measurementsDir / "parsetable.csv")(1))
        write.append(parsingMeasurementsPath,    "\n" + language.id + "," + read.lines(language.measurementsDir / "parsing.csv")(1))

        val benchmarksCSV = CSV.parse(language.benchmarksPath)

        benchmarksCSV.map { row =>
            write.append(benchmarksPath, "\n" + language.id + "," + row.get("\"Param: jsglr2Variant\"").get + "," + row.get("\"Score\"").get + "," + row.get("\"Score Error (99.9%)\"").get)
        }
    }
}

@main
def ini(args: String*) = withArgs(args :_ *)(processResults(_))