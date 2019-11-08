package org.spoofax.jsglr2.integrationtest.features;

import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.spoofax.jsglr2.integrationtest.BaseTestWithSdf3ParseTables;
import org.spoofax.terms.ParseError;

import java.util.stream.Stream;

public class LiteralsTest extends BaseTestWithSdf3ParseTables {

    public LiteralsTest() {
        super("literals.sdf3");
    }

    @TestFactory public Stream<DynamicTest> testLowerCaseLiteralLowerCaseRequired() throws ParseError {
        return testSuccessByExpansions("sensitive", "Literal(CaseSensitive)");
    }

    @TestFactory public Stream<DynamicTest> testMixedLiteralLowerCaseRequired() throws ParseError {
        return testParseFailure("senSitive");
    }

    @TestFactory public Stream<DynamicTest> testLowerCaseLiteralMixedAllowed() throws ParseError {
        return testSuccessByExpansions("insensitive", "Literal(CaseInsensitive)");
    }

    @TestFactory public Stream<DynamicTest> testMixedLiteralMixedAllowed() throws ParseError {
        return testSuccessByExpansions("insenSitive", "Literal(CaseInsensitive)");
    }

}