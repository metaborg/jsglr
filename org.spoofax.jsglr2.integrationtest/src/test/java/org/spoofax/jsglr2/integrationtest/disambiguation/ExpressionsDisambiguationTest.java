package org.spoofax.jsglr2.integrationtest.disambiguation;

import java.util.stream.Stream;

import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.spoofax.jsglr2.integrationtest.BaseTestWithSdf3ParseTables;
import org.spoofax.terms.ParseError;

public class ExpressionsDisambiguationTest extends BaseTestWithSdf3ParseTables {

    public ExpressionsDisambiguationTest() {
        super("expressions-disambiguation.sdf3");
    }

    @TestFactory public Stream<DynamicTest> leftAssociativeOperator() throws ParseError {
        return testSuccessByExpansions("x+x+x", "Inf1(Inf1(Term(), Term()), Term())");
    }

    @TestFactory public Stream<DynamicTest> rightAssociativeOperator() throws ParseError {
        return testSuccessByExpansions("x-x-x", "Inf2(Term(), Inf2(Term(), Term()))");
    }

    @TestFactory public Stream<DynamicTest> nonAssociativeOperator() throws ParseError {
        return testSuccessByExpansions("x*x*x", "Inf3(Inf3(Term(), Term()), Term())");
    }

    @TestFactory public Stream<DynamicTest> lowPriorityInfixLeft() throws ParseError {
        return testSuccessByExpansions("x-x+x", "Inf2(Term(), Inf1(Term(), Term()))");
    }

    @TestFactory public Stream<DynamicTest> highPriorityInfixLeft() throws ParseError {
        return testSuccessByExpansions("x-x*x", "Inf3(Inf2(Term(), Term()), Term())");
    }

    @TestFactory public Stream<DynamicTest> highPriorityPrefix() throws ParseError {
        return testSuccessByExpansions("x+@x+x", "Inf1(Inf1(Term(), Pre1(Term())), Term())");
    }

    @TestFactory public Stream<DynamicTest> lowPriorityPrefixDeep() throws ParseError {
        return testSuccessByExpansions("x+$x+x", "Inf1(Term(), Pre2(Inf1(Term(), Term())))");
    }

    @TestFactory public Stream<DynamicTest> highPriorityPostfix() throws ParseError {
        return testSuccessByExpansions("x+x!+x", "Inf1(Inf1(Term(), Pos1(Term())), Term())");
    }

    @TestFactory public Stream<DynamicTest> lowPriorityPostfixDeep() throws ParseError {
        return testSuccessByExpansions("x+x%+x", "Inf1(Pos2(Inf1(Term(), Term())), Term())");
    }

    @TestFactory public Stream<DynamicTest> danglingPrefix() throws ParseError {
        return testSuccessByExpansions("&&x^x", "DisPre1(DisInPre1(Term(), Term()))");
    }

    @TestFactory public Stream<DynamicTest> danglingSuffix() throws ParseError {
        return testSuccessByExpansions("x.x''", "DisPos1(DisInPos1(Term(), Term()))");
    }

    @TestFactory public Stream<DynamicTest> longestMatchFrontSimple() throws ParseError {
        return testSuccessByExpansions("xx]]", "ListFront([ListFront([Term(), Term()])])");
    }

    @TestFactory public Stream<DynamicTest> longestMatchFrontComplex() throws ParseError {
        //@formatter:off
        return testSuccessByExpansions(
            "xx]xxx]xx]",
            "ListFront(\n" +
            "  [ ListFront(\n" +
            "      [ListFront([Term(), Term()]), Term(), Term(), Term()]\n" +
            "    )\n" +
            "  , Term()\n" +
            "  , Term()\n" +
            "  ]\n" +
            ")"
        );
        //@formatter:on
    }

    @TestFactory public Stream<DynamicTest> longestMatchBackSimple() throws ParseError {
        return testSuccessByExpansions(";;xx", "ListBack([ListBack([Term(), Term()])])");
    }

    @TestFactory public Stream<DynamicTest> longestMatchBackComplex() throws ParseError {
        //@formatter:off
        return testSuccessByExpansions(
            ";xx;xxx;xx",
            "ListBack(\n" +
            "  [ Term()\n" +
            "  , Term()\n" +
            "  , ListBack(\n" +
            "      [Term(), Term(), Term(), ListBack([Term(), Term()])]\n" +
            "    )\n" +
            "  ]\n" +
            ")"
        );
        //@formatter:on
    }

}
