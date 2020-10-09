import $ivy.`com.lihaoyi::ammonite-ops:1.8.1`, ammonite.ops._

import $file.common, common._, Args._

def preProcess(file: String) =
    // Replace Unicode
    file.replaceAll("[^\\x00-\\xFF]", "?")

def setupSources(implicit args: Args) = {
    println("Setting up sources...")
    
    mkdir! sourcesDir

    args.languages.foreach { language =>
        println(" " + language.name)
        
        rm! language.sourcesDir
        mkdir! language.sourcesDir / "repos"
        mkdir! language.sourcesDir / "batch"
        mkdir! language.sourcesDir / "incremental"

        // Inspiration: https://briancoyner.github.io/articles/2013-06-05-git-sparse-checkout/
        def clone(source: RepoSource, languageSourceRepoDir: Path) = {
            %%("git", "init")(languageSourceRepoDir)

            // Config sparse checkout
            %%("git", "config", "core.sparseCheckout", "true")(languageSourceRepoDir)
            write(languageSourceRepoDir / ".git" / "info" / "sparse-checkout", source match {
                // If a list of files is given: only checkout these files
                case IncrementalSource(_, _, _, files) if files.nonEmpty => files.mkString("\n")
                // Else: filter files based on extension
                case _ => "*." + language.extension
            })

            %%("git", "remote", "add", "origin", source.repo)(languageSourceRepoDir)

            source match {
                case BatchRepoSource(_, repo) =>
                    // Clone without all history
                    %%("git", "fetch", "origin", "master", "--depth=1")(languageSourceRepoDir)
                case IncrementalSource(_, repo, fetchOptions, _) =>
                    // Clone with full history, possibly limited by the fetchOptions
                    %%("git", "fetch", "origin", "master", fetchOptions)(languageSourceRepoDir)
            }

            %%("git", "checkout", "master")(languageSourceRepoDir)
        }

        language.sources.batch.foreach { source =>
            println("  " + source.id)

            val languageSourceDir =
                source match {
                    case repoSource: RepoSource =>
                        val languageSourceRepoDir = language.sourcesDir / "repos" / source.id

                        rm! languageSourceRepoDir
                        mkdir! languageSourceRepoDir

                        timed("clone " + source.id)(clone(repoSource, languageSourceRepoDir))

                        languageSourceRepoDir
                    case localSource: LocalSource =>
                        getPath(localSource.path)
                }

            timed("collect " + source.id) {
                val files = ls.rec! languageSourceDir |? (_.ext == language.extension)

                // Copy all files to the aggregated directory
                files.foreach { file =>
                    val pathInSource = file relativeTo languageSourceDir
                    val filename = source.id + "_" + pathInSource.toString.replace("/", "_").replace(".", "_")

                    val preProcessed = preProcess(read! file)

                    write(language.sourcesDir / "batch" / filename, preProcessed)
                }
            }
        }

        language.sources.incremental.foreach { source =>
            println("  " + source.id)

            val languageSourceRepoDir = language.sourcesDir / "repos" / source.id

            rm! languageSourceRepoDir
            mkdir! languageSourceRepoDir

            timed("clone " + source.id)(clone(source, languageSourceRepoDir))

            timed("preprocess " + source.id) {
                val files = if (source.files.nonEmpty) source.files.map(f => languageSourceRepoDir / RelPath(f))
                            else ls.rec! languageSourceRepoDir |? (_.ext == language.extension)

                val revisions = (
                    if (source.files.nonEmpty)
                        %%("git", "log", "--format=%H", "--reverse", "--", files.map(_.toString).toSeq)(languageSourceRepoDir)
                    else
                        %%("git", "log", "--format=%H", "--reverse", "--", "*." + language.extension)(languageSourceRepoDir)
                ).out.string.split("\n")

                revisions.zipWithIndex.foreach { case (revision, i) =>
                    %%("git", "checkout", "-f", revision)(languageSourceRepoDir)

                    val revisionPath = language.sourcesDir / "incremental" / source.id / i.toString
                    mkdir! revisionPath

                    // Copy all files to the aggregated directory
                    files.filter(_.toIO.exists).foreach { file =>
                        val pathInRepo = file relativeTo languageSourceRepoDir
                        val filename = pathInRepo.toString.replace("/", "_")

                        val preProcessed = preProcess(read! file)

                        write(revisionPath / filename, preProcessed)
                    }
                }
            }
        }

        // Repo dirs are now processed, remove
        rm! language.sourcesDir / "repos"
    }
}

@main
def ini(args: String*) = withArgs(args :_ *)(setupSources(_))
