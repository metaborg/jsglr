package org.spoofax.jsglr2.integrationtest.incremental;

import java.util.stream.Stream;

import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.spoofax.jsglr2.integrationtest.BaseTestWithSdf3ParseTables;

public class IncrementalPreferAvoidTest extends BaseTestWithSdf3ParseTables {

    public IncrementalPreferAvoidTest() {
        super("prefer-avoid.sdf3");
    }

    @TestFactory public Stream<DynamicTest> testIncrementalPreferAvoid() {
        //@formatter:off
        return testIncrementalSuccessByExpansions(
            new String[] { "p x",        "a x",                 "p x" },
            new String[] { "Prefer(X1)", "Avoid(amb([X2,X3]))", "Prefer(X1)" }
        );
        //@formatter:off
    }
}