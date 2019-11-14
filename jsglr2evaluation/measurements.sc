import $ivy.`com.lihaoyi::ammonite-ops:1.8.1`, ammonite.ops._

import $file.common, common._

import $file.spoofax, spoofax._
import org.spoofax.jsglr2.JSGLR2Variant
import org.spoofax.jsglr2.integration.IntegrationVariant
import org.spoofax.jsglr2.integration.ParseTableVariant

def execMeasurements(implicit args: Args) = {
    println("Executing measurements...")

    config.languages.foreach { language =>
        println(" " + language.id)

        val measurementsMvnDir = (pwd / up / "org.spoofax.jsglr2.measure")

        timed("measure " + language.id) {
            %%("mvn", "exec:java", "-Dexec.args=\"" + language.id + " " + language.extension + " " + language.parseTablePath + " " + language.sourcesDir + "\"", "-DreportPath=" + language.measurementsDir)(measurementsMvnDir)
        }
    }
}

@main
def ini(args: String*) = withArgs(args :_ *)(execMeasurements(_))