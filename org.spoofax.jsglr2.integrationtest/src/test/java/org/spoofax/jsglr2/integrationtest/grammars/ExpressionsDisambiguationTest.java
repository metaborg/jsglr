package org.spoofax.jsglr2.integrationtest.grammars;

import org.junit.Test;
import org.spoofax.jsglr2.integrationtest.BaseTestWithSdf3ParseTables;
import org.spoofax.terms.ParseError;

public class ExpressionsDisambiguationTest extends BaseTestWithSdf3ParseTables {

    public ExpressionsDisambiguationTest() {
        super("expressions-disambiguation.sdf3");
    }

    @Test public void leftAssociativeOperator() throws ParseError {
        testSuccessByExpansions("x+x+x", "Inf1(Inf1(Term(), Term()), Term())");
    }

    @Test public void rightAssociativeOperator() throws ParseError {
        testSuccessByExpansions("x-x-x", "Inf2(Term(), Inf2(Term(), Term()))");
    }

    @Test public void nonAssociativeOperator() throws ParseError {
        testParseFailure("x*x*x");
    }

    @Test public void lowPriorityInfixLeft() throws ParseError {
        testSuccessByExpansions("x-x+x", "Inf2(Term(), Inf1(Term(), Term()))");
    }

    @Test public void highPriorityInfixLeft() throws ParseError {
        testSuccessByExpansions("x-x*x", "Inf3(Inf2(Term(), Term()), Term())");
    }

    @Test public void highPriorityPrefix() throws ParseError {
        testSuccessByExpansions("x+@x+x", "Inf1(Inf1(Term(), Pre1(Term())), Term())");
    }

    @Test public void lowPriorityPrefixDeep() throws ParseError {
        testSuccessByExpansions("x+$x+x", "Inf1(Term(), Pre2(Inf1(Term(), Term())))");
    }

    @Test public void highPriorityPostfix() throws ParseError {
        testSuccessByExpansions("x+x!+x", "Inf1(Inf1(Term(), Pos1(Term())), Term())");
    }

    @Test public void lowPriorityPostfixDeep() throws ParseError {
        testSuccessByExpansions("x+x%+x", "Inf1(Pos2(Inf1(Term(), Term())), Term())");
    }

    @Test public void danglingPrefixClosest() throws ParseError {
        testSuccessByExpansions("&&x^x", "DisPre1(DisInPre1(Term(), Term()))");
    }

    @Test public void danglingPrefixFurthest() throws ParseError {
        testSuccessByExpansions("??x:x", "DisInPre2(DisPre2(Term()), Term())");
    }

    @Test public void danglingSuffixClosest() throws ParseError {
        testSuccessByExpansions("x.x''", "DisPos1(DisInPos1(Term(), Term()))");
    }

    @Test public void danglingSuffixFurthest() throws ParseError {
        testSuccessByExpansions("x~x\"\"", "DisInPos2(Term(), DisPos2(Term()))");
    }

    @Test public void longestMatchFrontSimple() throws ParseError {
        testSuccessByExpansions("xx]]", "ListFront([ListFront([Term(), Term()])])");
    }

    @Test public void longestMatchFrontComplex() throws ParseError {
        testSuccessByExpansions("xx]xxx]xx]",
            "ListFront(\n" + "  [ ListFront(\n" + "      [ListFront([Term(), Term()]), Term(), Term(), Term()]\n"
                + "    )\n" + "  , Term()\n" + "  , Term()\n" + "  ]\n" + ")");
    }

    @Test public void longestMatchBackSimple() throws ParseError {
        testSuccessByExpansions(";;xx", "ListBack([ListBack([Term(), Term()])])");
    }

    @Test public void longestMatchBackComplex() throws ParseError {
        testSuccessByExpansions(";xx;xxx;xx", "ListBack(\n" + "  [ Term()\n" + "  , Term()\n" + "  , ListBack(\n"
            + "      [Term(), Term(), Term(), ListBack([Term(), Term()])]\n" + "    )\n" + "  ]\n" + ")");
    }

    @Test public void shortestMatchBackSimple() throws ParseError {
        testSuccessByExpansions("//yy", "ListBack2([ListBack2([Term()]), Term()])");
    }

    @Test public void shortestMatchBackComplex() throws ParseError {
        testSuccessByExpansions("/yy/yyy/yy",
            "ListBack2(\n" + "  [ Term()\n" + "  , Term()\n" + "  , ListBack2([Term()])\n" + "  , Term()\n"
                + "  , Term()\n" + "  , ListBack2([Term()])\n" + "  , Term()\n" + "  ]\n" + ")");
    }

    @Test public void shortestMatchFrontSimple() throws ParseError {
        testSuccessByExpansions("yy||", "ListFront2([Term(), ListFront2([Term()])])");
    }

    @Test public void shortestMatchFrontComplex() throws ParseError {
        testSuccessByExpansions("yy|yyy|yy|",
            "ListFront2(\n" + "  [ Term()\n" + "  , ListFront2([Term()])\n" + "  , Term()\n" + "  , Term()\n"
                + "  , ListFront2([Term()])\n" + "  , Term()\n" + "  , Term()\n" + "  ]\n" + ")");
    }


}
