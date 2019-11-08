package org.spoofax.jsglr2.integrationtest.features;

import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.spoofax.jsglr2.integrationtest.BaseTestWithSdf3ParseTables;
import org.spoofax.terms.ParseError;

import java.util.stream.Stream;

public class ListsTest extends BaseTestWithSdf3ParseTables {

    public ListsTest() {
        super("lists.sdf3");
    }

    @TestFactory public Stream<DynamicTest> testEmpty() throws ParseError {
        return testSuccessByExpansions("", "amb([ZeroOrMoreXs([]),ZeroOrMoreXsCommaSeparated([])])");
    }

    @TestFactory public Stream<DynamicTest> testSingleX() throws ParseError {
        return testSuccessByExpansions("x",
            "amb([ZeroOrMoreXs([X]),ZeroOrMoreXsCommaSeparated([X]),OneOrMoreXs([X]),OneOrMoreXsCommaSeparated([X])])");
    }

    @TestFactory public Stream<DynamicTest> testTwoLayoutSeparatedXs() throws ParseError {
        return testSuccessByExpansions("x x", "amb([ZeroOrMoreXs([X, X]), OneOrMoreXs([X, X])])");
    }

    @TestFactory public Stream<DynamicTest> testTwoCommaSeparatedXs() throws ParseError {
        return testSuccessByExpansions("x,x",
            "amb([ZeroOrMoreXsCommaSeparated([X, X]), OneOrMoreXsCommaSeparated([X, X])])");
    }

    @TestFactory public Stream<DynamicTest> testThreeLayoutSeparatedXs() throws ParseError {
        return testSuccessByExpansions("x x x", "amb([ZeroOrMoreXs([X, X, X]), OneOrMoreXs([X, X, X])])");
    }

    @TestFactory public Stream<DynamicTest> testThreeCommaSeparatedXs() throws ParseError {
        return testSuccessByExpansions("x,x , x",
            "amb([ZeroOrMoreXsCommaSeparated([X, X, X]), OneOrMoreXsCommaSeparated([X, X, X])])");
    }

}