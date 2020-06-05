package org.spoofax.jsglr2.integrationtest.disambiguation;

import java.util.stream.Stream;

import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.spoofax.jsglr2.integrationtest.BaseTestWithSdf3ParseTables;
import org.spoofax.terms.ParseError;

public class EmptyListAmbiguousTest extends BaseTestWithSdf3ParseTables {

    public EmptyListAmbiguousTest() {
        super("empty-list-ambiguous.sdf3");
    }

    @TestFactory public Stream<DynamicTest> emptyList() throws ParseError {
        return testSuccessByExpansions("[]", "List(amb([[Empty()],[]]))");
    }

    @TestFactory public Stream<DynamicTest> oneElement() throws ParseError {
        return testSuccessByExpansions("[x]", "List([Term()])");
    }

}
