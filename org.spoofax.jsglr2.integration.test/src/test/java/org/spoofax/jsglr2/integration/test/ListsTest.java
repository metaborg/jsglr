package org.spoofax.jsglr2.integration.test;

import java.io.IOException;
import org.junit.Test;
import org.spoofax.jsglr2.parsetable.ParseTableReadException;
import org.spoofax.terms.ParseError;

public class ListsTest extends BaseTestWithSpoofaxCoreSdf3 {

    public ListsTest() {
        super("lists.sdf3");
    }

    @Test
    public void testEmpty() throws ParseError, ParseTableReadException, IOException {
        testSuccessByExpansions("", "amb([ZeroOrMoreXs([]),ZeroOrMoreXsCommaSeparated([])])");
    }

    @Test
    public void testSingleX() throws ParseError, ParseTableReadException, IOException {
        testSuccessByExpansions("x",
            "amb([ZeroOrMoreXs([X]),ZeroOrMoreXsCommaSeparated([X]),OneOrMoreXs([X]),OneOrMoreXsCommaSeparated([X])])");
    }

    @Test
    public void testTwoLayoutSeparatedXs() throws ParseError, ParseTableReadException, IOException {
        testSuccessByExpansions("x x", "amb([ZeroOrMoreXs([X, X]), OneOrMoreXs([X, X])])");
    }

    @Test
    public void testTwoCommaSeparatedXs() throws ParseError, ParseTableReadException, IOException {
        testSuccessByExpansions("x,x", "amb([ZeroOrMoreXsCommaSeparated([X, X]), OneOrMoreXsCommaSeparated([X, X])])");
    }

    @Test
    public void testThreeLayoutSeparatedXs() throws ParseError, ParseTableReadException, IOException {
        testSuccessByExpansions("x x x", "amb([ZeroOrMoreXs([X, X, X]), OneOrMoreXs([X, X, X])])");
    }

    @Test
    public void testThreeCommaSeparatedXs() throws ParseError, ParseTableReadException, IOException {
        testSuccessByExpansions("x,x , x",
            "amb([ZeroOrMoreXsCommaSeparated([X, X, X]), OneOrMoreXsCommaSeparated([X, X, X])])");
    }

}