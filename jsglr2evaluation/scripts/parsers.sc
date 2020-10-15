import $ivy.`com.lihaoyi::ammonite-ops:1.8.1`, ammonite.ops._

import $file.common, common._, Args._
import $file.spoofax, spoofax._
import org.spoofax.jsglr2.{JSGLR2Variant, JSGLR2Success, JSGLR2Failure}
import org.spoofax.jsglr2.integration.IntegrationVariant
import org.metaborg.parsetable.ParseTableVariant
import scala.util.{Failure, Success, Try}

trait Parser {
    def id: String
    def parse(input: String): ParseResult
}

case class JSGLR2Parser(parseTablePath: Path, jsglr2Preset: JSGLR2Variant.Preset) extends Parser {
    val id = "jsglr2-" + jsglr2Preset.name
    val variant = new IntegrationVariant(
        new ParseTableVariant(),
        jsglr2Preset.variant
    )
    val jsglr2 = getJSGLR2(variant, parseTablePath)
    def parse(input: String) = jsglr2.parseResult(input, null, null) match {
        case _: JSGLR2Success[_] => ParseSuccess
        case failure: JSGLR2Failure[_] => ParseFailure(Some(failure.parseFailure.failureCause.toMessage.toString))
    }
}

case class JSGLR1Parser(parseTablePath: Path) extends Parser {
    val id = "jsglr1"
    val jsglr1 = getJSGLR1(parseTablePath)
    def parse(input: String) = Try(jsglr1.parse(input, null, null)) match {
        case Success(_) => ParseSuccess
        case Failure(_) => ParseFailure(None)
    }
}

trait ParseResult {
    def isValid: Boolean
    def isInvalid = !isValid
}
object ParseSuccess extends ParseResult {
    def isValid = true
}
case class ParseFailure(error: Option[String]) extends ParseResult {
    def isValid = false
}

object Parser {
    def variants(language: Language)(implicit args: Args) = Seq(
        JSGLR1Parser(language.parseTablePath),
        JSGLR2Parser(language.parseTablePath, JSGLR2Variant.Preset.standard)
    )
}