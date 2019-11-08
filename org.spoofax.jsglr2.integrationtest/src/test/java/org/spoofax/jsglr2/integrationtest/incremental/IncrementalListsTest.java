package org.spoofax.jsglr2.integrationtest.incremental;

import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.spoofax.jsglr2.integrationtest.BaseTestWithSdf3ParseTables;
import org.spoofax.terms.ParseError;

import java.util.stream.Stream;

public class IncrementalListsTest extends BaseTestWithSdf3ParseTables {

    public IncrementalListsTest() {
        super("lists.sdf3");
    }

    @TestFactory public Stream<DynamicTest> testIncrementalLayoutSeparatedXs() throws ParseError {
        //@formatter:off
        return testIncrementalSuccessByExpansions(
            new String[] {
                "x x x",
                "x x x x",
                "x x x"
            },
            new String[] {
                "amb([ZeroOrMoreXs([X, X, X]),    OneOrMoreXs([X, X, X])   ])",
                "amb([ZeroOrMoreXs([X, X, X, X]), OneOrMoreXs([X, X, X, X])])",
                "amb([ZeroOrMoreXs([X, X, X]),    OneOrMoreXs([X, X, X])   ])"
            }
        );
        //@formatter:on
    }

}