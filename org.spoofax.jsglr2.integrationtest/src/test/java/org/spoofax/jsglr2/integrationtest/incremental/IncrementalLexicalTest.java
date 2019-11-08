package org.spoofax.jsglr2.integrationtest.incremental;

import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.spoofax.jsglr2.integrationtest.BaseTestWithSdf3ParseTables;
import org.spoofax.terms.ParseError;

import java.util.stream.Stream;

public class IncrementalLexicalTest extends BaseTestWithSdf3ParseTables {

    public IncrementalLexicalTest() {
        super("lexical-id.sdf3");
    }

    @TestFactory public Stream<DynamicTest> incrementalIdentifiers() throws ParseError {
        //@formatter:off
        return testIncrementalSuccessByExpansions(
            new String[] { "a",     "abcde",     "abfghije",     "abfghije",     "abfghijeklm",     "xyzabfghijeklm" },
            new String[] { "\"a\"", "\"abcde\"", "\"abfghije\"", "\"abfghije\"", "\"abfghijeklm\"", "\"xyzabfghijeklm\"" }
        );
        //@formatter:on
    }

}
