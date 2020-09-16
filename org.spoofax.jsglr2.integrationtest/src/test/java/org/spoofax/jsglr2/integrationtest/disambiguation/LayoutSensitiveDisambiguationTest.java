package org.spoofax.jsglr2.integrationtest.disambiguation;

import java.util.stream.Stream;

import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.spoofax.jsglr2.integrationtest.BaseTestWithLayoutSensitiveSdf3ParseTables;
import org.spoofax.terms.ParseError;

public class LayoutSensitiveDisambiguationTest extends BaseTestWithLayoutSensitiveSdf3ParseTables {

    public LayoutSensitiveDisambiguationTest() {
        super("layout-sensitive.sdf3");
    }

    @TestFactory public Stream<DynamicTest> alignmentMisaligned1() throws ParseError {
        //@formatter:off
        return testLayoutSensitiveParseFiltered(
            "doAlignList s1 \n" +
            "       s2"
        );
        //@formatter:on
    }

    @TestFactory public Stream<DynamicTest> alignmentMisaligned2() throws ParseError {
        //@formatter:off
        return testLayoutSensitiveParseFiltered(
            "doAlignList s1 \n" +
            "             s2"
        );
        //@formatter:on
    }

    @TestFactory public Stream<DynamicTest> alignmentAligned() throws ParseError {
        //@formatter:off
        return testLayoutSensitiveSuccessByExpansions(
            "doAlignList s1 \n" +
            "            s2",
            "DoAlignList([\"s1\", \"s2\"])"
        );
        //@formatter:on
    }

    @TestFactory public Stream<DynamicTest> explicitAlignmentMisaligned1() throws ParseError {
        //@formatter:off
        return testLayoutSensitiveParseFiltered(
            "doAlign s1 \n" +
            "       s2");
        //@formatter:on
    }

    @TestFactory public Stream<DynamicTest> explicitAlignmentMisaligned2() throws ParseError {
        //@formatter:off
        return testLayoutSensitiveParseFiltered(
            "doAlign s1 \n" +
            "         s2"
        );
        //@formatter:on
    }

    @TestFactory public Stream<DynamicTest> explicitAlignmentAligned() throws ParseError {
        //@formatter:off
        return testLayoutSensitiveSuccessByExpansions(
            "doAlign s1 \n" +
            "        s2",
            "DoAlign(StmtSeq(Stmt(\"s1\"), \"s2\"))"
        );
        //@formatter:on
    }

    @TestFactory public Stream<DynamicTest> offsideExpression1() throws ParseError {
        //@formatter:off
        return testLayoutSensitiveSuccessByExpansions(
            "do e1 +\n" +
            "    e2",
            "Do(OffsideExp(Add(\"e1\", \"e2\")))"
        );
        //@formatter:on
    }

    @TestFactory public Stream<DynamicTest> offsideExpression2() throws ParseError {
        //@formatter:off
        return testLayoutSensitiveSuccessByExpansions(
            "do e1 +\n" +
            "   e2",
            "Add(Do(OffsideExp(\"e1\")), \"e2\")"
        );
        //@formatter:on
    }

    @TestFactory public Stream<DynamicTest> offsideExpression3() throws ParseError {
        //@formatter:off
        return testLayoutSensitiveSuccessByExpansions(
            "do e1 +\n" +
            "  e2",
            "Add(Do(OffsideExp(\"e1\")), \"e2\")"
        );
        //@formatter:on
    }

    @TestFactory public Stream<DynamicTest> offsideMultipleElements1() throws ParseError {
        //@formatter:off
        return testLayoutSensitiveSuccessByExpansions(
            "doOffside e1\n" +
            "+ e2",
            "Add(DoOffside(Exp(\"e1\")), \"e2\")"
        );
        //@formatter:on
    }

    @TestFactory public Stream<DynamicTest> offsideMultipleElements2() throws ParseError {
        //@formatter:off
        return testLayoutSensitiveSuccessByExpansions(
            "doOffside e1\n" +
            " + e2",
            "DoOffside(Exp(Add(\"e1\", \"e2\")))"
        );
        //@formatter:on
    }

    @TestFactory public Stream<DynamicTest> indentExpression1() throws ParseError {
        //@formatter:off
        return testLayoutSensitiveSuccessByExpansions(
            "doIndent s1",
            "DoIndent(\"s1\")"
        );
        //@formatter:on
    }

    @TestFactory public Stream<DynamicTest> indentExpression2() throws ParseError {
        //@formatter:off
        return testLayoutSensitiveSuccessByExpansions(
            "doIndent \n" +
            " s1",
            "DoIndent(\"s1\")"
        );
        //@formatter:on
    }

    @TestFactory public Stream<DynamicTest> indentExpressionFailed() throws ParseError {
        //@formatter:off
        return testLayoutSensitiveParseFiltered(
            "doIndent \n" +
            "s1"
        );
        //@formatter:on
    }

    @TestFactory public Stream<DynamicTest> newlineIndentExpressionNoNewline() throws ParseError {
        //@formatter:off
        return testLayoutSensitiveParseFiltered(
            "doNLIndent s1"
        );
        //@formatter:on
    }

    @TestFactory public Stream<DynamicTest> newlineIndentExpressionNoIndent() throws ParseError {
        //@formatter:off
        return testLayoutSensitiveParseFiltered(
            "doNLIndent \n" +
            "s1"
        );
        //@formatter:on
    }

    @TestFactory public Stream<DynamicTest> newlineIndentExpression() throws ParseError {
        //@formatter:off
        return testLayoutSensitiveSuccessByExpansions(
            "doNLIndent \n" +
            " s1",
            "DoNLIndent(\"s1\"))"
        );
        //@formatter:on
    }

    @TestFactory public Stream<DynamicTest> singleLine() throws ParseError {
        //@formatter:off
        return testLayoutSensitiveSuccessByExpansions(
            "foo bar",
            "SingleLine()"
        );
        //@formatter:on
    }

    @TestFactory public Stream<DynamicTest> singleLineMultiline() throws ParseError {
        //@formatter:off
        return testLayoutSensitiveParseFiltered(
            "foo\nbar"
        );
        //@formatter:on
    }

    @TestFactory public Stream<DynamicTest> singleLinePrePostUnix() throws ParseError {
        //@formatter:off
        return testLayoutSensitiveSuccessByExpansions(
            "\n\nfoo bar\n\n",
            "SingleLine()"
        );
        //@formatter:on
    }

    @TestFactory public Stream<DynamicTest> singleLinePrePostWindows() throws ParseError {
        //@formatter:off
        return testLayoutSensitiveSuccessByExpansions(
            "\n\r\n\rfoo bar\n\r\n\r",
            "SingleLine()"
        );
        //@formatter:on
    }

    @TestFactory public Stream<DynamicTest> singleLineMultilinePrePostUnix() throws ParseError {
        //@formatter:off
        return testLayoutSensitiveParseFiltered(
            "\n\nfoo\nbar\n\n"
        );
        //@formatter:on
    }

    @TestFactory public Stream<DynamicTest> singleLineMultilinePrePostWindows() throws ParseError {
        //@formatter:off
        return testLayoutSensitiveParseFiltered(
            "\n\r\n\rfoo\n\rbar\n\r\n\r"
        );
        //@formatter:on
    }

}
