import $ivy.`com.lihaoyi::ammonite-ops:1.8.1`, ammonite.ops._

import $file.common, common._, Suite._
import $file.parsers, parsers._
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

            val parseFailures: Seq[(String, Option[String])] = parsers.flatMap { parser =>
                try {
                    Await.result(Future.successful(
                        parser.parse(input) match {
                            case ParseFailure(error) => Some((parser.id, error))
                            case _ => None
                        }
                    ), 10 seconds)
                } catch {
                    case _: TimeoutException => Some((parser.id, Some("timeout")))
                }
            }

            // TODO: verify that JSGLR2 variants produce the same AST

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
