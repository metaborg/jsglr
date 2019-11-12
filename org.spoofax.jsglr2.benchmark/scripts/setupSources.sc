import $ivy.`com.lihaoyi::ammonite-ops:1.8.1`
import ammonite.ops._

import $file.config
import config.config

def setupSources(dir: Path) = {
    println("Setting up sources...")

    val sourcesDir = dir / 'sources
    
    mkdir! sourcesDir

    config.languages.foreach { language =>
        println(" " + language.id)

        val languageSourcesDir = sourcesDir / language.id
        
        rm! languageSourcesDir
        mkdir! languageSourcesDir / "repos"

        language.sources.foreach { source =>
            println("  " + source.id)
            
            val languageSourceRepoDir = languageSourcesDir / "repos" / source.id
        
            rm! languageSourceRepoDir
            mkdir! languageSourceRepoDir

            // Initially clone without checking out and without all history
            %%("git", "clone", "--no-checkout", "--depth=1", source.repo, ".")(languageSourceRepoDir)

            // Config sparse checkout: filter files based on extension
            %%("git", "config", "core.sparseCheckout", "true")(languageSourceRepoDir)
            write(languageSourceRepoDir / ".git" / "info" / "sparse-checkout", "*." + language.extension)

            // Pull with the filter, skip history
            %%("git", "checkout", "master")(languageSourceRepoDir)

            val files = ls.rec! languageSourceRepoDir |? (_.ext == language.extension)

            // Copy all files to the aggregated directory
            files.map { file =>
                val pathInRepo = file relativeTo languageSourceRepoDir
                val filename = source.id + "_" + pathInRepo.toString.replace("/", "_")

                mv(file, languageSourcesDir / filename)
            }
        }

        // Repo dirs are now empty, remove
        rm! languageSourcesDir / "repos"
    }
}

@main
def ini(args: String*) = {
    val dir = args match {
        case Seq(dir) => Path(dir, root)
    }

    setupSources(dir)
}