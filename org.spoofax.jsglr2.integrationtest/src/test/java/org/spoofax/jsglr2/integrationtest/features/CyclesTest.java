package org.spoofax.jsglr2.integrationtest.features;

import java.util.stream.Stream;

import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.spoofax.jsglr2.integrationtest.BaseTestWithRecoverySdf3ParseTables;
import org.spoofax.terms.ParseError;

public class CyclesTest extends BaseTestWithRecoverySdf3ParseTables {

    public CyclesTest() {
        super("cycles.sdf3", false);
    }

    @TestFactory public Stream<DynamicTest> testEmpty() throws ParseError {
        return testSuccessByAstString("", "E()");
    }

    @TestFactory public Stream<DynamicTest> testSingle() throws ParseError {
        return testSuccessByAstString("x", "X()");
    }

}
