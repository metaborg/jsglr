package org.spoofax.jsglr2.integrationtest.grammars;

import org.junit.Test;
import org.spoofax.jsglr2.integrationtest.BaseTestWithSdf3ParseTables;
import org.spoofax.terms.ParseError;

public class LexicalTest extends BaseTestWithSdf3ParseTables {

    public LexicalTest() {
        super("lexical-id.sdf3");
    }

    @Test public void identifiers() throws ParseError {
        testSuccessByExpansions("a", "\"a\")");
        testSuccessByExpansions("abcde", "\"abcde\")");
    }

    @Test public void incrementalIdentifiers() throws ParseError {
        testIncrementalSuccessByExpansions(
            new String[] { "a", "abcde", "abfghije", "abfghije", "abfghijeklm", "xyzabfghijeklm" }, new String[] {
                "\"a\"", "\"abcde\"", "\"abfghije\"", "\"abfghije\"", "\"abfghijeklm\"", "\"xyzabfghijeklm\"" });
    }

}