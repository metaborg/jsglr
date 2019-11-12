import $ivy.`com.lihaoyi::ammonite-ops:1.8.1`, ammonite.ops._

import $file.args, args._
import $file.config, config.config

def setupLanguages(args: Args) = {
    println("Setting up languages...")

    val languagesDir = args.dir / 'languages
    
    mkdir! languagesDir

    config.languages.foreach { language =>
        println(" " + language.id)

        val languageRepoDir = languagesDir / language.id
        val languageDir = languageRepoDir / language.path
        
        rm! languageRepoDir
        mkdir! languageRepoDir

        println(s"  Cloning ${language.repo}...")
        %%("git", "clone", language.repo, ".")(languageRepoDir)

        println(s"  Building ${languageDir}...")
        %%("mvn", "install")(languageDir)
    }
}

@main
def ini(args: String*) = withArgs(args :_ *)(setupLanguages _)