import $ivy.`com.lihaoyi::ammonite-ops:1.8.1`
import ammonite.ops._

case class Args(dir: Path)

def withArgs(args: String*)(body: Args => Unit) = {
    val dir = args match {
        case Seq(dir) => Path(dir, root)
    }

    body(Args(dir))
}