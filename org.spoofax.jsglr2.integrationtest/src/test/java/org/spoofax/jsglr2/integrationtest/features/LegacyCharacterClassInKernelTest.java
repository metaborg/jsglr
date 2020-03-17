package org.spoofax.jsglr2.integrationtest.features;

import java.util.stream.Stream;

import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.spoofax.jsglr2.integrationtest.BaseTestWithSdf3ParseTables;
import org.spoofax.jsglr2.integrationtest.BaseTestWithSdf3ParseTablesWithJSGLR1;
import org.spoofax.terms.ParseError;

public class LegacyCharacterClassInKernelTest extends BaseTestWithSdf3ParseTablesWithJSGLR1 {

    public LegacyCharacterClassInKernelTest() {
        super("legacy-cc-in-kernel.sdf3");
    }

    @TestFactory public Stream<DynamicTest> singleEscapeN1() throws ParseError {
        return testSuccessByJSGLR1("\\n");
    }

    @TestFactory public Stream<DynamicTest> singleEscapeQuote1() throws ParseError {
        return testSuccessByJSGLR1("\\\"");
    }

    @TestFactory public Stream<DynamicTest> octalEscape1() throws ParseError {
        return testSuccessByJSGLR1("\\123");
    }

    @TestFactory public Stream<DynamicTest> singleEscapeN2() throws ParseError {
        return testSuccessByExpansions("\\n", "Escape(110)");
    }

    @TestFactory public Stream<DynamicTest> singleEscapeQuote2() throws ParseError {
        return testSuccessByExpansions("\\\"", "Escape(34)");
    }

    @TestFactory public Stream<DynamicTest> octalEscape2() throws ParseError {
        return testSuccessByExpansions("\\123", "OctEscape(49,50,51)");
    }

}
