package org.spoofax.jsglr2.integrationtest.grammars;

import java.io.IOException;
import org.junit.Test;
import org.spoofax.jsglr2.integrationtest.BaseTestWithSdf3ParseTables;
import org.spoofax.jsglr2.parsetable.ParseTableReadException;
import org.spoofax.terms.ParseError;

public class CSVTest extends BaseTestWithSdf3ParseTables {

    public CSVTest() {
        super("csv.sdf3");
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