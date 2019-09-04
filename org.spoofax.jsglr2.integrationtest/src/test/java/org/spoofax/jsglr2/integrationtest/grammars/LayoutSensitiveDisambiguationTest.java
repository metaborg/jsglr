package org.spoofax.jsglr2.integrationtest.grammars;

import org.junit.Test;
import org.spoofax.jsglr2.integrationtest.BaseTestWithLayoutSensitiveSdf3ParseTables;
import org.spoofax.terms.ParseError;

public class LayoutSensitiveDisambiguationTest extends BaseTestWithLayoutSensitiveSdf3ParseTables {

    public LayoutSensitiveDisambiguationTest() {
        super("layout-sensitive.sdf3");
    }

    @Test public void alignmentMisaligned1() throws ParseError {
        testLayoutSensitiveParseFiltered("doAlignList s1 \n" + "       s2");
    }

    @Test public void alignmentMisaligned2() throws ParseError {
        testLayoutSensitiveParseFiltered("doAlignList s1 \n" + "             s2");
    }

    @Test public void alignmentAligned() throws ParseError {
        testLayoutSensitiveSuccessByExpansions("doAlignList s1 \n" + "            s2", "DoAlignList([\"s1\", \"s2\"])");
    }

    @Test public void explicitAlignmentMisaligned1() throws ParseError {
        testLayoutSensitiveParseFiltered("doAlign s1 \n" + "       s2");
    }

    @Test public void explicitAlignmentMisaligned2() throws ParseError {
        testLayoutSensitiveParseFiltered("doAlign s1 \n" + "         s2");
    }

    @Test public void explicitAlignmentAligned() throws ParseError {
        testLayoutSensitiveSuccessByExpansions("doAlign s1 \n" + "        s2",
            "DoAlign(StmtSeq(Stmt(\"s1\"), \"s2\"))");
    }

    @Test public void offsideExpression1() throws ParseError {
        testLayoutSensitiveSuccessByExpansions("do e1 +\n" + "    e2", "Do(OffsideExp(Add(\"e1\", \"e2\")))");
    }

    @Test public void offsideExpression2() throws ParseError {
        testLayoutSensitiveSuccessByExpansions("do e1 +\n" + "   e2", "Add(Do(OffsideExp(\"e1\")), \"e2\")");
    }

    @Test public void offsideExpression3() throws ParseError {
        testLayoutSensitiveSuccessByExpansions("do e1 +\n" + "  e2", "Add(Do(OffsideExp(\"e1\")), \"e2\")");
    }

    @Test public void offsideMultipleElements1() throws ParseError {
        testLayoutSensitiveSuccessByExpansions("doOffside e1\n" + "+ e2", "Add(DoOffside(Exp(\"e1\")), \"e2\")");
    }

    @Test public void offsideMultipleElements2() throws ParseError {
        testLayoutSensitiveSuccessByExpansions("doOffside e1\n" + " + e2", "DoOffside(Exp(Add(\"e1\", \"e2\")))");
    }

    @Test public void indentExpression1() throws ParseError {
        testLayoutSensitiveSuccessByExpansions("doIndent s1", "DoIndent(\"s1\")");
    }

    @Test public void indentExpression2() throws ParseError {
        testLayoutSensitiveSuccessByExpansions("doIndent \n" + " s1", "DoIndent(\"s1\")");
    }

    @Test public void indentExpressionFailed() throws ParseError {
        testLayoutSensitiveParseFiltered("doIndent \n" + "s1");
    }

    @Test public void newlineIndentExpressionFailed1() throws ParseError {
        testLayoutSensitiveParseFiltered("doNLIndent s1");
    }

    @Test public void newlineIndentExpressionFailed2() throws ParseError {
        testLayoutSensitiveParseFiltered("doNLIndent \n" + "s1");
    }

    @Test public void newlineIndentExpression() throws ParseError {
        testLayoutSensitiveSuccessByExpansions("doNLIndent \n" + " s1", "DoNLIndent(\"s1\"))");
    }



}
