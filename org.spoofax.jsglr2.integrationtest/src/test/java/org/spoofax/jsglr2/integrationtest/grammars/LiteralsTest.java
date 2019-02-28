package org.spoofax.jsglr2.integrationtest.grammars;

import org.junit.Test;
import org.spoofax.jsglr2.integrationtest.BaseTestWithSdf3ParseTables;
import org.spoofax.terms.ParseError;

public class LiteralsTest extends BaseTestWithSdf3ParseTables {

    public LiteralsTest() {
        super("literals.sdf3");
    }

    @Test
    public void testLowerCaseLiteralLowerCaseRequired() throws ParseError {
        testSuccessByExpansions("sensitive", "Literal(CaseSensitive)");
    }

    @Test
    public void testMixedLiteralLowerCaseRequired() throws ParseError {
        testParseFailure("senSitive");
    }

    @Test
    public void testLowerCaseLiteralMixedAllowed() throws ParseError {
        testSuccessByExpansions("insensitive", "Literal(CaseInsensitive)");
    }

    @Test
    public void testMixedLiteralMixedAllowed() throws ParseError {
        testSuccessByExpansions("insenSitive", "Literal(CaseInsensitive)");
    }

}