package org.spoofax.jsglr2.tests.parser;

import java.io.IOException;
import java.net.URISyntaxException;

import org.junit.Test;
import org.spoofax.jsglr.client.InvalidParseTableException;
import org.spoofax.jsglr2.parsetable.ParseTableReadException;
import org.spoofax.jsglr2.tests.util.BaseTest;
import org.spoofax.jsglr2.util.WithGrammar;
import org.spoofax.terms.ParseError;

public class CommentsTest extends BaseTest implements WithGrammar {

    public CommentsTest() throws ParseError, ParseTableReadException, IOException, InvalidParseTableException,
        InterruptedException, URISyntaxException {
        setupParseTableFromDefFile("comments");
    }

    @Test
    public void oneX() throws ParseError, ParseTableReadException, IOException {
        testSuccessByExpansions("x", "Xs([X])");
    }

    @Test
    public void twoXs() throws ParseError, ParseTableReadException, IOException {
        testSuccessByExpansions("x x", "Xs([X, X])");
        testSuccessByExpansions("x x // x", "Xs([X, X])");
        testSuccessByExpansions("x /* x */ x", "Xs([X, X])");
        testSuccessByExpansions("x /* \n */ x", "Xs([X, X])");
    }

}