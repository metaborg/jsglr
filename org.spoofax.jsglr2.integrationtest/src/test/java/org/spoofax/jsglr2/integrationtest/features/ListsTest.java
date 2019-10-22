package org.spoofax.jsglr2.integrationtest.features;

import org.junit.Test;
import org.spoofax.jsglr2.integrationtest.BaseTestWithSdf3ParseTables;
import org.spoofax.terms.ParseError;

public class ListsTest extends BaseTestWithSdf3ParseTables {

    public ListsTest() {
        super("lists.sdf3");
    }

    @Test public void testEmpty() throws ParseError {
        testSuccessByExpansions("", "amb([ZeroOrMoreXs([]),ZeroOrMoreXsCommaSeparated([])])");
    }

    @Test public void testSingleX() throws ParseError {
        testSuccessByExpansions("x",
            "amb([ZeroOrMoreXs([X]),ZeroOrMoreXsCommaSeparated([X]),OneOrMoreXs([X]),OneOrMoreXsCommaSeparated([X])])");
    }

    @Test public void testTwoLayoutSeparatedXs() throws ParseError {
        testSuccessByExpansions("x x", "amb([ZeroOrMoreXs([X, X]), OneOrMoreXs([X, X])])");
    }

    @Test public void testTwoCommaSeparatedXs() throws ParseError {
        testSuccessByExpansions("x,x", "amb([ZeroOrMoreXsCommaSeparated([X, X]), OneOrMoreXsCommaSeparated([X, X])])");
    }

    @Test public void testThreeLayoutSeparatedXs() throws ParseError {
        testSuccessByExpansions("x x x", "amb([ZeroOrMoreXs([X, X, X]), OneOrMoreXs([X, X, X])])");
    }

    @Test public void testThreeCommaSeparatedXs() throws ParseError {
        testSuccessByExpansions("x,x , x",
            "amb([ZeroOrMoreXsCommaSeparated([X, X, X]), OneOrMoreXsCommaSeparated([X, X, X])])");
    }

}