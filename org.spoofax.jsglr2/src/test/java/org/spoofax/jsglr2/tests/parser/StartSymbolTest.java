package org.spoofax.jsglr2.tests.parser;

import java.io.IOException;
import java.net.URISyntaxException;

import org.junit.Test;
import org.spoofax.jsglr.client.InvalidParseTableException;
import org.spoofax.jsglr2.parsetable.ParseTableReadException;
import org.spoofax.jsglr2.tests.util.BaseTestWithParseTableFromTerm;
import org.spoofax.jsglr2.util.WithLegacySdfGrammar;
import org.spoofax.terms.ParseError;

public class StartSymbolTest extends BaseTestWithParseTableFromTerm implements WithLegacySdfGrammar {

    public StartSymbolTest() throws ParseError, ParseTableReadException, IOException, InvalidParseTableException,
        InterruptedException, URISyntaxException {
        setupParseTableFromDefFile("start-symbol");
    }

    @Test
    public void withoutStartSymbol() throws ParseError, ParseTableReadException, IOException {
        testSuccessByExpansions(null, "foo", "amb([\"foo\", Id(\"foo\")])");
    }

    @Test
    public void withStartSymbol() throws ParseError, ParseTableReadException, IOException {
        testSuccessByExpansions("Start", "foo", "Id(\"foo\")");
    }

}