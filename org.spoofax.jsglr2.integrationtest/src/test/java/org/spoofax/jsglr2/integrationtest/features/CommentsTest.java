package org.spoofax.jsglr2.integrationtest.features;

import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.spoofax.jsglr2.integrationtest.BaseTestWithSdf3ParseTables;
import org.spoofax.terms.ParseError;

import java.util.stream.Stream;

public class CommentsTest extends BaseTestWithSdf3ParseTables {

    public CommentsTest() {
        super("comments.sdf3");
    }

    @TestFactory public Stream<DynamicTest> oneX() throws ParseError {
        return testSuccessByExpansions("x", "Xs([X])");
    }

    @TestFactory public Stream<DynamicTest> twoXs1() throws ParseError {
        return testSuccessByExpansions("x x", "Xs([X, X])");
    }

    @TestFactory public Stream<DynamicTest> twoXs2() throws ParseError {
        return testSuccessByExpansions("x x // x", "Xs([X, X])");
    }

    @TestFactory public Stream<DynamicTest> twoXs3() throws ParseError {
        return testSuccessByExpansions("x /* x */ x", "Xs([X, X])");
    }

    @TestFactory public Stream<DynamicTest> twoXs4() throws ParseError {
        return testSuccessByExpansions("x /* \n */ x", "Xs([X, X])");
    }

}