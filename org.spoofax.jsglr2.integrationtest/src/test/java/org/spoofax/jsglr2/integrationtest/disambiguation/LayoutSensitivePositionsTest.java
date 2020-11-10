package org.spoofax.jsglr2.integrationtest.disambiguation;

import java.util.stream.Stream;

import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.spoofax.jsglr2.integrationtest.BaseTestWithLayoutSensitiveSdf3ParseTables;
import org.spoofax.terms.ParseError;

public class LayoutSensitivePositionsTest extends BaseTestWithLayoutSensitiveSdf3ParseTables {

    public LayoutSensitivePositionsTest() {
        super("layout-sensitive-positions.sdf3");
    }

    @TestFactory public Stream<DynamicTest> literalFirstLine1Col1() throws ParseError {
        //@formatter:off
        return concat(
            testLayoutSensitiveSuccessByExpansions("l", "LiteralFirstLine1Col1()", "LiteralFirstLine1Col1"),
            testLayoutSensitiveParseFiltered(" l", "LiteralFirstLine1Col1"),
            testLayoutSensitiveParseFiltered("\nl", "LiteralFirstLine1Col1"),
            testLayoutSensitiveParseFiltered("\n\rl", "LiteralFirstLine1Col1"),
            testLayoutSensitiveParseFiltered(" \nl", "LiteralFirstLine1Col1"),
            testLayoutSensitiveParseFiltered(" \n\rl", "LiteralFirstLine1Col1"),
            testLayoutSensitiveParseFiltered("\n l", "LiteralFirstLine1Col1"),
            testLayoutSensitiveParseFiltered("\n\r l", "LiteralFirstLine1Col1")
        );
        //@formatter:on
    }

    @TestFactory public Stream<DynamicTest> literalFirstLine1Col2() throws ParseError {
        //@formatter:off
        return concat(
            testLayoutSensitiveParseFiltered("l", "LiteralFirstLine1Col2"),
            testLayoutSensitiveSuccessByExpansions(" l", "LiteralFirstLine1Col2()", "LiteralFirstLine1Col2"),
            testLayoutSensitiveParseFiltered("\nl", "LiteralFirstLine1Col2"),
            testLayoutSensitiveParseFiltered("\n\rl", "LiteralFirstLine1Col2"),
            testLayoutSensitiveParseFiltered(" \nl", "LiteralFirstLine1Col2"),
            testLayoutSensitiveParseFiltered(" \n\rl", "LiteralFirstLine1Col2"),
            testLayoutSensitiveParseFiltered("\n l", "LiteralFirstLine1Col2"),
            testLayoutSensitiveParseFiltered("\n\r l", "LiteralFirstLine1Col2")
        );
        //@formatter:on
    }

    @TestFactory public Stream<DynamicTest> literalFirstLine2Col1() throws ParseError {
        //@formatter:off
        return concat(
            testLayoutSensitiveParseFiltered("l", "LiteralFirstLine2Col1"),
            testLayoutSensitiveParseFiltered(" l", "LiteralFirstLine2Col1"),
            testLayoutSensitiveSuccessByExpansions("\nl", "LiteralFirstLine2Col1()", "LiteralFirstLine2Col1"),
            testLayoutSensitiveSuccessByExpansions("\n\rl", "LiteralFirstLine2Col1()", "LiteralFirstLine2Col1"),
            testLayoutSensitiveSuccessByExpansions(" \nl", "LiteralFirstLine2Col1()", "LiteralFirstLine2Col1"),
            testLayoutSensitiveSuccessByExpansions(" \n\rl", "LiteralFirstLine2Col1()", "LiteralFirstLine2Col1"),
            testLayoutSensitiveParseFiltered("\n l", "LiteralFirstLine2Col1"),
            testLayoutSensitiveParseFiltered("\n\r l", "LiteralFirstLine2Col1")
        );
        //@formatter:on
    }

    @TestFactory public Stream<DynamicTest> literalFirstLine2Col2() throws ParseError {
        //@formatter:off
        return concat(
            testLayoutSensitiveParseFiltered("l", "LiteralFirstLine2Col2"),
            testLayoutSensitiveParseFiltered(" l", "LiteralFirstLine2Col2"),
            testLayoutSensitiveParseFiltered("\nl", "LiteralFirstLine2Col2"),
            testLayoutSensitiveParseFiltered("\n\rl", "LiteralFirstLine2Col2"),
            testLayoutSensitiveParseFiltered(" \nl", "LiteralFirstLine2Col2"),
            testLayoutSensitiveParseFiltered(" \n\rl", "LiteralFirstLine2Col2"),
            testLayoutSensitiveSuccessByExpansions("\n l", "LiteralFirstLine2Col2()", "LiteralFirstLine2Col2"),
            testLayoutSensitiveSuccessByExpansions("\n\r l", "LiteralFirstLine2Col2()", "LiteralFirstLine2Col2")
        );
        //@formatter:on
    }

    @TestFactory public Stream<DynamicTest> lexicalFirstLine1Col1() throws ParseError {
        //@formatter:off
        return concat(
            testLayoutSensitiveSuccessByExpansions("x", "LexicalFirstLine1Col1(\"x\")", "LexicalFirstLine1Col1"),
            testLayoutSensitiveParseFiltered(" x", "LexicalFirstLine1Col1"),
            testLayoutSensitiveParseFiltered("\nx", "LexicalFirstLine1Col1"),
            testLayoutSensitiveParseFiltered("\n\rx", "LexicalFirstLine1Col1"),
            testLayoutSensitiveParseFiltered(" \nx", "LexicalFirstLine1Col1"),
            testLayoutSensitiveParseFiltered(" \n\rx", "LexicalFirstLine1Col1"),
            testLayoutSensitiveParseFiltered("\n x", "LexicalFirstLine1Col1"),
            testLayoutSensitiveParseFiltered("\n\r x", "LexicalFirstLine1Col1")
        );
        //@formatter:on
    }

    @TestFactory public Stream<DynamicTest> lexicalFirstLine1Col2() throws ParseError {
        //@formatter:off
        return concat(
            testLayoutSensitiveParseFiltered("x", "LexicalFirstLine1Col2"),
            testLayoutSensitiveSuccessByExpansions(" x", "LexicalFirstLine1Col2(\"x\")", "LexicalFirstLine1Col2"),
            testLayoutSensitiveParseFiltered("\nx", "LexicalFirstLine1Col2"),
            testLayoutSensitiveParseFiltered("\n\rx", "LexicalFirstLine1Col2"),
            testLayoutSensitiveParseFiltered(" \nx", "LexicalFirstLine1Col2"),
            testLayoutSensitiveParseFiltered(" \n\rx", "LexicalFirstLine1Col2"),
            testLayoutSensitiveParseFiltered("\n x", "LexicalFirstLine1Col2"),
            testLayoutSensitiveParseFiltered("\n\r x", "LexicalFirstLine1Col2")
        );
        //@formatter:on
    }

    @TestFactory public Stream<DynamicTest> lexicalFirstLine2Col1() throws ParseError {
        //@formatter:off
        return concat(
            testLayoutSensitiveParseFiltered("x", "LexicalFirstLine2Col1"),
            testLayoutSensitiveParseFiltered(" x", "LexicalFirstLine2Col1"),
            testLayoutSensitiveSuccessByExpansions("\nx", "LexicalFirstLine2Col1(\"x\")", "LexicalFirstLine2Col1"),
            testLayoutSensitiveSuccessByExpansions("\n\rx", "LexicalFirstLine2Col1(\"x\")", "LexicalFirstLine2Col1"),
            testLayoutSensitiveSuccessByExpansions(" \nx", "LexicalFirstLine2Col1(\"x\")", "LexicalFirstLine2Col1"),
            testLayoutSensitiveSuccessByExpansions(" \n\rx", "LexicalFirstLine2Col1(\"x\")", "LexicalFirstLine2Col1"),
            testLayoutSensitiveParseFiltered("\n x", "LexicalFirstLine2Col1"),
            testLayoutSensitiveParseFiltered("\n\r x", "LexicalFirstLine2Col1")
        );
        //@formatter:on
    }

    @TestFactory public Stream<DynamicTest> lexicalFirstLine2Col2() throws ParseError {
        //@formatter:off
        return concat(
            testLayoutSensitiveParseFiltered("x", "LexicalFirstLine2Col2"),
            testLayoutSensitiveParseFiltered(" x", "LexicalFirstLine2Col2"),
            testLayoutSensitiveParseFiltered("\nx", "LexicalFirstLine2Col2"),
            testLayoutSensitiveParseFiltered("\n\rx", "LexicalFirstLine2Col2"),
            testLayoutSensitiveParseFiltered(" \nx", "LexicalFirstLine2Col2"),
            testLayoutSensitiveParseFiltered(" \n\rx", "LexicalFirstLine2Col2"),
            testLayoutSensitiveSuccessByExpansions("\n x", "LexicalFirstLine2Col2(\"x\")", "LexicalFirstLine2Col2"),
            testLayoutSensitiveSuccessByExpansions("\n\r x", "LexicalFirstLine2Col2(\"x\")", "LexicalFirstLine2Col2")
        );
        //@formatter:on
    }

    @TestFactory public Stream<DynamicTest> leftCol1() throws ParseError {
        //@formatter:off
        return concat(
            testLayoutSensitiveSuccessByExpansions("p p", "LeftCol1([P(), P()])", "LeftCol1"),
            testLayoutSensitiveSuccessByExpansions("p\np", "LeftCol1([P(), P()])", "LeftCol1"),
            testLayoutSensitiveParseFiltered("p\n p", "LeftCol1"),
            testLayoutSensitiveSuccessByExpansions("p\np\n p", "LeftCol1([P(), P(), P()])", "LeftCol1"),
            testLayoutSensitiveSuccessByExpansions("p\n p\np", "LeftCol1([P(), P(), P()])", "LeftCol1")
        );
        //@formatter:on
    }

    @TestFactory public Stream<DynamicTest> leftCol2() throws ParseError {
        //@formatter:off
        return concat(
            testLayoutSensitiveSuccessByExpansions("q q", "LeftCol2([Q(), Q()])", "LeftCol2"),
            testLayoutSensitiveParseFiltered("q\nq", "LeftCol2"),
            testLayoutSensitiveSuccessByExpansions("q\n q", "LeftCol2([Q(), Q()])", "LeftCol2"),
            testLayoutSensitiveSuccessByExpansions("q\n  q\n q", "LeftCol2([Q(), Q(), Q()])", "LeftCol2"),
            testLayoutSensitiveSuccessByExpansions("q\n q\n  q", "LeftCol2([Q(), Q(), Q()])", "LeftCol2")
        );
        //@formatter:on
    }

    @TestFactory public Stream<DynamicTest> rightCol1() throws ParseError {
        //@formatter:off
        return concat(
            testLayoutSensitiveSuccessByExpansions("r r", "RightCol1([R(), R()])", "RightCol1"),
            testLayoutSensitiveSuccessByExpansions("r\nr", "RightCol1([R(), R()])", "RightCol1"),
            testLayoutSensitiveParseFiltered(" r\nr", "RightCol1"),
            testLayoutSensitiveSuccessByExpansions("r\nr\nr", "RightCol1([R(), R(), R()])", "RightCol1"),
            testLayoutSensitiveParseFiltered("r\n r\nr", "RightCol1")
        );
        //@formatter:on
    }

    @TestFactory public Stream<DynamicTest> rightCol2() throws ParseError {
        //@formatter:off
        return concat(
            testLayoutSensitiveSuccessByExpansions("s s", "RightCol2([S(), S()])", "RightCol2"),
            testLayoutSensitiveParseFiltered("s\ns", "RightCol2"),
            testLayoutSensitiveSuccessByExpansions(" s\ns", "RightCol2([S(), S()])", "RightCol2"),
            testLayoutSensitiveSuccessByExpansions(" s\ns\ns", "RightCol2([S(), S(), S()])", "RightCol2"),
            testLayoutSensitiveParseFiltered("s\n  s\ns", "RightCol2")
        );
        //@formatter:on
    }

    @TestFactory public Stream<DynamicTest> leadingEmptyNonLayout() throws ParseError {
        //@formatter:off
        return concat(
            testLayoutSensitiveSuccessByExpansions("foo\n bar baz", "LeadingEmptyNonLayout(BarBaz(Some(Bar()), Baz()))", "LeadingEmptyNonLayout"),
            testLayoutSensitiveParseFiltered("foo bar\nbaz", "LeadingEmptyNonLayout"),
            testLayoutSensitiveParseFiltered("foo bar\n  baz", "LeadingEmptyNonLayout"),
            testLayoutSensitiveSuccessByExpansions("foo\n baz", "LeadingEmptyNonLayout(BarBaz(None(), Baz()))", "LeadingEmptyNonLayout"),
            testLayoutSensitiveParseFiltered("foo\nbaz", "LeadingEmptyNonLayout"),
            testLayoutSensitiveParseFiltered("foo\n  baz", "LeadingEmptyNonLayout")
        );
        //@formatter:on
    }

}
