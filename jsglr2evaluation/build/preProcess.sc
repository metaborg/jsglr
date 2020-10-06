import $ivy.`com.lihaoyi::ammonite-ops:1.8.1`, ammonite.ops._

import $file.common, common._, Args._

import $file.spoofax, spoofax._
import org.spoofax.jsglr2.JSGLR2Variant
import org.spoofax.jsglr2.integration.IntegrationVariant
import org.metaborg.parsetable.ParseTableVariant

def preProcess(implicit args: Args) = {
    println("Validate sources...")

    config.languages.foreach { language =>
        println(" " + language.name)

        val variant = new IntegrationVariant(
            new ParseTableVariant(),
            JSGLR2Variant.Preset.standard.variant
        )

        val jsglr2 = getJSGLR2(variant, language.parseTablePath)
        val jsglr1 = getJSGLR1(language.parseTablePath)

        timed("validate " + language.id) {
            language.sourceFilesBatch.foreach { file =>
                val input = read! file

                val jsglr2AST = jsglr2.parse(input)
                val jsglr1AST = jsglr1.parse(input, null, null)

                if (jsglr2AST == null || jsglr1AST == null) {
                    val filename = file relativeTo language.sourcesDir

                    println("   Invalid: " + filename)

                    mkdir! sourcesDir / "invalid"
                    mv.over(file, sourcesDir / "invalid" / filename)
                }

                // TODO: if language has ANTLR grammar, also validate with ANTLR
            }
        }
    }
}

@main
def ini(args: String*) = withArgs(args :_ *)(preProcess(_))
