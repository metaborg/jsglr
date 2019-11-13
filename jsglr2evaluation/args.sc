import $ivy.`com.lihaoyi::ammonite-ops:1.8.1`
import ammonite.ops._

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