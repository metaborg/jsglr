package org.spoofax.jsglr2.integrationtest.features;

import java.util.Collections;
import java.util.stream.Stream;

import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.spoofax.jsglr2.integrationtest.BaseTestWithSdf3ParseTables;
import org.spoofax.jsglr2.integrationtest.MessageDescriptor;
import org.spoofax.jsglr2.messages.Severity;
import org.spoofax.terms.ParseError;

public class NonAssocMessagesTest extends BaseTestWithSdf3ParseTables {

    public NonAssocMessagesTest() {
        super("expressions-non-assoc.sdf3");
    }

    @TestFactory public Stream<DynamicTest> testPlusLeftAssoc() throws ParseError {
        return testMessages("x + x + x", Collections.emptyList());
    }

    @TestFactory public Stream<DynamicTest> testEqNonAssoc() throws ParseError {
        return testMessages("x == x == x",
            Collections.singletonList(new MessageDescriptor("Operator is non-associative", Severity.ERROR, 0, 1, 1)));
    }

    @TestFactory public Stream<DynamicTest> testGtNonNested() throws ParseError {
        return testMessages("x > x > x",
            Collections.singletonList(new MessageDescriptor("Operator is non-nested", Severity.ERROR, 0, 1, 1)));
    }

}
