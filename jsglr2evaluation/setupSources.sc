import $ivy.`com.lihaoyi::ammonite-ops:1.8.1`, ammonite.ops._

import $file.common, common._, Args._

def preProcess(file: String) =
    // Replace Unicode
    file.replaceAll("[^\\x00-\\xFF]", "?")

def setupSources(implicit args: Args) = {
    println("Setting up sources...")
    
    mkdir! sourcesDir

    config.languages.foreach { language =>
        println(" " + language.id)
        
        rm! language.sourcesDir
        mkdir! language.sourcesDir / "repos"

        language.sources.foreach { source =>
            println("  " + source.id)
            
            val languageSourceRepoDir = language.sourcesDir / "repos" / source.id
        
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
            files.foreach { file =>
                val pathInRepo = file relativeTo languageSourceRepoDir
                val filename = source.id + "_" + pathInRepo.toString.replace("/", "_")

                val preProcessed = preProcess(read! file)

                write(language.sourcesDir / filename, preProcessed)
                rm! file
            }
        }

        // Repo dirs are now empty, remove
        rm! language.sourcesDir / "repos"
    }
}

@main
def ini(args: String*) = withArgs(args :_ *)(setupSources(_))