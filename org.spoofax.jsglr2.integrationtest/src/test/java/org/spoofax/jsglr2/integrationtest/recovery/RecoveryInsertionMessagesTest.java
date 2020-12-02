package org.spoofax.jsglr2.integrationtest.recovery;

import java.util.Arrays;
import java.util.stream.Stream;

import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.spoofax.jsglr2.integrationtest.BaseTestWithRecoverySdf3ParseTables;
import org.spoofax.jsglr2.integrationtest.MessageDescriptor;
import org.spoofax.jsglr2.messages.Severity;
import org.spoofax.terms.ParseError;

public class RecoveryInsertionMessagesTest extends BaseTestWithRecoverySdf3ParseTables {

    public RecoveryInsertionMessagesTest() {
        super("recovery-insertion.sdf3", false, false);
    }

    @TestFactory public Stream<DynamicTest> testSingleLineYRecovering() throws ParseError {
        return testMessages("xz", Arrays.asList(
        //@formatter:off
            new MessageDescriptor("Y expected", Severity.ERROR, 1, 1, 2, 1)
        //@formatter:on
        ), getTestVariants(isRecoveryVariant));
    }

}
