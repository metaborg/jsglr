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
            "alignList s1 \n" +
            "       s2",
            "AlignList"
        );
        //@formatter:on
    }

    @TestFactory public Stream<DynamicTest> alignmentMisaligned2() throws ParseError {
        //@formatter:off
        return testLayoutSensitiveParseFiltered(
            "alignList s1 \n" +
            "             s2",
            "AlignList"
        );
        //@formatter:on
    }

    @TestFactory public Stream<DynamicTest> alignmentAligned() throws ParseError {
        //@formatter:off
        return testLayoutSensitiveSuccessByExpansions(
            "alignList s1 \n" +
            "          s2",
            "AlignList([\"s1\", \"s2\"])",
            "AlignList"
        );
        //@formatter:on
    }


    @TestFactory public Stream<DynamicTest> explicitAlignmentMisaligned1() throws ParseError {
        //@formatter:off
        return testLayoutSensitiveParseFiltered(
            "align s1 \n" +
            "     s2",
            "Align"
        );
        //@formatter:on
    }

    @TestFactory public Stream<DynamicTest> explicitAlignmentMisaligned2() throws ParseError {
        //@formatter:off
        return testLayoutSensitiveParseFiltered(
            "align s1 \n" +
            "       s2",
            "Align"
        );
        //@formatter:on
    }

    @TestFactory public Stream<DynamicTest> explicitAlignmentAligned() throws ParseError {
        //@formatter:off
        return testLayoutSensitiveSuccessByExpansions(
            "align s1 \n" +
            "      s2",
            "Align(StmtSeq(Stmt(\"s1\"), \"s2\"))",
            "Align"
        );
        //@formatter:on
    }


    @TestFactory public Stream<DynamicTest> offsideExpression1() throws ParseError {
        //@formatter:off
        return testLayoutSensitiveSuccessByExpansions(
            "offside e1 +\n" +
            "         e2",
            "Offside(Add(\"e1\", \"e2\"))",
            "Offside"
        );
        //@formatter:on
    }

    @TestFactory public Stream<DynamicTest> offsideExpression2() throws ParseError {
        //@formatter:off
        return testLayoutSensitiveParseFiltered(
            "offside e1 +\n" +
            "        e2",
            "Offside"
        );
        //@formatter:on
    }

    @TestFactory public Stream<DynamicTest> offsideExpression3() throws ParseError {
        //@formatter:off
        return testLayoutSensitiveParseFiltered(
            "offside e1 +\n" +
            "       e2",
            "Offside"
        );
        //@formatter:on
    }


    @TestFactory public Stream<DynamicTest> offsideMultipleElements1() throws ParseError {
        //@formatter:off
        return testLayoutSensitiveSuccessByExpansions(
            "offside2 e1 + e2",
            "Offside2(Add(\"e1\", \"e2\"))",
            "Offside2"
        );
        //@formatter:on
    }

    @TestFactory public Stream<DynamicTest> offsideMultipleElements2() throws ParseError {
        //@formatter:off
        return testLayoutSensitiveParseFiltered(
            "offside2 e1\n" +
            "+ e2",
            "Offside2"
        );
        //@formatter:on
    }

    @TestFactory public Stream<DynamicTest> offsideMultipleElements3() throws ParseError {
        //@formatter:off
        return testLayoutSensitiveSuccessByExpansions(
            "offside2 e1\n" +
            " + e2",
            "Offside2(Add(\"e1\", \"e2\"))",
            "Offside2"
        );
        //@formatter:on
    }


    @TestFactory public Stream<DynamicTest> indentExpression1() throws ParseError {
        //@formatter:off
        return testLayoutSensitiveSuccessByExpansions(
            "indent e1",
            "Indent(\"e1\")",
            "Indent"
        );
        //@formatter:on
    }

    @TestFactory public Stream<DynamicTest> indentExpression2() throws ParseError {
        //@formatter:off
        return testLayoutSensitiveSuccessByExpansions(
            "indent \n" +
            " e1",
            "Indent(\"e1\")",
            "Indent"
        );
        //@formatter:on
    }

    @TestFactory public Stream<DynamicTest> indentExpressionFailed() throws ParseError {
        //@formatter:off
        return testLayoutSensitiveParseFiltered(
            "indent \n" +
            "e1",
            "Indent"
        );
        //@formatter:on
    }

    @TestFactory public Stream<DynamicTest> newlineIndentExpressionNoNewline() throws ParseError {
        //@formatter:off
        return testLayoutSensitiveParseFiltered(
            "NLindent e1",
            "NLIndent"
        );
        //@formatter:on
    }

    @TestFactory public Stream<DynamicTest> newlineIndentExpressionNoIndent() throws ParseError {
        //@formatter:off
        return testLayoutSensitiveParseFiltered(
            "NLindent \n" +
            "e1",
            "NLIndent"
        );
        //@formatter:on
    }

    @TestFactory public Stream<DynamicTest> newlineIndentExpression() throws ParseError {
        //@formatter:off
        return testLayoutSensitiveSuccessByExpansions(
            "NLindent \n" +
            " e1",
            "NLIndent(\"e1\"))",
            "NLIndent"
        );
        //@formatter:on
    }


    @TestFactory public Stream<DynamicTest> singleLine() throws ParseError {
        //@formatter:off
        return testLayoutSensitiveSuccessByExpansions(
            "foo bar",
            "SingleLine()",
            "SingleLine"
        );
        //@formatter:on
    }

    @TestFactory public Stream<DynamicTest> singleLineMultiline() throws ParseError {
        //@formatter:off
        return testLayoutSensitiveParseFiltered(
            "foo\nbar",
            "SingleLine"
        );
        //@formatter:on
    }

    @TestFactory public Stream<DynamicTest> singleLinePrePostUnix() throws ParseError {
        //@formatter:off
        return testLayoutSensitiveSuccessByExpansions(
            "\n\nfoo bar\n\n",
            "SingleLine()",
            "SingleLine"
        );
        //@formatter:on
    }

    @TestFactory public Stream<DynamicTest> singleLinePrePostWindows() throws ParseError {
        //@formatter:off
        return testLayoutSensitiveSuccessByExpansions(
            "\n\r\n\rfoo bar\n\r\n\r",
            "SingleLine()",
            "SingleLine"
        );
        //@formatter:on
    }

    @TestFactory public Stream<DynamicTest> singleLineMultilinePrePostUnix() throws ParseError {
        //@formatter:off
        return testLayoutSensitiveParseFiltered(
            "\n\nfoo\nbar\n\n",
            "SingleLine"
        );
        //@formatter:on
    }

    @TestFactory public Stream<DynamicTest> singleLineMultilinePrePostWindows() throws ParseError {
        //@formatter:off
        return testLayoutSensitiveParseFiltered(
            "\n\r\n\rfoo\n\rbar\n\r\n\r",
            "SingleLine"
        );
        //@formatter:on
    }

}
