import $ivy.`com.lihaoyi::ammonite-ops:1.8.1`, ammonite.ops._

import $file.args, args._
import $file.config, config.config

def setupLanguages(implicit args: Args) = {
    println("Setting up languages...")
    
    mkdir! languagesDir

    config.languages.foreach { implicit language =>
        println(" " + language.id)
        
        rm! languageRepoDir
        mkdir! languageRepoDir

        println(s"  Cloning ${language.repo}...")
        %%("git", "clone", language.repo, ".")(languageRepoDir)

        println(s"  Building ${languageDir}...")
        %%("mvn", "install")(languageDir)
    }
}

@main
def ini(args: String*) = withArgs(args :_ *)(setupLanguages(_))