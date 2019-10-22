package org.spoofax.jsglr2.integrationtest.incremental;

import org.junit.Test;
import org.spoofax.jsglr2.integrationtest.BaseTestWithSdf3ParseTables;
import org.spoofax.terms.ParseError;

public class IncrementalListsTest extends BaseTestWithSdf3ParseTables {

    public IncrementalListsTest() {
        super("lists.sdf3");
    }

    @Test public void testIncrementalLayoutSeparatedXs() throws ParseError {
        //@formatter:off
        testIncrementalSuccessByExpansions(
            new String[] {
                "x x x",
                "x x x x",
                "x x x"
            },
            new String[] {
                "amb([ZeroOrMoreXs([X, X, X]),    OneOrMoreXs([X, X, X])   ])",
                "amb([ZeroOrMoreXs([X, X, X, X]), OneOrMoreXs([X, X, X, X])])",
                "amb([ZeroOrMoreXs([X, X, X]),    OneOrMoreXs([X, X, X])   ])"
            }
        );
        //@formatter:on
    }

}