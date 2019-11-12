import $ivy.`com.lihaoyi::ammonite-ops:1.8.1`
import ammonite.ops._

import $file.config
import config.config

def setupLanguages(dir: Path) = {
    println("Setting up languages...")

    val languagesDir = dir / 'languages
    
    mkdir! languagesDir

    config.languages.foreach { language =>
        println(language.id)

        val languageRepoDir = languagesDir / language.id
        val languageDir = languageRepoDir / language.path
        
        rm! languageRepoDir
        mkdir! languageRepoDir

        println(s"Cloning ${language.repo}...")
        %%("git", "clone", language.repo, ".")(languageRepoDir)

        println(s"Building ${languageDir}...")
        %%("mvn", "install")(languageDir)
    }
}

@main
def ini(args: String*) = {
    val dir = args match {
        case Seq(dir) => Path(dir, root)
    }

    setupLanguages(dir)
}