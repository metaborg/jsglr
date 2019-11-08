package org.spoofax.jsglr2.integrationtest.features;

import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.spoofax.jsglr2.integrationtest.BaseTestWithSdf3ParseTables;
import org.spoofax.terms.ParseError;

import java.util.stream.Stream;

public class SdfSyntaxTest extends BaseTestWithSdf3ParseTables {

    public SdfSyntaxTest() {
        super("sdf-syntax.sdf3");
    }

    @TestFactory public Stream<DynamicTest> identifier() throws ParseError {
        return testSuccessByExpansions("x", "\"x\"");
    }

}