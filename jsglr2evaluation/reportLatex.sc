import $ivy.`com.lihaoyi::ammonite-ops:1.8.1`, ammonite.ops._

import $file.common, common._, Args._

import $file.spoofax, spoofax._
import org.spoofax.jsglr2.JSGLR2Variant
import org.spoofax.jsglr2.integration.IntegrationVariant
import org.spoofax.jsglr2.integration.ParseTableVariant

def latexTableTestSets(implicit args: Args) = {
    val s = new StringBuilder()

    s.append("\\begin{tabular}{|l|l|r|r|r|}\n")
    s.append("\\hline\n")
    s.append("Language & Source & Files & Lines & Size (bytes) \\\\\n")
    s.append("\\hline\n")

    config.languages.foreach { language =>
        s.append("\\multirow{" + language.sources.size + "}{*}{" + language.name + "}\n")

        language.sources.zipWithIndex.foreach { case (source, index) =>
            val files = ls.rec! language.sourcesDir
            val lines = files | read.lines | (_.size) sum
            val size = files | stat | (_.size) sum

            s.append("  & " + source.id + " & " + files.size + " & " + lines + " & " + size + " \\\\ ")

            if (index == language.sources.size - 1)
                s.append("\\hline\n");
            else
                s.append("\\cline{2-5}\n")
        }
    }

    s.append("\\end{tabular}\n")

    s.toString
}

def latexTableMeasurements(csv: CSV) = {
    val s = new StringBuilder()

    s.append("\\begin{tabular}{|l|" + ("r|" * config.languages.size) + "}\n")
    s.append("\\hline\n")
    s.append("Measure" + config.languages.map(" & " + _.name).mkString("") + " \\\\\n")
    s.append("\\hline\n")

    csv.columns.filter(_ != "language").foreach { column =>
        s.append(column)

        config.languages.foreach { language =>
            val row = csv.rows.find(_("language") == language.id).get
            val value = row(column)

            s.append(" & " + value);
        }

        s.append(" \\\\ \\hline\n");
    }

    s.append("\\end{tabular}\n")

    s.toString
}

def latexTableBenchmarks(benchmarksCSV: CSV, benchmarkType: BenchmarkType)(implicit args: Args) = {
    val s = new StringBuilder()

    s.append("\\begin{tabular}{|l|" + ("r|" * (config.languages.size * (1 + benchmarkType.errorColumns.size))) + "}\n")
    s.append("\\hline\n")
    s.append("\\multirow{2}{*}{Variant}" + config.languages.map(language => s" & \\multicolumn{${1 + benchmarkType.errorColumns.size}}{c|}{${language.name}}").mkString("") + " \\\\\n")
    s.append(s"\\cline{2-${1 + config.languages.size * (1 + benchmarkType.errorColumns.size)}}\n")
    s.append(config.languages.map(_ => " & Score" + benchmarkType.errorColumns.map(" & " + _._2).mkString).mkString + " \\\\\n")
    s.append("\\hline\n")

    val variants = benchmarksCSV.rows.map(_("variant")).distinct

    variants.foreach { variant =>
        s.append(variant)

        config.languages.foreach { language =>
            def get(column: String) = benchmarksCSV.rows.find { row =>
                row("language") == language.id &&
                row("variant") == variant
            } match {
                case Some(row) => round(row(column))
                case None      => ""
            }

            s.append(" & " + get("score"))

            benchmarkType.errorColumns.foreach { case (errorColumn, _) =>
                s.append(" & " + get(errorColumn));
            }
        }

        s.append(" \\\\ \\hline\n");
    }

    s.append("\\end{tabular}\n")

    s.toString
}

trait BenchmarkType {
    def errorColumns: Seq[(String, String)]
}
case object Time extends BenchmarkType {
    def errorColumns = Seq("error" -> "Error")
}
case object Throughput extends BenchmarkType {
    def errorColumns = Seq("low" -> "Low", "high" -> "High")
}

def reportLatex(implicit args: Args) = {
    println("LateX reporting...")
    
    mkdir! args.reportDir

    write.over(args.reportDir / "testsets.tex", latexTableTestSets)
    write.over(args.reportDir / "measurements-parsetables.tex", latexTableMeasurements(CSV.parse(parseTableMeasurementsPath)))
    write.over(args.reportDir / "measurements-parsing.tex",     latexTableMeasurements(CSV.parse(parsingMeasurementsPath)))
    write.over(args.reportDir / "benchmarks-time.tex",          latexTableBenchmarks(CSV.parse(batchBenchmarksPath), Time))
    write.over(args.reportDir / "benchmarks-throughput.tex",    latexTableBenchmarks(CSV.parse(batchBenchmarksNormalizedPath), Throughput))
}

@main
def ini(args: String*) = withArgs(args :_ *)(reportLatex(_))