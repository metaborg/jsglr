package org.spoofax.jsglr2.integrationtest.grammars;

import java.io.IOException;
import org.junit.Test;
import org.spoofax.jsglr2.integrationtest.BaseTestWithSdf3ParseTables;
import org.spoofax.jsglr2.parsetable.ParseTableReadException;
import org.spoofax.terms.ParseError;

public class LiteralsTest extends BaseTestWithSdf3ParseTables {

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