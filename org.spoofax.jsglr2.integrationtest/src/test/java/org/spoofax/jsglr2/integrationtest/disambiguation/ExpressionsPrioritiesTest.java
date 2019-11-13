package org.spoofax.jsglr2.integrationtest.disambiguation;

import java.util.stream.Stream;

import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.spoofax.jsglr2.integrationtest.BaseTestWithSdf3ParseTables;
import org.spoofax.terms.ParseError;

public class ExpressionsPrioritiesTest extends BaseTestWithSdf3ParseTables {

    public ExpressionsPrioritiesTest() {
        super("exp-priorities.sdf3");
    }

    @TestFactory public Stream<DynamicTest> oneTerm() throws ParseError {
        return testSuccessByExpansions("x", "Term()");
    }

    @TestFactory public Stream<DynamicTest> onePlus() throws ParseError {
        return testSuccessByExpansions("x+x", "Add(Term(),Term())");
    }

    @TestFactory public Stream<DynamicTest> oneMult() throws ParseError {
        return testSuccessByExpansions("x*x", "Mult(Term(),Term())");
    }

    @TestFactory public Stream<DynamicTest> twoPlus() throws ParseError {
        return testSuccessByExpansions("x+x+x", "Add(Add(Term(),Term()),Term())");
    }

    @TestFactory public Stream<DynamicTest> multPlus() throws ParseError {
        return testSuccessByExpansions("x*x+x", "Add(Mult(Term(),Term()),Term())");
    }

    @TestFactory public Stream<DynamicTest> plusMult() throws ParseError {
        return testSuccessByExpansions("x+x*x", "Add(Term(),Mult(Term(),Term()))");
    }

    @TestFactory public Stream<DynamicTest> twoMult() throws ParseError {
        return testSuccessByExpansions("x*x*x", "Mult(Mult(Term(),Term()),Term())");
    }

}
