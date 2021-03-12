package org.spoofax.jsglr2.integrationtest.recovery;

import java.util.Arrays;
import java.util.stream.Stream;

import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.spoofax.jsglr2.integrationtest.BaseTestWithRecoverySdf3ParseTables;
import org.spoofax.jsglr2.integrationtest.MessageDescriptor;
import org.spoofax.jsglr2.messages.Severity;
import org.spoofax.terms.ParseError;

public class RecoveryPermissiveLiteralTest extends BaseTestWithRecoverySdf3ParseTables {

    public RecoveryPermissiveLiteralTest() {
        super("recovery-permissive-literal.sdf3", true, false, true);
    }

    @TestFactory public Stream<DynamicTest> testOpeningLiteralExpected() throws ParseError {
        return testMessages("}", Arrays.asList(
        //@formatter:off
            new MessageDescriptor("{ expected", Severity.ERROR, 0, 1, 1, 1)
        //@formatter:on
        ), getTestVariants(isRecoveryVariant));
    }

    @TestFactory public Stream<DynamicTest> testClosingLiteralExpected() throws ParseError {
        return testMessages("{", Arrays.asList(
        //@formatter:off
            new MessageDescriptor("} expected", Severity.ERROR, 0, 1, 1, 1)
        //@formatter:on
        ), getTestVariants(isRecoveryVariant));
    }

}
