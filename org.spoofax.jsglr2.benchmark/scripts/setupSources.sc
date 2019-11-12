import $ivy.`com.lihaoyi::ammonite-ops:1.8.1`
import ammonite.ops._

import $file.config
import config.config

def setupSources(dir: Path) = {
    println("Setting up sources...")

    val sourcesDir = dir / 'sources
    
    mkdir! sourcesDir

    config.languages.foreach { language =>
        println(language.id)

        val languageSourcesDir = sourcesDir / language.id
        
        rm! languageSourcesDir
        mkdir! languageSourcesDir

        language.sources.foreach { source =>
            val languageSourceDir = languageSourcesDir / source.id
        
            rm! languageSourceDir
            mkdir! languageSourceDir

            // Initially clone without checking out
            %%("git", "clone", "--no-checkout", source.repo, ".")(languageSourceDir)

            // Config sparse checkout: filter files based on extension
            %%("git", "config", "core.sparseCheckout", "true")(languageSourceDir)
            write(languageSourceDir / ".git" / "info" / "sparse-checkout", "*." + language.extension)

            // Pull with the filter, skip history
            %%("git", "checkout", "master")(languageSourceDir)
        }
    }
}

@main
def ini(args: String*) = {
    val dir = args match {
        case Seq(dir) => Path(dir, root)
    }

    setupSources(dir)
}