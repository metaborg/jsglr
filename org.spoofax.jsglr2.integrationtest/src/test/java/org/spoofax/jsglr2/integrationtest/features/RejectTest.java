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

    // This test only fails when running `./b build all`, not when running the tests separately.
    @Disabled @TestFactory public Stream<DynamicTest> testReject() throws ParseError {
        return testSuccessByAstString("foo", "List([Foo])");
    }

    /**
     * This test cannot pass until reject priorities have been implemented.
     *
     * @see org.spoofax.jsglr2.stack.collections.IForActorStacks
     */
    @Disabled @TestFactory public Stream<DynamicTest> testNestedReject() throws ParseError {
        return testParseFailure("bar");
    }

    @TestFactory public Stream<DynamicTest> testNonReject() throws ParseError {
        return testSuccessByAstString("baz", "List([Id(\"baz\")])");
    }

    // N.B. this test finds an edgecase where JSGLR2 will exhibit a bug if you use a multimap with insertion-order preservation in org.metaborg.sdf2table.grammar.NormGrammar
    @Disabled("This test fails _sometimes_. The theory is that this is due to a bug that is only exposed based on a certain ordering of objects in a hash-based collection, where the objects have a non-deterministic hashcode (e.g. by using the default one).")
    @TestFactory public Stream<DynamicTest> testBoth() throws ParseError {
        return testSuccessByAstString("foo baz", "List([Foo,Id(\"baz\")])");
    }

}
