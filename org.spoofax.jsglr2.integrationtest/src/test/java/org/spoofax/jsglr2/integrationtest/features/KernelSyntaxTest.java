package org.spoofax.jsglr2.integrationtest.features;

import java.util.stream.Stream;

import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.spoofax.jsglr2.integrationtest.BaseTestWithSdf3ParseTables;
import org.spoofax.terms.ParseError;

public class KernelSyntaxTest extends BaseTestWithSdf3ParseTables {

    public KernelSyntaxTest() {
        super("kernel-syntax.sdf3");
    }

    @TestFactory public Stream<DynamicTest> identifier() throws ParseError {
        return testSuccessByExpansions("x", "\"x\"");
    }

    @TestFactory public Stream<DynamicTest> addition() throws ParseError {
        return testSuccessByExpansions("x+x", "Add(\"x\",\"x\")");
    }

}
