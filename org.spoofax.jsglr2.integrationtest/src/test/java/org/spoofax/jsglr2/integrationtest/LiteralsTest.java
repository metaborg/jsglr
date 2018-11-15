package org.spoofax.jsglr2.integrationtest;

import java.io.IOException;
import org.junit.Test;
import org.spoofax.jsglr2.parsetable.ParseTableReadException;
import org.spoofax.terms.ParseError;

public class LiteralsTest extends BaseTestWithSpoofaxCoreSdf3 {

    public LiteralsTest() {
        super("literals.sdf3");
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