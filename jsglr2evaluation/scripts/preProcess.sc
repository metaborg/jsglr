import $ivy.`com.lihaoyi::ammonite-ops:1.8.1`, ammonite.ops._

import $file.common, common._, Suite._
import $file.parsers, parsers._
import org.spoofax.interpreter.terms.IStrategoTerm
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent._
import scala.concurrent.duration._

println("Validate sources...")

suite.languages.foreach { language =>
    println(" " + language.name)

    val parsers = Parser.variants(language)

    timed("validate " + language.id) {
        language.sourceFilesBatch().foreach { file =>
            val input = read! file
            val filename = file relativeTo language.sourcesDir

            val results: Seq[(String, ParseResult)] = parsers.map { parser =>
                try {
                    Await.result(Future.successful {
                        (parser.id, parser.parse(input))
                    }, 10 seconds)
                } catch {
                    case _: TimeoutException => (parser.id, ParseFailure(Some("timeout")))
                }
            }

            val failures: Seq[(String, Option[String])] = results.flatMap {
                case (parser, ParseFailure(error)) => Some((parser, error))
                case _ => None
            }

            val successASTs: Seq[IStrategoTerm] = results.flatMap {
                case (parser, ParseSuccess(ast)) => ast
                case _ => None
            }

            def consistentASTs(asts: Seq[IStrategoTerm]) = true // TODO: check consistency

            val valid =
                if (failures.nonEmpty) {
                    println("   Invalid: " + filename)
                    failures.foreach { case (parser, error) =>
                        println("     " + parser + error.fold("")(" (" + _ + ")"))
                    }

                    false
                } else if (!consistentASTs(successASTs)) {
                    println("   Inconsistent: " + filename)

                    false
                } else
                    true

            if (!valid) {
                mkdir! sourcesDir / "invalid"
                mv.over(file, sourcesDir / "invalid" / filename.last)
            }
        }
    }
}
