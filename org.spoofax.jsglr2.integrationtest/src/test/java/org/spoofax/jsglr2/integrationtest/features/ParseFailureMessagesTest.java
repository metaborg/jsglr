package org.spoofax.jsglr2.integrationtest.features;

import java.util.Arrays;
import java.util.stream.Stream;

import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.metaborg.core.messages.MessageSeverity;
import org.spoofax.jsglr2.integrationtest.BaseTestWithSdf3ParseTables;
import org.spoofax.jsglr2.integrationtest.MessageDescriptor;
import org.spoofax.jsglr2.parser.result.ParseFailureType;
import org.spoofax.terms.ParseError;

public class ParseFailureMessagesTest extends BaseTestWithSdf3ParseTables {

    public ParseFailureMessagesTest() {
        super("parse-failure-messages.sdf3");
    }

    @TestFactory public Stream<DynamicTest> wrongStartSymbolA() throws ParseError {
        return testMessages("b", Arrays.asList(
        //@formatter:off
            new MessageDescriptor(ParseFailureType.InvalidStartSymbol.message, MessageSeverity.ERROR)
        //@formatter:on
        ), "A");
    }

    @TestFactory public Stream<DynamicTest> wrongStartSymbolB() throws ParseError {
        return testMessages("a", Arrays.asList(
        //@formatter:off
            new MessageDescriptor(ParseFailureType.InvalidStartSymbol.message, MessageSeverity.ERROR)
        //@formatter:on
        ), "B");
    }

    @TestFactory public Stream<DynamicTest> unexpectedInput() throws ParseError {
        return testMessages("c", Arrays.asList(
        //@formatter:off
            new MessageDescriptor(ParseFailureType.UnexpectedInput.message, MessageSeverity.ERROR)
        //@formatter:on
        ));
    }

    @TestFactory public Stream<DynamicTest> unexpectedEOF() throws ParseError {
        return testMessages("", Arrays.asList(
        //@formatter:off
            new MessageDescriptor(ParseFailureType.UnexpectedEOF.message, MessageSeverity.ERROR)
        //@formatter:on
        ));
    }

}
