package org.spoofax.jsglr2.integrationtest.features;

import org.junit.Ignore;
import org.junit.Test;
import org.spoofax.jsglr2.integrationtest.BaseTestWithSdf3ParseTables;
import org.spoofax.terms.ParseError;

public class RejectTest extends BaseTestWithSdf3ParseTables {

    public RejectTest() {
        super("reject.sdf3");
    }

    // This test only fails when running `./b build all`, not when running the tests separately.
    @Ignore @Test public void testReject() throws ParseError {
        testSuccessByAstString("foo", "Foo");
    }

    /**
     * This test cannot pass until reject priorities have been implemented.
     *
     * @see org.spoofax.jsglr2.stack.collections.IForActorStacks
     */
    @Ignore @Test public void testNestedReject() throws ParseError {
        testParseFailure("bar");
    }

    @Test public void testNonReject() throws ParseError {
        testSuccessByAstString("baz", "Id(\"baz\")");
    }

}