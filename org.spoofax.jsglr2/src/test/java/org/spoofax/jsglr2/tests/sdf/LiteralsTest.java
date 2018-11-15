package org.spoofax.jsglr2.tests.sdf;

import java.io.IOException;
import java.net.URISyntaxException;

import org.junit.Test;
import org.spoofax.jsglr.client.InvalidParseTableException;
import org.spoofax.jsglr2.parsetable.ParseTableReadException;
import org.spoofax.jsglr2.tests.util.BaseTestWithParseTableFromTerm;
import org.spoofax.jsglr2.util.WithLegacySdfGrammar;
import org.spoofax.jsglr2.util.WithJSGLR1;
import org.spoofax.terms.ParseError;

public class LiteralsTest extends BaseTestWithParseTableFromTerm implements WithJSGLR1, WithLegacySdfGrammar {

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