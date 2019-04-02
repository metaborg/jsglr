package org.spoofax.jsglr2.integrationtest.grammars;

import org.junit.Test;
import org.spoofax.jsglr2.incremental.EditorUpdate;
import org.spoofax.jsglr2.integrationtest.BaseTestWithSdf3ParseTables;
import org.spoofax.terms.ParseError;

public class ExpPrioritiesTest extends BaseTestWithSdf3ParseTables {

    public ExpPrioritiesTest() {
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

    @Test public void changingPriorities() {
        // @formatter:off
        testIncrementalSuccessByExpansions("x+x+x",
            new EditorUpdate[] {
                new EditorUpdate(1, 2, "*"),
                new EditorUpdate(3, 4, "*"),
                new EditorUpdate(1, 2, "+"),
                new EditorUpdate(3, 4, "+")
            },
            new String[] {
                "Add(Add(Term(),Term()),Term())",
                "Add(Mult(Term(),Term()),Term())",
                "Mult(Mult(Term(),Term()),Term())",
                "Add(Term(),Mult(Term(),Term()))",
                "Add(Add(Term(),Term()),Term())"
            });
        // @formatter:on
    }

    @Test public void largerPrioritiesTest() {
        testIncrementalSuccessByExpansions(new String[] { "x*x+x*x+x*x", "x*x+x*x+x+x", "x*x*x*x+x+x" },
            new String[] { "Add(Add(Mult(Term,Term),Mult(Term,Term)),Mult(Term,Term))",
                "Add(Add(Add(Mult(Term,Term),Mult(Term,Term)),Term),Term)",
                "Add(Add(Mult(Mult(Mult(Term,Term),Term),Term),Term),Term)" });
    }

    @Test public void nothingChangedTest() {
        testIncrementalSuccessByExpansions(new String[] { "x*x+x*x+x*x", "x*x+x*x+x*x" },
            new String[] { "Add(Add(Mult(Term,Term),Mult(Term,Term)),Mult(Term,Term))",
                "Add(Add(Mult(Term,Term),Mult(Term,Term)),Mult(Term,Term))" });
    }

}
