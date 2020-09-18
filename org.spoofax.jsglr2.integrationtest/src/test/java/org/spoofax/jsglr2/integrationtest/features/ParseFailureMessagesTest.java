package org.spoofax.jsglr2.integrationtest.features;

import java.util.Arrays;
import java.util.stream.Stream;

import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.spoofax.jsglr2.integrationtest.BaseTestWithSdf3ParseTables;
import org.spoofax.jsglr2.integrationtest.MessageDescriptor;
import org.spoofax.jsglr2.messages.Severity;
import org.spoofax.jsglr2.parser.result.ParseFailureCause;
import org.spoofax.terms.ParseError;

public class ParseFailureMessagesTest extends BaseTestWithSdf3ParseTables {

    public ParseFailureMessagesTest() {
        super("parse-failure-messages.sdf3");
    }

    @TestFactory public Stream<DynamicTest> wrongStartSymbolA() throws ParseError {
        return testMessages("b", Arrays.asList(
        //@formatter:off
            new MessageDescriptor(ParseFailureCause.Type.InvalidStartSymbol.message, Severity.ERROR)
        //@formatter:on
        ), "A");
    }

    @TestFactory public Stream<DynamicTest> wrongStartSymbolB() throws ParseError {
        return testMessages("a", Arrays.asList(
        //@formatter:off
            new MessageDescriptor(ParseFailureCause.Type.InvalidStartSymbol.message, Severity.ERROR)
        //@formatter:on
        ), "B");
    }

    @TestFactory public Stream<DynamicTest> unexpectedInputoffset0() throws ParseError {
        return testMessages("c", Arrays.asList(
        //@formatter:off
            new MessageDescriptor(ParseFailureCause.Type.UnexpectedInput.message, Severity.ERROR, 0, 1, 1, 1)
        //@formatter:on
        ));
    }

    @TestFactory public Stream<DynamicTest> unexpectedInputoffset1() throws ParseError {
        return testMessages("xc", Arrays.asList(
        //@formatter:off
            new MessageDescriptor(ParseFailureCause.Type.UnexpectedInput.message, Severity.ERROR, 1, 1, 2, 1)
        //@formatter:on
        ));
    }

    @TestFactory public Stream<DynamicTest> unexpectedInputoffset2() throws ParseError {
        return testMessages("xxc", Arrays.asList(
        //@formatter:off
            new MessageDescriptor(ParseFailureCause.Type.UnexpectedInput.message, Severity.ERROR, 2, 1, 3, 1)
        //@formatter:on
        ));
    }

    // EOF messages are initially reported on the character after the last character, but since that is out of the range
    // of the original input they are reported on the last character.

    @TestFactory public Stream<DynamicTest> unexpectedEOFempty() throws ParseError {
        return testMessages("", Arrays.asList(
        //@formatter:off
            new MessageDescriptor(ParseFailureCause.Type.UnexpectedEOF.message, Severity.ERROR)
        //@formatter:on
        ));
    }

    @TestFactory public Stream<DynamicTest> unexpectedEOFx() throws ParseError {
        return testMessages("x", Arrays.asList(
        //@formatter:off
            new MessageDescriptor(ParseFailureCause.Type.UnexpectedEOF.message, Severity.ERROR, 0, 1, 1, 1)
        //@formatter:on
        ));
    }

    @TestFactory public Stream<DynamicTest> unexpectedEOFxx() throws ParseError {
        return testMessages("xx", Arrays.asList(
        //@formatter:off
            new MessageDescriptor(ParseFailureCause.Type.UnexpectedEOF.message, Severity.ERROR, 1, 1, 2, 1)
        //@formatter:on
        ));
    }

}
