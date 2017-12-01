package org.spoofax.jsglr2.tests.parser;

import java.io.IOException;
import java.net.URISyntaxException;

import org.junit.Test;
import org.spoofax.jsglr.client.InvalidParseTableException;
import org.spoofax.jsglr2.parsetable.ParseTableReadException;
import org.spoofax.jsglr2.tests.util.BaseTest;
import org.spoofax.jsglr2.util.WithGrammar;
import org.spoofax.terms.ParseError;

public class RejectTest extends BaseTest implements WithGrammar {

    public RejectTest() throws ParseError, ParseTableReadException, IOException, InvalidParseTableException,
        InterruptedException, URISyntaxException {
        setupParseTableFromDefFile("reject");
    }

    @Test
    public void testReject() throws ParseError, ParseTableReadException, IOException {
        testParseFailure("foo");
    }

    @Test
    public void testNestedReject() throws ParseError, ParseTableReadException, IOException {
        testParseFailure("bar");
    }

    @Test
    public void testNonReject() throws ParseError, ParseTableReadException, IOException {
        testSuccessByAstString("baz", "Id(\"baz\")");
    }

}