package org.spoofax.jsglr2.integrationtest.disambiguation;

import org.junit.jupiter.api.Test;
import org.spoofax.jsglr2.integrationtest.BaseTestWithSdf3ParseTables;
import org.spoofax.terms.ParseError;

public class ExpressionsPrioritiesTest extends BaseTestWithSdf3ParseTables {

    public ExpressionsPrioritiesTest() {
        super("exp-priorities.sdf3");
    }

    @Test public void oneTerm() throws ParseError {
        testSuccessByExpansions("x", "Term()");
    }

    @Test public void onePlus() throws ParseError {
        testSuccessByExpansions("x+x", "Add(Term(),Term())");
    }

    @Test public void oneMult() throws ParseError {
        testSuccessByExpansions("x*x", "Mult(Term(),Term())");
    }

    @Test public void twoPlus() throws ParseError {
        testSuccessByExpansions("x+x+x", "Add(Add(Term(),Term()),Term())");
    }

    @Test public void multPlus() throws ParseError {
        testSuccessByExpansions("x*x+x", "Add(Mult(Term(),Term()),Term())");
    }

    @Test public void plusMult() throws ParseError {
        testSuccessByExpansions("x+x*x", "Add(Term(),Mult(Term(),Term()))");
    }

    @Test public void twoMult() throws ParseError {
        testSuccessByExpansions("x*x*x", "Mult(Mult(Term(),Term()),Term())");
    }

}
