package org.spoofax.jsglr2.integrationtest.features;

import java.util.stream.Stream;

import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.spoofax.jsglr2.integrationtest.BaseTestWithSdf3ParseTables;
import org.spoofax.terms.ParseError;

public class LexicalTest extends BaseTestWithSdf3ParseTables {

    public LexicalTest() {
        super("lexical-id.sdf3");
    }

    @TestFactory public Stream<DynamicTest> identifierShort() throws ParseError {
        return testSuccessByExpansions("a", "\"a\")");
    }

    @TestFactory public Stream<DynamicTest> identifierLong() throws ParseError {
        return testSuccessByExpansions("abcde", "\"abcde\")");
    }

}
