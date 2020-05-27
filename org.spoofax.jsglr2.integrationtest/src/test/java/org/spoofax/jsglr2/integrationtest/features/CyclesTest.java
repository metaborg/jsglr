package org.spoofax.jsglr2.integrationtest.features;

import java.util.Arrays;
import java.util.stream.Stream;

import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.spoofax.jsglr2.integrationtest.BaseTestWithRecoverySdf3ParseTables;
import org.spoofax.jsglr2.integrationtest.MessageDescriptor;
import org.spoofax.jsglr2.messages.Severity;
import org.spoofax.jsglr2.parser.result.ParseFailureCause;
import org.spoofax.terms.ParseError;

public class CyclesTest extends BaseTestWithRecoverySdf3ParseTables {

    public CyclesTest() {
        super("cycles.sdf3", false);
    }

    @TestFactory public Stream<DynamicTest> testCycle() throws ParseError {
        // Empty input, so message is not reported on a character
        return testMessages("", Arrays.asList(
        //@formatter:off
            new MessageDescriptor(ParseFailureCause.Type.Cycle.message, Severity.ERROR)
        //@formatter:on
        ));
    }

    @TestFactory public Stream<DynamicTest> testCycleWithPrefix() throws ParseError {
        // The cycle is _after_ the x, but the input ends after the x and thus the message is reported _on_ the x
        return testMessages("x", Arrays.asList(
        //@formatter:off
            new MessageDescriptor(ParseFailureCause.Type.Cycle.message, Severity.ERROR, 0, 1, 1)
        //@formatter:on
        ));
    }

}
