package org.spoofax.jsglr2.integrationtest.features;

import java.util.stream.Stream;

import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.spoofax.jsglr2.integrationtest.BaseTestWithSdf3ParseTables;

public class PreferAvoidTest extends BaseTestWithSdf3ParseTables {

    public PreferAvoidTest() {
        super("prefer-avoid.sdf3");
    }

    @TestFactory public Stream<DynamicTest> testAvoid() {
        return testSuccessByExpansions("a x", "Avoid(amb([X2, X3]))");
    }

    @TestFactory public Stream<DynamicTest> testPrefer() {
        return testSuccessByExpansions("p x", "Prefer(X1)");
    }

}