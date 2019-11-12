import $ivy.`com.lihaoyi::ammonite-ops:1.8.1`, ammonite.ops._

import $file.args, args._
import $file.config, config.config

import $file.spoofax, spoofax._
import org.spoofax.jsglr2.JSGLR2Variant
import org.spoofax.jsglr2.integration.IntegrationVariant
import org.spoofax.jsglr2.integration.ParseTableVariant

def validateSources(implicit args: Args) = {
    println("Validating sources...")

    config.languages.foreach { implicit language =>
        println(" " + language.id)

        val files = ls.rec! languageSourcesDir

        val variant = new IntegrationVariant(
            new ParseTableVariant(),
            JSGLR2Variant.Preset.standard.variant
        )

        val parseTablePath = languageDir / "target" / "metaborg" / "sdf.tbl"

        val jsglr2 = getJSGLR2(variant, parseTablePath)

        files.foreach { file =>
            val ast = jsglr2.parse(read! file)

            if (ast == null) {
                val filename = file relativeTo languageSourcesDir

                println("   Invalid: " + filename)

                mkdir! languageSourcesDir / "invalid"
                mv(file, languageSourcesDir / "invalid" / filename)
            }
        }
    }
}

@main
def ini(args: String*) = withArgs(args :_ *)(validateSources(_))