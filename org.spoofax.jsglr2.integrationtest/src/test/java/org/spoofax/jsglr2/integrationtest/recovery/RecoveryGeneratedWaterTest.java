package org.spoofax.jsglr2.integrationtest.recovery;

import java.util.Arrays;
import java.util.stream.Stream;

import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.spoofax.jsglr2.integrationtest.BaseTestWithRecoverySdf3ParseTables;
import org.spoofax.jsglr2.integrationtest.MessageDescriptor;
import org.spoofax.jsglr2.messages.Severity;
import org.spoofax.terms.ParseError;

public class RecoveryGeneratedWaterTest extends BaseTestWithRecoverySdf3ParseTables {

    public RecoveryGeneratedWaterTest() {
        super("recovery-generated-water.sdf3", true, true);
    }

    @TestFactory public Stream<DynamicTest> testWater() throws ParseError {
        return testMessages("bar", Arrays.asList(
        //@formatter:off
            new MessageDescriptor("Not expected", Severity.ERROR, 0, 1, 1, 3)
        //@formatter:on
        ), getTestVariants(isRecoveryVariant));
    }

    @TestFactory public Stream<DynamicTest> testWaterAfterLayout() throws ParseError {
        // If insertions are preceded by layout, the corresponding message is moved to the start of that layout. Here we
        // test that the message for water is not moved if it is preceded by layout.
        return testMessages(" bar", Arrays.asList(
        //@formatter:off
            new MessageDescriptor("Not expected", Severity.ERROR, 1, 1, 2, 3)
        //@formatter:on
        ), getTestVariants(isRecoveryVariant));
    }

}
