package org.spoofax.jsglr2.integrationtest.recovery;

import java.util.Arrays;
import java.util.stream.Stream;

import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.metaborg.core.messages.MessageSeverity;
import org.spoofax.jsglr2.integrationtest.BaseTestWithRecoverySdf3ParseTables;
import org.spoofax.jsglr2.integrationtest.MessageDescriptor;
import org.spoofax.terms.ParseError;

public class RecoveryMessagesTest extends BaseTestWithRecoverySdf3ParseTables {

    public RecoveryMessagesTest() {
        super("recovery.sdf3");
    }

    @TestFactory public Stream<DynamicTest> testSingleLineYRecovering() throws ParseError {
        return testMessages("y", Arrays.asList(
        //@formatter:off
            new MessageDescriptor("Invalid syntax", MessageSeverity.ERROR, 0, 1, 1)
        //@formatter:on
        ), getTestVariants(isRecoveryVariant));
    }

}
