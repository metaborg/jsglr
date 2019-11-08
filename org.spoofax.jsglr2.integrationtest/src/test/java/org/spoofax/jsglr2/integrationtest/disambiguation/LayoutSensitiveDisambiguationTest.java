package org.spoofax.jsglr2.integrationtest.disambiguation;

import org.junit.jupiter.api.Test;
import org.spoofax.jsglr2.integrationtest.BaseTestWithLayoutSensitiveSdf3ParseTables;
import org.spoofax.terms.ParseError;

public class LayoutSensitiveDisambiguationTest extends BaseTestWithLayoutSensitiveSdf3ParseTables {

    public LayoutSensitiveDisambiguationTest() {
        super("layout-sensitive.sdf3");
    }

    @Test public void alignmentMisaligned1() throws ParseError {
        //@formatter:off
        testLayoutSensitiveParseFiltered(
            "doAlignList s1 \n" +
            "       s2"
        );
        //@formatter:on
    }

    @Test public void alignmentMisaligned2() throws ParseError {
        //@formatter:off
        testLayoutSensitiveParseFiltered(
            "doAlignList s1 \n" +
            "             s2"
        );
        //@formatter:on
    }

    @Test public void alignmentAligned() throws ParseError {
        //@formatter:off
        testLayoutSensitiveSuccessByExpansions(
            "doAlignList s1 \n" +
            "            s2",
            "DoAlignList([\"s1\", \"s2\"])"
        );
        //@formatter:on
    }

    @Test public void explicitAlignmentMisaligned1() throws ParseError {
        //@formatter:off
        testLayoutSensitiveParseFiltered(
            "doAlign s1 \n" +
            "       s2");
        //@formatter:on
    }

    @Test public void explicitAlignmentMisaligned2() throws ParseError {
        //@formatter:off
        testLayoutSensitiveParseFiltered(
            "doAlign s1 \n" +
            "         s2"
        );
        //@formatter:on
    }

    @Test public void explicitAlignmentAligned() throws ParseError {
        //@formatter:off
        testLayoutSensitiveSuccessByExpansions(
            "doAlign s1 \n" +
            "        s2",
            "DoAlign(StmtSeq(Stmt(\"s1\"), \"s2\"))"
        );
        //@formatter:on
    }

    @Test public void offsideExpression1() throws ParseError {
        //@formatter:off
        testLayoutSensitiveSuccessByExpansions(
            "do e1 +\n" +
            "    e2",
            "Do(OffsideExp(Add(\"e1\", \"e2\")))"
        );
        //@formatter:on
    }

    @Test public void offsideExpression2() throws ParseError {
        //@formatter:off
        testLayoutSensitiveSuccessByExpansions(
            "do e1 +\n" +
            "   e2",
            "Add(Do(OffsideExp(\"e1\")), \"e2\")"
        );
        //@formatter:on
    }

    @Test public void offsideExpression3() throws ParseError {
        //@formatter:off
        testLayoutSensitiveSuccessByExpansions(
            "do e1 +\n" +
            "  e2",
            "Add(Do(OffsideExp(\"e1\")), \"e2\")"
        );
        //@formatter:on
    }

    @Test public void offsideMultipleElements1() throws ParseError {
        //@formatter:off
        testLayoutSensitiveSuccessByExpansions(
            "doOffside e1\n" +
            "+ e2",
            "Add(DoOffside(Exp(\"e1\")), \"e2\")"
        );
        //@formatter:on
    }

    @Test public void offsideMultipleElements2() throws ParseError {
        //@formatter:off
        testLayoutSensitiveSuccessByExpansions(
            "doOffside e1\n" +
            " + e2",
            "DoOffside(Exp(Add(\"e1\", \"e2\")))"
        );
        //@formatter:on
    }

    @Test public void indentExpression1() throws ParseError {
        //@formatter:off
        testLayoutSensitiveSuccessByExpansions(
            "doIndent s1",
            "DoIndent(\"s1\")"
        );
        //@formatter:on
    }

    @Test public void indentExpression2() throws ParseError {
        //@formatter:off
        testLayoutSensitiveSuccessByExpansions(
            "doIndent \n" +
            " s1",
            "DoIndent(\"s1\")"
        );
        //@formatter:on
    }

    @Test public void indentExpressionFailed() throws ParseError {
        //@formatter:off
        testLayoutSensitiveParseFiltered(
            "doIndent \n" +
            "s1"
        );
        //@formatter:on
    }

    @Test public void newlineIndentExpressionNoNewline() throws ParseError {
        //@formatter:off
        testLayoutSensitiveParseFiltered(
            "doNLIndent s1"
        );
        //@formatter:on
    }

    @Test public void newlineIndentExpressionNoIndent() throws ParseError {
        //@formatter:off
        testLayoutSensitiveParseFiltered(
            "doNLIndent \n" +
            "s1"
        );
        //@formatter:on
    }

    @Test public void newlineIndentExpression() throws ParseError {
        //@formatter:off
        testLayoutSensitiveSuccessByExpansions(
            "doNLIndent \n" +
            " s1",
            "DoNLIndent(\"s1\"))"
        );
        //@formatter:on
    }

}
