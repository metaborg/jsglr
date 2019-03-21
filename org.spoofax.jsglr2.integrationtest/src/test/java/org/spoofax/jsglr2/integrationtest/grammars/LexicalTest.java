package org.spoofax.jsglr2.integrationtest.grammars;

import org.junit.Test;
import org.spoofax.jsglr2.incremental.EditorUpdate;
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
        testIncrementalSuccessByExpansions("a",
            new EditorUpdate[] { new EditorUpdate(1, 1, "bcde"), new EditorUpdate(2, 4, "fghij"),
                new EditorUpdate(0, 0, "xyz") },
            new String[] { "\"a\"", "\"abcde\"", "\"abfghije\"", "\"xyzabfghije\"" });
    }

}