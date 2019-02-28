package org.spoofax.jsglr2.integrationtest.grammars;

import org.junit.Test;
import org.spoofax.jsglr2.integrationtest.BaseTestWithSdf3ParseTables;
import org.spoofax.terms.ParseError;

public class SumNonAmbiguousTest extends BaseTestWithSdf3ParseTables {

    public SumNonAmbiguousTest() {
        super("sum-nonambiguous.sdf3");
    }

    @Test public void one() throws ParseError {
        testSuccessByExpansions("x", "Term()");
    }

    @Test public void two() throws ParseError {
        testSuccessByExpansions("x+x", "Add(Term(),Term())");
    }

    @Test public void three() throws ParseError {
        testSuccessByExpansions("x+x+x", "Add(Add(Term(),Term()),Term())");
    }

}