package org.spoofax.jsglr2.integrationtest.features;

import java.util.stream.Stream;

import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.spoofax.jsglr2.integrationtest.BaseTestWithSdf3ParseTables;
import org.spoofax.terms.ParseError;

public class LexicalTest extends BaseTestWithSdf3ParseTables {

    public LexicalTest() {
        super("lexical-lex-cf-kern.sdf3");
    }

    @TestFactory public Stream<DynamicTest> lexicalShort() throws ParseError {
        return testSuccessByExpansions("Lexical", "a", "\"a\")");
    }

    @TestFactory public Stream<DynamicTest> lexicalLong() throws ParseError {
        return testSuccessByExpansions("Lexical", "abcde", "\"abcde\")");
    }

    @TestFactory public Stream<DynamicTest> contextFreeSingle() throws ParseError {
        return testSuccessByExpansions("ContextFree", "cfa", "Single(97)");
    }

    @TestFactory public Stream<DynamicTest> contextFreeDouble() throws ParseError {
        return testSuccessByExpansions("ContextFree", "cfab", "Double(97,98)");
    }

    @TestFactory public Stream<DynamicTest> kernelSingle() throws ParseError {
        return testSuccessByExpansions("Kernel", "kernela", "Single(97)");
    }

    @TestFactory public Stream<DynamicTest> kernelDouble() throws ParseError {
        return testSuccessByExpansions("Kernel", "kernelab", "Double(97,98)");
    }

}
