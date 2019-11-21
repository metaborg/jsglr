import $ivy.`com.lihaoyi::ammonite-ops:1.8.1`
import $ivy.`io.circe::circe-generic:0.12.3`
import $ivy.`io.circe::circe-yaml:0.11.0-M1`

import ammonite.ops._
import cats.syntax.either._
import io.circe.generic.auto._
import io.circe.yaml._
import java.time.LocalDateTime

case class Config(languages: Seq[Language])

case class Language(id: String, name: String, extension: String, repo: String, antlrBenchmark: Option[String], path: String, sources: Seq[Source]) {
    def repoDir(implicit args: Args) = Args.languagesDir / id
    def dir(implicit args: Args) = repoDir / RelPath(path)
    def sourcesDir(implicit args: Args) = Args.sourcesDir / id
    def parseTablePath(implicit args: Args) = dir / "target" / "metaborg" / "sdf.tbl"
    def measurementsDir(implicit args: Args) = Args.measurementsDir / id
    def benchmarksDir(implicit args: Args) = Args.benchmarksDir / id
    
    def sourceFiles(implicit args: Args) = ls! sourcesDir |? (_.ext == extension)
    def sourceFilesPerFileBenchmark(implicit args: Args): Seq[Path] = {
        val files = sourceFiles sortBy(-_.size)
        val trimPercentage: Float = 10F
        val filesTrimmed = files.slice(
            ((trimPercentage / 100F) * files.size).toInt,
            (((100F - trimPercentage) / 100F) * files.size).toInt
        )

        val fileCount = filesTrimmed.size
        val samples = 10
        val step = fileCount / samples

        for (i <- 0 until samples) yield filesTrimmed(i * step)
    }
}

case class Source(id: String, repo: String)

val configJson = parser.parse(read! pwd/"config.yml")
val config = configJson.flatMap(_.as[Config]).valueOr(throw _)

case class Args(dir: Path, iterations: Int, reportDir: Path)

object Args {

    implicit def languagesDir(implicit args: Args)    = args.dir / 'languages
    implicit def sourcesDir(implicit args: Args)      = args.dir / 'sources
    implicit def measurementsDir(implicit args: Args) = args.dir / 'measurements
    implicit def benchmarksDir(implicit args: Args)   = args.dir / 'benchmarks
    implicit def resultsDir(implicit args: Args)      = args.dir / 'results
    
    implicit def parseTableMeasurementsPath(implicit args: Args) = resultsDir / "measurements-parsetable.csv"
    implicit def parsingMeasurementsPath(implicit args: Args)    = resultsDir / "measurements-parsing.csv"

    implicit def batchBenchmarksPath(implicit args: Args)             = resultsDir / "benchmarks-batch-time.csv"
    implicit def batchBenchmarksNormalizedPath(implicit args: Args)   = resultsDir / "benchmarks-batch-throughput.csv"
    implicit def perFileBenchmarksPath(implicit args: Args)           = resultsDir / "benchmarks-perFile-time.csv"
    implicit def perFileBenchmarksNormalizedPath(implicit args: Args) = resultsDir / "benchmarks-perFile-throughput.csv"

}

def withArgs(args: String*)(body: Args => Unit) = {
    val (dir, iterations, reportDir) = args match {
        case Seq(dir) => (Path(dir, root), 0, None)
        case Seq(dir, iterations) => (Path(dir, root), iterations.toInt, None)
        case Seq(dir, iterations, reportDir) => (Path(dir, root), iterations.toInt, Some(Path(reportDir, root)))
    }

    body(Args(dir, iterations, reportDir.getOrElse(dir / "reports")))
}

def timed(name: String)(block: => Unit)(implicit args: Args): Unit = {
    println(s"$name: start @ ${LocalDateTime.now}")
    val t0 = System.currentTimeMillis()
    
    block
    val t1 = System.currentTimeMillis()

    val seconds = (BigDecimal(t1 - t0)) / 1000

    val report =
        s"$name: finished in " +
        (if (seconds < 60)
            s"${round(seconds, 1)}s"
        else if (seconds < 3600)
            s"${round(seconds / 60, 1)}m"
        else
            s"${round(seconds / 3600, 1)}h")

    println(report)

    write.append(args.dir / "timing.txt", s"${LocalDateTime.now} $report\n")
}

case class CSV(columns: Seq[String], rows: Seq[CSVRow])
case class CSVRow(values: Map[String, String]) {
    def apply(column: String) = values.get(column).getOrElse("")
}

object CSV {

    def parse(file: Path): CSV = {
        read.lines(file) match {
            case headerLine +: rowLines =>
                val columns = headerLine.split(",").toSeq

                val rows = rowLines.map { row =>
                    CSVRow((columns zip row.split(",")).toMap)
                }

                CSV(columns, rows)
        }
    }

}

import scala.math.BigDecimal.RoundingMode

def round(number: BigDecimal, scale: Int = 0): BigDecimal = number.setScale(scale, RoundingMode.HALF_UP)
def round(number: String): String = if (number != "NaN" && number != "") round(BigDecimal(number)).toString else number