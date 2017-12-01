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

public class LiteralsTest extends BaseTest implements WithJSGLR1, WithGrammar {

    public LiteralsTest() throws ParseError, ParseTableReadException, IOException, InvalidParseTableException,
        InterruptedException, URISyntaxException {
        setupParseTableFromDefFile("literals");
    }

    @Test
    public void testLowerCaseLiteralLowerCaseRequired() throws ParseError, ParseTableReadException, IOException {
        testSuccessByExpansions("sensitive", "Literal(CaseSensitive)");
    }

    @Test
    public void testMixedLiteralLowerCaseRequired() throws ParseError, ParseTableReadException, IOException {
        testParseFailure("senSitive");
    }

    @Test
    public void testLowerCaseLiteralMixedAllowed() throws ParseError, ParseTableReadException, IOException {
        testSuccessByExpansions("insensitive", "Literal(CaseInsensitive)");
    }

    @Test
    public void testMixedLiteralMixedAllowed() throws ParseError, ParseTableReadException, IOException {
        testSuccessByExpansions("insenSitive", "Literal(CaseInsensitive)");
    }

}