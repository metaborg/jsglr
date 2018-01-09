package org.spoofax.jsglr2.tests.sdf;

import java.io.IOException;
import java.net.URISyntaxException;

import org.junit.Test;
import org.spoofax.jsglr.client.InvalidParseTableException;
import org.spoofax.jsglr2.parsetable.ParseTableReadException;
import org.spoofax.jsglr2.tests.util.BaseTest;
import org.spoofax.jsglr2.util.WithGrammar;
import org.spoofax.jsglr2.util.WithJSGLR1;
import org.spoofax.terms.ParseError;

public class ListsTest extends BaseTest implements WithJSGLR1, WithGrammar {

    public ListsTest() throws ParseError, ParseTableReadException, IOException, InvalidParseTableException,
        InterruptedException, URISyntaxException {
        setupParseTableFromDefFile("lists");
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