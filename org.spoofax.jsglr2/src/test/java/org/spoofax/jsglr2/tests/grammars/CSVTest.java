package org.spoofax.jsglr2.tests.grammars;

import java.io.IOException;
import java.net.URISyntaxException;

import org.junit.Test;
import org.spoofax.jsglr.client.InvalidParseTableException;
import org.spoofax.jsglr2.parsetable.ParseTableReadException;
import org.spoofax.jsglr2.tests.util.BaseTestWithJSGLR1;
import org.spoofax.jsglr2.util.WithGrammar;
import org.spoofax.terms.ParseError;

public class CSVTest extends BaseTestWithJSGLR1 implements WithGrammar {

    public CSVTest() throws ParseError, ParseTableReadException, IOException, InvalidParseTableException,
        InterruptedException, URISyntaxException {
        setupParseTableFromDefFile("csv");
    }

    @Test
    public void singleRowSingleColumn() throws ParseError, ParseTableReadException, IOException {
        testSuccessByExpansions("42", "Document([Row([Int(\"42\")])])");
        testSuccessByExpansions("\"abc\"", "Document([Row([String(\"\\\"abc\\\"\")])])");
    }

    @Test
    public void singleRowMultipleColumns() throws ParseError, ParseTableReadException, IOException {
        testSuccessByExpansions("1,2", "Document([Row([Int(\"1\"), Int(\"2\")])])");
        testSuccessByExpansions("1 ,	2", "Document([Row([Int(\"1\"), Int(\"2\")])])");
    }

    @Test
    public void multipleRows() throws ParseError, ParseTableReadException, IOException {
        testSuccessByExpansions("1,2\n3", "Document([Row([Int(\"1\"), Int(\"2\")]), Row([Int(\"3\")])])");
    }

}