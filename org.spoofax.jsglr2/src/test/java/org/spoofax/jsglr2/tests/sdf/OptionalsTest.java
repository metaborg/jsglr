package org.spoofax.jsglr2.tests.sdf;

import java.io.IOException;
import java.net.URISyntaxException;

import org.junit.Test;
import org.spoofax.jsglr.client.InvalidParseTableException;
import org.spoofax.jsglr2.parsetable.ParseTableReadException;
import org.spoofax.jsglr2.tests.util.BaseTest;
import org.spoofax.jsglr2.util.WithGrammar;
import org.spoofax.terms.ParseError;

public class OptionalsTest extends BaseTest implements WithGrammar {

    public OptionalsTest() throws ParseError, ParseTableReadException, IOException, InvalidParseTableException,
        InterruptedException, URISyntaxException {
        setupParseTableFromDefFile("optionals");
    }

    @Test
    public void testEmpty() throws ParseError, ParseTableReadException, IOException {
        testSuccessByExpansions("", "None");
    }

    @Test
    public void testSingleX() throws ParseError, ParseTableReadException, IOException {
        testSuccessByExpansions("X", "Some(X)");
    }

}