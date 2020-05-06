package org.spoofax.jsglr2.integrationtest.recovery;

import java.util.Arrays;
import java.util.stream.Stream;

import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.spoofax.jsglr2.integrationtest.BaseTestWithRecoverySdf3ParseTables;
import org.spoofax.jsglr2.integrationtest.MessageDescriptor;
import org.spoofax.jsglr2.messages.Severity;
import org.spoofax.terms.ParseError;

public class RecoveryLexicalHiddenInsertionMessagesTest extends BaseTestWithRecoverySdf3ParseTables {

    public RecoveryLexicalHiddenInsertionMessagesTest() {
        super("recovery-insertion-lexical-hidden.sdf3", false);
    }

    @TestFactory public Stream<DynamicTest> testHiddenYRecovering() throws ParseError {
        return testMessages("xz", Arrays.asList(
        //@formatter:off
            new MessageDescriptor("Token expected", Severity.ERROR, 1, 1, 2)
        //@formatter:on
        ), getTestVariants(isRecoveryVariant));
    }

}
