import $ivy.`com.lihaoyi::ammonite-ops:1.8.1`, ammonite.ops._

import $file.common, common._

import $file.spoofax, spoofax._
import org.spoofax.jsglr2.JSGLR2Variant
import org.spoofax.jsglr2.integration.IntegrationVariant
import org.metaborg.parsetable.ParseTableVariant

def execMeasurements(implicit args: Args) = {
    println("Executing measurements...")

    config.languages.foreach { language =>
        println(" " + language.name)

        val measurementsMvnDir = (pwd / up / "org.spoofax.jsglr2.measure")

        timed("measure " + language.id) {
            %%(
                "mvn",
                "exec:java",
                "-Dexec.args=\""+
                    s"language=${language.id} " +
                    s"extension=${language.extension} " +
                    s"parseTablePath=${language.parseTablePath} " +
                    s"sourcePath=${language.sourcesDir} " +
                    s"type=multiple" +
                "\"",
                "-DreportPath=" + language.measurementsDir,
                MAVEN_OPTS="-Xmx8G -Xss64M"
            )(measurementsMvnDir)
        }
    }
}

@main
def ini(args: String*) = withArgs(args :_ *)(execMeasurements(_))