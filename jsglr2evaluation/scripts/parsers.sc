import $ivy.`com.lihaoyi::ammonite-ops:1.8.1`, ammonite.ops._

import $file.common, common._, Suite._
import $file.spoofax, spoofax._
import org.spoofax.interpreter.terms.IStrategoTerm
import org.spoofax.jsglr2.{JSGLR2Variant, JSGLR2Success, JSGLR2Failure}
import org.spoofax.jsglr2.integration.IntegrationVariant
import org.metaborg.parsetable.ParseTableVariant
import scala.util.{Failure, Success, Try}

trait Parser {
    def id: String
    def parse(input: String): ParseResult
}

case class JSGLR2Parser(language: Language, jsglr2Preset: JSGLR2Variant.Preset) extends Parser {
    val id = "jsglr2-" + jsglr2Preset.name
    val variant = new IntegrationVariant(
        new ParseTableVariant(),
        jsglr2Preset.variant
    )
    val jsglr2 = getJSGLR2(variant, language)
    def parse(input: String) = jsglr2.parseResult(input, null, null) match {
        case success: JSGLR2Success[IStrategoTerm] =>
            if (success.isAmbiguous)
                ParseFailure(Some("ambiguous"))
            else
                ParseSuccess(Some(success.ast))
        case failure: JSGLR2Failure[_] => ParseFailure(Some(failure.parseFailure.failureCause.toMessage.toString))
    }
}

case class JSGLR1Parser(language: Language) extends Parser {
    val id = "jsglr1"
    val jsglr1 = getJSGLR1(language)
    def parse(input: String) = Try(jsglr1.parse(input, null, null)) match {
        case Success(_) => ParseSuccess(None)
        case Failure(_) => ParseFailure(None)
    }
}

import $ivy.`org.metaborg:org.spoofax.jsglr2.benchmark:2.6.0-SNAPSHOT`
import $ivy.`org.antlr:antlr4-runtime:4.7.2`

import org.antlr.v4.runtime.{Lexer => ANTLR_Lexer, Parser => ANTLR_Parser, _}
import org.antlr.v4.runtime.tree.Tree
import org.antlr.v4.runtime.misc.ParseCancellationException
import org.spoofax.jsglr2.benchmark.antlr4.{Java8Lexer => ANTLR_Java8Lexer, Java8Parser => ANTLR_Java8Parser}
import org.spoofax.jsglr2.benchmark.antlr4.{JavaLexer => ANTLR_JavaLexer, JavaParser => ANTLR_JavaParser}

case class ANTLRParser[ANTLR__Lexer <: ANTLR_Lexer, ANTLR__Parser <: ANTLR_Parser](id: String, getLexer: CharStream => ANTLR__Lexer, getParser: TokenStream => ANTLR__Parser, doParse: ANTLR__Parser => Tree) extends Parser {
    def parse(input: String) = {
        try {
            val charStream = CharStreams.fromString(input)
            val lexer = getLexer(charStream)

            val tokens = new CommonTokenStream(lexer)
            val parser = getParser(tokens)

            parser.setErrorHandler(new BailErrorStrategy())
            
            doParse(parser)

            ParseSuccess(None)
        } catch {
            case e: ParseCancellationException => ParseFailure(None)
        }
    }
}

trait ParseResult {
    def isValid: Boolean
    def isInvalid = !isValid
}
case class ParseSuccess(ast: Option[IStrategoTerm]) extends ParseResult {
    def isValid = true
}
case class ParseFailure(error: Option[String]) extends ParseResult {
    def isValid = false
}

object Parser {
    def variants(language: Language)(implicit suite: Suite): Seq[Parser] = Seq(
        //JSGLR1Parser(language),
        JSGLR2Parser(language, JSGLR2Variant.Preset.standard),
        JSGLR2Parser(language, JSGLR2Variant.Preset.incremental)
        /*JSGLR2Parser(language.parseTablePath, JSGLR2Variant.Preset.recovery),
        JSGLR2Parser(language.parseTablePath, JSGLR2Variant.Preset.recoveryIncremental)*/
    ) ++ language.antlrBenchmarks.map { benchmark =>
        benchmark.id match {
            case "antlr" =>
                ANTLRParser[ANTLR_Java8Lexer, ANTLR_Java8Parser](benchmark.id, new ANTLR_Java8Lexer(_), new ANTLR_Java8Parser(_), _.compilationUnit)
            case "antlr-optimized" =>
                ANTLRParser[ANTLR_JavaLexer, ANTLR_JavaParser](benchmark.id, new ANTLR_JavaLexer(_), new ANTLR_JavaParser(_), _.compilationUnit)
        }
    }
}