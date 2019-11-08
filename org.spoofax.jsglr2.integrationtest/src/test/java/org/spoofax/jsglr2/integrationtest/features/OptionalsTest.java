package org.spoofax.jsglr2.integrationtest.features;

import org.junit.jupiter.api.Test;
import org.spoofax.jsglr2.integrationtest.BaseTestWithSdf3ParseTables;
import org.spoofax.terms.ParseError;

public class OptionalsTest extends BaseTestWithSdf3ParseTables {

    public OptionalsTest() {
        super("optionals.sdf3");
    }

    @Test public void testEmpty() throws ParseError {
        testSuccessByExpansions("", "None");
    }

    @Test public void testSingleX() throws ParseError {
        testSuccessByExpansions("X", "Some(X)");
    }

    @Test public void testIncrementalOptionals() throws ParseError {
        //@formatter:off
        testIncrementalSuccessByExpansions(
            new String[] { "X",       "",     "X" },
            new String[] { "Some(X)", "None", "Some(X)" }
        );
        //@formatter:off
    }

}