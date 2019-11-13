import $ivy.`com.lihaoyi::ammonite-ops:1.8.1`, ammonite.ops._

import $file.args, args._, Args._
import $file.config, config.config

def setupLanguages(implicit args: Args) = {
    println("Setting up languages...")
    
    mkdir! languagesDir

    config.languages.foreach { language =>
        println(" " + language.id)
        
        rm! language.repoDir
        mkdir! language.repoDir

        println(s"  Cloning ${language.repo}...")
        %%("git", "clone", language.repo, ".")(language.repoDir)

        println(s"  Building ${language.dir}...")
        %%("mvn", "install")(language.dir)
    }
}

@main
def ini(args: String*) = withArgs(args :_ *)(setupLanguages(_))