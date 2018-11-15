package org.spoofax.jsglr2.integrationtest;

import java.io.IOException;
import org.junit.Test;
import org.spoofax.jsglr2.parsetable.ParseTableReadException;
import org.spoofax.terms.ParseError;

public class SumNonAmbiguousTest extends BaseTestWithSpoofaxCoreSdf3 {

    public SumNonAmbiguousTest() {
        super("sum-nonambiguous.sdf3");
    }

    @Test
    public void one() throws ParseError, ParseTableReadException, IOException {
        testSuccessByExpansions("x", "Term()");
    }

    @Test
    public void two() throws ParseError, ParseTableReadException, IOException {
        testSuccessByExpansions("x+x", "Add(Term(),Term())");
    }

    @Test
    public void three() throws ParseError, ParseTableReadException, IOException {
        testSuccessByExpansions("x+x+x", "Add(Add(Term(),Term()),Term())");
    }

}