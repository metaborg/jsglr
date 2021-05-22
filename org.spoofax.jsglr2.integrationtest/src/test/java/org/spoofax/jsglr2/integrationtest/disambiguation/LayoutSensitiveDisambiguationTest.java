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

    @TestFactory public Stream<DynamicTest> alignListSep1() throws ParseError {
        //@formatter:off
        return testLayoutSensitiveSuccessByExpansions(
            "alignListSep s1,\n" +
            "             s2",
            "AlignListSep([\"s1\", \"s2\"])",
            "AlignListSep"
        );
        //@formatter:on
    }

    @TestFactory public Stream<DynamicTest> alignListSep2() throws ParseError {
        //@formatter:off
        return testLayoutSensitiveSuccessByExpansions(
            "alignListSep s1,\n" +
            "             s2,\n" +
            "             s3\n",
            "AlignListSep([\"s1\", \"s2\", \"s3\"])",
            "AlignListSep"
        );
        //@formatter:on
    }

    @TestFactory public Stream<DynamicTest> alignListSep3() throws ParseError {
        //@formatter:off
        return testLayoutSensitiveSuccessByExpansions(
            "alignListSep\n" +
            "s1\n" +
            ",\n" +
            "s2",
            "AlignListSep([\"s1\", \"s2\"])",
            "AlignListSep"
        );
        //@formatter:on
    }

    @TestFactory public Stream<DynamicTest> alignListSep4() throws ParseError {
        //@formatter:off
        return testLayoutSensitiveSuccessByExpansions(
            "alignListSep s1\n" +
            ",            s2\n" +
            ",            s3\n",
            "AlignListSep([\"s1\", \"s2\", \"s3\"])",
            "AlignListSep"
        );
        //@formatter:on
    }

    @TestFactory public Stream<DynamicTest> alignListSepFail1() throws ParseError {
        //@formatter:off
        return testLayoutSensitiveParseFiltered(
            "alignListSep s1,s2",
            "AlignListSep"
        );
        //@formatter:on
    }

    @TestFactory public Stream<DynamicTest> alignListSepFail2() throws ParseError {
        //@formatter:off
        return testLayoutSensitiveParseFiltered(
            "alignListSep\n" +
            "s1\n" +
            ",s2",
            "AlignListSep"
        );
        //@formatter:on
    }

    @TestFactory public Stream<DynamicTest> alignListSepFail3() throws ParseError {
        //@formatter:off
        return testLayoutSensitiveParseFiltered(
            "alignListSep\n" +
            "  s1\n" +
            ",s2",
            "AlignListSep"
        );
        //@formatter:on
    }

    @TestFactory public Stream<DynamicTest> alignListSepFail4() throws ParseError {
        //@formatter:off
        return testLayoutSensitiveParseFiltered(
            "alignListSep\n" +
            "  s1\n" +
            ",s2",
            "AlignListSep"
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

    @TestFactory public Stream<DynamicTest> explicitAlignment3Aligned1() throws ParseError {
        //@formatter:off
        return testLayoutSensitiveSuccessByExpansions(
            "align3 s1 \n" +
            "       s2\n" +
            "       s3",
            "Align3(\"s1\", \"s2\", \"s3\")",
            "Align3"
        );
        //@formatter:on
    }

    @TestFactory public Stream<DynamicTest> explicitAlignment3Aligned2() throws ParseError {
        //@formatter:off
        return testLayoutSensitiveSuccessByExpansions(
            "align3\n" +
            "s1\n" +
            "s2\n" +
            "s3",
            "Align3(\"s1\", \"s2\", \"s3\")",
            "Align3"
        );
        //@formatter:on
    }

    @TestFactory public Stream<DynamicTest> explicitAlignment3Aligned3() throws ParseError {
        //@formatter:off
        return testLayoutSensitiveSuccessByExpansions(
            "align3 s1\n\n" +
            "       s2\n\n\n" +
            "       s3\n",
            "Align3(\"s1\", \"s2\", \"s3\")",
            "Align3"
        );
        //@formatter:on
    }

    @TestFactory public Stream<DynamicTest> explicitAlignment3Misaligned1() throws ParseError {
        //@formatter:off
        return testLayoutSensitiveParseFiltered(
            "align3 s1 \n" +
            "      s2\n" +
            "       s3",
            "Align3"
        );
        //@formatter:on
    }

    @TestFactory public Stream<DynamicTest> explicitAlignment3Misaligned2() throws ParseError {
        //@formatter:off
        return testLayoutSensitiveParseFiltered(
            "align3 s1 s2\n" +
            "       s3",
            "Align3"
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

    @TestFactory public Stream<DynamicTest> indentMultipleExpressions1() throws ParseError {
        //@formatter:off
        return testLayoutSensitiveSuccessByExpansions(
            "indent2 e1 e2",
            "Indent2(\"e1\", \"e2\")",
            "Indent2"
        );
        //@formatter:on
    }

    @TestFactory public Stream<DynamicTest> indentMultipleExpressions2() throws ParseError {
        //@formatter:off
        return testLayoutSensitiveSuccessByExpansions(
            "indent2 e1\n" +
            " e2",
            "Indent2(\"e1\", \"e2\")",
            "Indent2"
        );
        //@formatter:on
    }

    @TestFactory public Stream<DynamicTest> indentMultipleExpressions3() throws ParseError {
        //@formatter:off
        return testLayoutSensitiveSuccessByExpansions(
            "indent2\n" +
            "  e1\n" +
            " e2",
            "Indent2(\"e1\", \"e2\")",
            "Indent2"
        );
        //@formatter:on
    }

    @TestFactory public Stream<DynamicTest> indentMultipleExpressions4() throws ParseError {
        //@formatter:off
        return testLayoutSensitiveSuccessByExpansions(
            "indent2\n" +
            " e1\n\n" +
            "  e2",
            "Indent2(\"e1\", \"e2\")",
            "Indent2"
        );
        //@formatter:on
    }

    @TestFactory public Stream<DynamicTest> indentMultipleExpressions5() throws ParseError {
        //@formatter:off
        return testLayoutSensitiveSuccessByExpansions(
            "indent2\n" +
            " e1 e2",
            "Indent2(\"e1\", \"e2\")",
            "Indent2"
        );
        //@formatter:on
    }

    @TestFactory public Stream<DynamicTest> indentMultipleExpressionsFailed1() throws ParseError {
        //@formatter:off
        return testLayoutSensitiveParseFiltered(
            "indent2 e1\n" +
            "e2",
            "Indent2"
        );
        //@formatter:on
    }

    @TestFactory public Stream<DynamicTest> indentMultipleExpressionsFailed2() throws ParseError {
        //@formatter:off
        return testLayoutSensitiveParseFiltered(
            "indent2\n" +
            "e1\n" +
            " e2",
            "Indent2"
        );
        //@formatter:on
    }

    @TestFactory public Stream<DynamicTest> indentMultipleExpressionsFailed3() throws ParseError {
        //@formatter:off
        return testLayoutSensitiveParseFiltered(
            "indent2\n" +
            " e1\n" +
            "e2",
            "Indent2"
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

    @TestFactory public Stream<DynamicTest> newlineIndentMultipleExpressions1() throws ParseError {
        //@formatter:off
        return testLayoutSensitiveSuccessByExpansions(
            "NLindent2\n" +
            " e1 e2",
            "NLIndent2(\"e1\", \"e2\")",
            "NLIndent2"
        );
        //@formatter:on
    }

    @TestFactory public Stream<DynamicTest> newlineIndentMultipleExpressions2() throws ParseError {
        //@formatter:off
        return testLayoutSensitiveSuccessByExpansions(
            "NLindent2\n" +
            " e1\n" +
            " e2",
            "NLIndent2(\"e1\", \"e2\")",
            "NLIndent2"
        );
        //@formatter:on
    }

    @TestFactory public Stream<DynamicTest> newlineIndentMultipleExpressions3() throws ParseError {
        //@formatter:off
        return testLayoutSensitiveSuccessByExpansions(
            "NLindent2\n" +
            " e1\n" +
            "  e2",
            "NLIndent2(\"e1\", \"e2\")",
            "NLIndent2"
        );
        //@formatter:on
    }

    @TestFactory public Stream<DynamicTest> newlineIndentMultipleExpressions4() throws ParseError {
        //@formatter:off
        return testLayoutSensitiveSuccessByExpansions(
            "NLindent2\n" +
            "  e1\n" +
            " e2",
            "NLIndent2(\"e1\", \"e2\")",
            "NLIndent2"
        );
        //@formatter:on
    }

    @TestFactory public Stream<DynamicTest> newlineIndentMultipleExpressionsFailed1() throws ParseError {
        //@formatter:off
        return testLayoutSensitiveParseFiltered(
            "NLindent2 e1 e2",
            "NLIndent2"
        );
        //@formatter:on
    }

    @TestFactory public Stream<DynamicTest> newlineIndentMultipleExpressionsFailed2() throws ParseError {
        //@formatter:off
        return testLayoutSensitiveParseFiltered(
            "NLindent2 e1\n" +
            " e2",
            "NLIndent2"
        );
        //@formatter:on
    }

    @TestFactory public Stream<DynamicTest> newlineIndentMultipleExpressionsFailed3() throws ParseError {
        //@formatter:off
        return testLayoutSensitiveParseFiltered(
            "NLindent2\n" +
            "e1" +
            " e2",
            "NLIndent2"
        );
        //@formatter:on
    }

    @TestFactory public Stream<DynamicTest> newlineIndentMultipleExpressionsFailed4() throws ParseError {
        //@formatter:off
        return testLayoutSensitiveParseFiltered(
            "NLindent2\n" +
            "e1 e2",
            "NLIndent2"
        );
        //@formatter:on
    }

    @TestFactory public Stream<DynamicTest> newlineIndentMultipleExpressionsFailed5() throws ParseError {
        //@formatter:off
        return testLayoutSensitiveParseFiltered(
            "NLindent2\n" +
            " e1\n" +
            "e2",
            "NLIndent2"
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

    @TestFactory public Stream<DynamicTest> notSingleLine1() throws ParseError {
        //@formatter:off
        return testLayoutSensitiveSuccessByExpansions(
            "foo\nbar",
            "NotSingleLine()",
            "NotSingleLine"
        );
        //@formatter:on
    }

    @TestFactory public Stream<DynamicTest> notSingleLine2() throws ParseError {
        //@formatter:off
        return testLayoutSensitiveSuccessByExpansions(
            "\n\nfoo\n\n\nbar\n",
            "NotSingleLine()",
            "NotSingleLine"
        );
        //@formatter:on
    }

    @TestFactory public Stream<DynamicTest> notSingleLineSingleLine() throws ParseError {
        //@formatter:off
        return testLayoutSensitiveParseFiltered(
            "foo bar",
            "NotSingleLine"
        );
        //@formatter:on
    }

    @TestFactory public Stream<DynamicTest> templateCombined1() throws ParseError {
        //@formatter:off
        return testLayoutSensitiveSuccessByExpansions(
            "if e1 + e2:\n" +
            " s1",
            "TemplateCombined(Add(\"e1\", \"e2\"), [\"s1\"])",
            "TemplateCombined"
        );
        //@formatter:on
    }

    @TestFactory public Stream<DynamicTest> templateCombined2() throws ParseError {
        //@formatter:off
        return testLayoutSensitiveSuccessByExpansions(
            "if e1:\n" +
            " s1\n" +
            " s2\n" +
            " s3\n",
            "TemplateCombined(\"e1\", [\"s1\", \"s2\", \"s3\"])",
            "TemplateCombined"
        );
        //@formatter:on
    }

    @TestFactory public Stream<DynamicTest> templateCombinedFail1() throws ParseError {
        //@formatter:off
        return testLayoutSensitiveParseFiltered(
            "if e1:\n" +
            " s1\n" +
            "  s2\n" +
            " s3\n",
            "TemplateCombined"
        );
        //@formatter:on
    }

    @TestFactory public Stream<DynamicTest> templateCombinedFail2() throws ParseError {
        //@formatter:off
        return testLayoutSensitiveParseFiltered(
            "if e1: s1",
            "TemplateCombined"
        );
        //@formatter:on
    }

    @TestFactory public Stream<DynamicTest> templateCombinedFail3() throws ParseError {
        //@formatter:off
        return testLayoutSensitiveParseFiltered(
            "if e1:\n" +
            "s1",
            "TemplateCombined"
        );
        //@formatter:on
    }

    @TestFactory public Stream<DynamicTest> templateCombinedFail4() throws ParseError {
        //@formatter:off
        return testLayoutSensitiveParseFiltered(
            "if e1: s1\n" +
            "       s2",
            "TemplateCombined"
        );
        //@formatter:on
    }

    @TestFactory public Stream<DynamicTest> templateCombinedFail5() throws ParseError {
        //@formatter:off
        return testLayoutSensitiveParseFiltered(
            "if e1\n" +
            ":\n" +
            " s1",
            "TemplateCombined"
        );
        //@formatter:on
    }

    @TestFactory public Stream<DynamicTest> templateCombinedFail6() throws ParseError {
        //@formatter:off
        return testLayoutSensitiveParseFiltered(
            "if e1 +\n" +
            "e2: \n" +
            "   s2",
            "TemplateCombined"
        );
        //@formatter:on
    }

    @TestFactory public Stream<DynamicTest> templateCombinedFail7() throws ParseError {
        //@formatter:off
        return testLayoutSensitiveParseFiltered(
            "if\n" +
            "  e1:\n" +
            "     s2",
            "TemplateCombined"
        );
        //@formatter:on
    }

    @TestFactory public Stream<DynamicTest> templateCombinedFail8() throws ParseError {
        //@formatter:off
        return testLayoutSensitiveParseFiltered(
            "if e1:\n" +
            "s1\n" +
            "s2",
            "TemplateCombined"
        );
        //@formatter:on
    }

    @TestFactory public Stream<DynamicTest> tokenized1() throws ParseError {
        //@formatter:off
        return testLayoutSensitiveSuccessByExpansions(
            "colon:",
            "Tokenized()",
            "Tokenized"
        );
        //@formatter:on
    }

    @TestFactory public Stream<DynamicTest> tokenized2() throws ParseError {
        //@formatter:off
        return testLayoutSensitiveSuccessByExpansions(
            "colon     :",
            "Tokenized()",
            "Tokenized"
        );
        //@formatter:on
    }

    @TestFactory public Stream<DynamicTest> tokenized3() throws ParseError {
        //@formatter:off
        return testLayoutSensitiveSuccessByExpansions(
            "\ncolon     :\n",
            "Tokenized()",
            "Tokenized"
        );
        //@formatter:on
    }

    @TestFactory public Stream<DynamicTest> tokenizedFail1() throws ParseError {
        //@formatter:off
        return testLayoutSensitiveParseFiltered(
            "colon\n:",
            "Tokenized"
        );
        //@formatter:on
    }

    @TestFactory public Stream<DynamicTest> tokenizedFail2() throws ParseError {
        //@formatter:off
        return testLayoutSensitiveParseFiltered(
            "\ncolon  \n     :\n",
            "Tokenized"
        );
        //@formatter:on
    }
}
