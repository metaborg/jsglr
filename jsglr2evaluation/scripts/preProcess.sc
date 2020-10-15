import $ivy.`com.lihaoyi::ammonite-ops:1.8.1`, ammonite.ops._

import $file.common, common._, Args._
import $file.parsers, parsers._

def preProcess(implicit args: Args) = {
    println("Validate sources...")

    args.languages.foreach { language =>
        println(" " + language.name)
        
        // TODO: validate with all variants
        // TODO: if language has ANTLR grammar, also validate with ANTLR

        val parsers = Parser.variants(language)

        timed("validate " + language.id) {
            language.sourceFilesBatch.foreach { file =>
                val input = read! file

                val parseFailures: Seq[(String, Option[String])] = parsers.flatMap { parser =>
                    parser.parse(input) match {
                        case ParseFailure(error) => Some((parser.id, error))
                        case _ => None
                    }
                }

                if (parseFailures.nonEmpty) {
                    val filename = file relativeTo language.sourcesDir

                    println("   Invalid: " + filename)
                    parseFailures.foreach { case (parser, error) =>
                        println("     " + parser + error.fold("")(" (" + _ + ")"))
                    }

                    mkdir! sourcesDir / "invalid"
                    mv.over(file, sourcesDir / "invalid" / filename.last)
                }
            }
        }
    }
}

@main
def ini(args: String*) = withArgs(args :_ *)(preProcess(_))
