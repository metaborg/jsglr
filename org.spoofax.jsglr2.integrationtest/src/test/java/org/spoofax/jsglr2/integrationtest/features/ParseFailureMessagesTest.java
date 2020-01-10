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

    @TestFactory public Stream<DynamicTest> unexpectedInputoffset0() throws ParseError {
        return testMessages("c", Arrays.asList(
        //@formatter:off
            new MessageDescriptor(ParseFailureType.UnexpectedInput.message, MessageSeverity.ERROR, 0, 1, 1)
        //@formatter:on
        ));
    }

    @TestFactory public Stream<DynamicTest> unexpectedInputoffset1() throws ParseError {
        return testMessages("xc", Arrays.asList(
        //@formatter:off
            new MessageDescriptor(ParseFailureType.UnexpectedInput.message, MessageSeverity.ERROR, 1, 1, 2)
        //@formatter:on
        ));
    }

    @TestFactory public Stream<DynamicTest> unexpectedInputoffset2() throws ParseError {
        return testMessages("xxc", Arrays.asList(
        //@formatter:off
            new MessageDescriptor(ParseFailureType.UnexpectedInput.message, MessageSeverity.ERROR, 2, 1, 3)
        //@formatter:on
        ));
    }

    @TestFactory public Stream<DynamicTest> unexpectedEOFempty() throws ParseError {
        return testMessages("", Arrays.asList(
        //@formatter:off
            new MessageDescriptor(ParseFailureType.UnexpectedEOF.message, MessageSeverity.ERROR, 0, 1, 1)
        //@formatter:on
        ));
    }

    @TestFactory public Stream<DynamicTest> unexpectedEOFx() throws ParseError {
        return testMessages("x", Arrays.asList(
        //@formatter:off
            new MessageDescriptor(ParseFailureType.UnexpectedEOF.message, MessageSeverity.ERROR, 1, 1, 2)
        //@formatter:on
        ));
    }

    @TestFactory public Stream<DynamicTest> unexpectedEOFxx() throws ParseError {
        return testMessages("xx", Arrays.asList(
        //@formatter:off
            new MessageDescriptor(ParseFailureType.UnexpectedEOF.message, MessageSeverity.ERROR, 2, 1, 3)
        //@formatter:on
        ));
    }

}
