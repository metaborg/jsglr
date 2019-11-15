import $ivy.`com.lihaoyi::ammonite-ops:1.8.1`, ammonite.ops._

import $file.common, common._, Args._

import $file.spoofax, spoofax._
import org.spoofax.jsglr2.JSGLR2Variant
import org.spoofax.jsglr2.integration.IntegrationVariant
import org.spoofax.jsglr2.integration.ParseTableVariant

def processResults(implicit args: Args) = {
    println("Processing results...")
    
    mkdir! resultsDir

    write.over(parseTableMeasurementsPath, "")
    write.over(parsingMeasurementsPath,    "")
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

        benchmarksCSV.rows.foreach { row =>
            write.append(benchmarksPath, "\n" + language.id + "," + row("\"Param: jsglr2Variant\"") + "," + row("\"Score\"") + "," + row("\"Score Error (99.9%)\""))
        }
    }
}

@main
def ini(args: String*) = withArgs(args :_ *)(processResults(_))