import $ivy.`com.lihaoyi::ammonite-ops:1.8.1`
import $ivy.`io.circe::circe-generic:0.12.3`
import $ivy.`io.circe::circe-yaml:0.11.0-M1`

import ammonite.ops._
import cats.syntax.either._
import io.circe.generic.auto._
import io.circe.yaml._
import java.time.LocalDateTime

case class Config(languages: Seq[Language])

case class Language(id: String, extension: String, repo: String, path: String, sources: Seq[Source]) {
    def repoDir(implicit args: Args) = Args.languagesDir / id
    def dir(implicit args: Args) = repoDir / path
    def sourcesDir(implicit args: Args) = Args.sourcesDir / id
    def parseTablePath(implicit args: Args) = dir / "target" / "metaborg" / "sdf.tbl"
    def measurementsDir(implicit args: Args) = Args.measurementsDir / id
    def benchmarksPath(implicit args: Args) = Args.benchmarksDir / (id + ".csv")
}

case class Source(id: String, repo: String)

val configJson = parser.parse(read! pwd/"config.yml")
val config = configJson.flatMap(_.as[Config]).valueOr(throw _)

case class Args(dir: Path)

object Args {

    implicit def languagesDir(implicit args: Args) = args.dir / 'languages
    implicit def sourcesDir(implicit args: Args)   = args.dir / 'sources
    implicit def measurementsDir(implicit args: Args)   = args.dir / 'measurements
    implicit def benchmarksDir(implicit args: Args)   = args.dir / 'benchmarks
}

def withArgs(args: String*)(body: Args => Unit) = {
    val dir = args match {
        case Seq(dir) => Path(dir, root)
    }

    body(Args(dir))
}

def timed(name: String)(block: => Unit)(implicit args: Args): Unit = {
    val t0 = System.currentTimeMillis()
    block
    val t1 = System.currentTimeMillis()

    val time = (BigDecimal(t1 - t0)) / 1000
    val report = s"$name: ${time}s"

    println(report)

    write.append(args.dir / "timing.txt", s"${LocalDateTime.now} $report\n")
}