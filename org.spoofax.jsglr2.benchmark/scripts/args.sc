import $ivy.`com.lihaoyi::ammonite-ops:1.8.1`
import ammonite.ops._

import $file.model, model._

case class Args(dir: Path)

def withArgs(args: String*)(body: Args => Unit) = {
    val dir = args match {
        case Seq(dir) => Path(dir, root)
    }

    body(Args(dir))
}

implicit def languagesDir(implicit args: Args) = args.dir / 'languages
implicit def languageRepoDir(implicit args: Args, language: Language) = languagesDir / language.id
implicit def languageDir(implicit args: Args, language: Language) = languageRepoDir / language.path

implicit def sourcesDir(implicit args: Args)   = args.dir / 'sources
implicit def languageSourcesDir(implicit args: Args, language: Language) = sourcesDir / language.id