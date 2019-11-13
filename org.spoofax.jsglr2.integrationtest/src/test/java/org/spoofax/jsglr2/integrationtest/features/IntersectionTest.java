package org.spoofax.jsglr2.integrationtest.features;

import java.util.stream.Stream;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.spoofax.jsglr2.integrationtest.BaseTestWithSdf3ParseTables;
import org.spoofax.terms.ParseError;

public class IntersectionTest extends BaseTestWithSdf3ParseTables {

    public IntersectionTest() {
        super("intersection.sdf3");
    }

    // TODO: implement stack priorities during reducing to fix intersection problem (see P9707 Section 8.4)

    @Disabled @TestFactory public Stream<DynamicTest> testOneNotInIntersection() throws ParseError {
        return testParseFailure("1");
    }

    @Disabled @TestFactory public Stream<DynamicTest> testTwoInIntersection() throws ParseError {
        return testSuccessByAstString("2", "Two");
    }

    @Disabled @TestFactory public Stream<DynamicTest> testThreeNotInIntersecton() throws ParseError {
        return testParseFailure("3");
    }

}