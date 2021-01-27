package org.spoofax.jsglr2.integrationtest.regression;

import java.util.stream.Stream;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.spoofax.jsglr2.integrationtest.BaseTestWithSdf3ParseTables;

public class Regression20210104OptimizedParseForestTest extends BaseTestWithSdf3ParseTables {

    public Regression20210104OptimizedParseForestTest() {
        super("regression-20210104-optimized-parse-forest.sdf3");
    }

    @Disabled @TestFactory public Stream<DynamicTest> xxy() {
        return testSuccessByAstString("xxy", "S(\"x\",\"xy\")");
    }

}
