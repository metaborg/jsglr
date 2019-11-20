import $ivy.`com.lihaoyi::ammonite-ops:1.8.1`, ammonite.ops._

import $file.common, common._, Args._

import $file.spoofax, spoofax._
import org.spoofax.jsglr2.JSGLR2Variant
import org.spoofax.jsglr2.integration.IntegrationVariant
import org.spoofax.jsglr2.integration.ParseTableVariant

def preprocess(implicit args: Args) = {
    println("Preprocessing sources...")

    config.languages.foreach { language =>
        println(" " + language.name)

        val files = ls.rec! language.sourcesDir

        val variant = new IntegrationVariant(
            new ParseTableVariant(),
            JSGLR2Variant.Preset.standard.variant
        )

        val jsglr2 = getJSGLR2(variant, language.parseTablePath)

        timed("validate " + language.id) {
            files.filterNot(_.last.toString.startsWith(".")).foreach { file =>
                val ast = jsglr2.parse(read! file)

                if (ast == null) {
                    val filename = file relativeTo language.sourcesDir

                    println("   Invalid: " + filename)

                    mkdir! sourcesDir / "invalid"
                    mv.over(file, sourcesDir / "invalid" / filename)
                }

                // TODO: if language has ANTLR grammar, also validate with ANTLR
            }
        }
    }

    // TODO: sample files (based on file size) for per file benchmarking
}

@main
def ini(args: String*) = withArgs(args :_ *)(preprocess(_))