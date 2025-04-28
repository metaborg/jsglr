package org.spoofax.jsglr2.integrationtest.features;

import java.util.stream.Stream;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.spoofax.jsglr2.integrationtest.BaseTestWithSdf3ParseTables;
import org.spoofax.terms.ParseError;

public class RejectTest extends BaseTestWithSdf3ParseTables {

    public RejectTest() {
        super("reject.sdf3");
    }

    @TestFactory public Stream<DynamicTest> testReject() throws ParseError {
        return testSuccessByAstString("foo", "List([Foo])");
    }

    @TestFactory public Stream<DynamicTest> testNestedReject() throws ParseError {
        return testParseFailure("bar");
    }

    @TestFactory public Stream<DynamicTest> testNonReject() throws ParseError {
        return testSuccessByAstString("baz", "List([Id(\"baz\")])");
    }

    @TestFactory public Stream<DynamicTest> testBoth() throws ParseError {
        return testSuccessByAstString("foo baz", "List([Foo,Id(\"baz\")])");
    }

}
