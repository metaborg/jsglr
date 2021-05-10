package org.spoofax.jsglr2.integrationtest.recovery;

import java.util.Arrays;
import java.util.stream.Stream;

import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.spoofax.jsglr2.integrationtest.BaseTestWithRecoverySdf3ParseTables;
import org.spoofax.jsglr2.integrationtest.MessageDescriptor;
import org.spoofax.jsglr2.messages.Severity;
import org.spoofax.terms.ParseError;

public class RecoveryDisambiguationTest extends BaseTestWithRecoverySdf3ParseTables {

    public RecoveryDisambiguationTest() {
        super("recovery-disambiguation.sdf3", false, false, true);
    }

    @TestFactory public Stream<DynamicTest> testSuccess() throws ParseError {
        return testSuccessByAstString("ab", "Some(AB(A,B))");
    }

    @TestFactory public Stream<DynamicTest> testBInsertion() throws ParseError {
        return testRecovery("a", "Some(AB(A,B))");
    }

    @TestFactory public Stream<DynamicTest> testAInsertion() throws ParseError {
        return testRecovery("b", "Some(AB(A,B))");
    }

    @TestFactory public Stream<DynamicTest> testWater() throws ParseError {
        return testRecovery("c", "None()");
    }

    @TestFactory public Stream<DynamicTest> testWaterLater() throws ParseError {
        // If there are two equal-cost recoveries, choose the one with recovery later. In this test, on the second a.
        return testMessages("a a b", Arrays.asList(
        //@formatter:off
            new MessageDescriptor("Not expected", Severity.ERROR, 2, 1, 3, 1)
        //@formatter:on
        ), getTestVariants(isRecoveryVariant));
    }

    @TestFactory public Stream<DynamicTest> test2WaterLater() throws ParseError {
        return testMessages("a a a b", Arrays.asList(
        //@formatter:off
            new MessageDescriptor("Not expected", Severity.ERROR, 2, 1, 3, 1),
            new MessageDescriptor("Not expected", Severity.ERROR, 4, 1, 5, 1)
        //@formatter:on
        ), getTestVariants(isRecoveryVariant));
    }

}
