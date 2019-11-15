import $ivy.`com.lihaoyi::ammonite-ops:1.8.1`, ammonite.ops._

import $file.common, common._, Args._

import $file.spoofax, spoofax._
import org.spoofax.jsglr2.JSGLR2Variant
import org.spoofax.jsglr2.integration.IntegrationVariant
import org.spoofax.jsglr2.integration.ParseTableVariant

def latexTableTestSets(implicit args: Args) = {
    val s = new StringBuilder()

    s.append("\\begin{table}[]\n")
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
    s.append("\\end{table}\n")

    s.toString
}

def latexTableMeasurements(csv: CSV) = {
    val s = new StringBuilder()

    s.append("\\begin{table}[]\n")
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
    s.append("\\end{table}\n")

    s.toString
}

def latexTableParseTables(implicit args: Args) = {
    latexTableMeasurements(CSV.parse(parseTableMeasurementsPath))
}

def latexTableParsing(implicit args: Args) = {
    latexTableMeasurements(CSV.parse(parsingMeasurementsPath))
}

def latexTableBenchmarks(implicit args: Args) = {
    val s = new StringBuilder()

    s.append("\\begin{table}[]\n")
    s.append("\\begin{tabular}{|l|" + ("r|" * config.languages.size) + "}\n")
    s.append("\\hline\n")
    s.append("Variant" + config.languages.map(" & " + _.name).mkString("") + " \\\\\n")
    s.append("\\hline\n")

    val benchmarksCSV = CSV.parse(benchmarksPath)

    val variants = benchmarksCSV.rows.map(_("variant")).distinct

    variants.foreach { variant =>
        s.append(variant)

        config.languages.foreach { language =>
            val row = benchmarksCSV.rows.find { row =>
                row("language") == language.id &&
                row("variant") == variant
            }.get

            val score = round(row("score"))
            val error = round(row("error"))

            s.append(" & " + score + " (" + error + ")");
        }

        s.append(" \\\\ \\hline\n");
    }

    s.append("\\end{tabular}\n")
    s.append("\\end{table}\n")

    s.toString
}

def reportLatex(implicit args: Args) = {
    println("LateX reporting...")
    
    mkdir! args.latexDir

    write.over(args.latexDir / "testsets.tex", latexTableTestSets)
    write.over(args.latexDir / "measurements-parsetables.tex", latexTableParseTables)
    write.over(args.latexDir / "measurements-parsing.tex", latexTableParsing)
    write.over(args.latexDir / "benchmarks.tex", latexTableBenchmarks)
}

@main
def ini(args: String*) = withArgs(args :_ *)(reportLatex(_))