package org.spoofax.jsglr2.integrationtest.recovery;

import java.util.Arrays;
import java.util.stream.Stream;

import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.spoofax.jsglr2.integrationtest.BaseTestWithRecoverySdf3ParseTables;
import org.spoofax.jsglr2.integrationtest.MessageDescriptor;
import org.spoofax.jsglr2.messages.Severity;
import org.spoofax.terms.ParseError;

public class RecoveryLexicalInsertionMessagesTest extends BaseTestWithRecoverySdf3ParseTables {

    public RecoveryLexicalInsertionMessagesTest() {
        super("recovery-insertion-lexical.sdf3", false, false, true);
    }

    @TestFactory public Stream<DynamicTest> testInsertion() throws ParseError {
        return testMessages("xz", Arrays.asList(
        //@formatter:off
            new MessageDescriptor("y expected", Severity.ERROR, 1, 1, 2, 1)
        //@formatter:on
        ), getTestVariants(isRecoveryVariant));
    }

    // We expect messages for insertions at the start of whitespace, which differs from the location of recovery
    // (typically at the end of whitespace)

    @TestFactory public Stream<DynamicTest> testInsertionAfterWhitespace() throws ParseError {
        return testMessages("x   z", Arrays.asList(
        //@formatter:off
            new MessageDescriptor("y expected", Severity.ERROR, 1, 1, 2, 1)
        //@formatter:on
        ), getTestVariants(isRecoveryVariant));
    }

    @TestFactory public Stream<DynamicTest> testInsertionAfterWhitespaceWithNewline() throws ParseError {
        return testMessages("x \n z", Arrays.asList(
        //@formatter:off
            new MessageDescriptor("y expected", Severity.ERROR, 1, 1, 2, 1)
        //@formatter:on
        ), getTestVariants(isRecoveryVariant));
    }

    @TestFactory public Stream<DynamicTest> testInsertionAfterWhitespaceStartingWithNewline() throws ParseError {
        // If the whitespace starts with a newline, report on the character before that

        return testMessages("x\n z", Arrays.asList(
        //@formatter:off
            new MessageDescriptor("y expected", Severity.ERROR, 0, 1, 1, 1)
        //@formatter:on
        ), getTestVariants(isRecoveryVariant));
    }

}
