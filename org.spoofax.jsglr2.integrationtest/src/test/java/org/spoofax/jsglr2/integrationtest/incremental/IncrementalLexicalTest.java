package org.spoofax.jsglr2.integrationtest.incremental;

import org.junit.Test;
import org.spoofax.jsglr2.integrationtest.BaseTestWithSdf3ParseTables;
import org.spoofax.terms.ParseError;

public class IncrementalLexicalTest extends BaseTestWithSdf3ParseTables {

    public IncrementalLexicalTest() {
        super("lexical-id.sdf3");
    }

    @Test public void incrementalIdentifiers() throws ParseError {
        //@formatter:off
        testIncrementalSuccessByExpansions(
            new String[] { "a",     "abcde",     "abfghije",     "abfghije",     "abfghijeklm",     "xyzabfghijeklm" },
            new String[] { "\"a\"", "\"abcde\"", "\"abfghije\"", "\"abfghije\"", "\"abfghijeklm\"", "\"xyzabfghijeklm\"" }
        );
        //@formatter:on
    }

}
