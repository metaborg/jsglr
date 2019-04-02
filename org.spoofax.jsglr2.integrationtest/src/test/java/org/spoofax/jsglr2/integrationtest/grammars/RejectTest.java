package org.spoofax.jsglr2.integrationtest.grammars;

import org.junit.Ignore;
import org.junit.Test;
import org.spoofax.jsglr2.integrationtest.BaseTestWithSdf3ParseTables;
import org.spoofax.terms.ParseError;

public class RejectTest extends BaseTestWithSdf3ParseTables {

    public RejectTest() {
        super("reject.sdf3");
    }


    @Ignore @Test public void testReject() throws ParseError {
        testSuccessByAstString("foo", "Foo");
    }

    @Test public void testNestedReject() throws ParseError {
        testParseFailure("bar");
    }

    @Test public void testNonReject() throws ParseError {
        testSuccessByAstString("baz", "Id(\"baz\")");
    }

    @Test public void incrementalReject() throws ParseError {
        testIncrementalSuccessByExpansions(new String[] { "foo", "for", "foo" },
            new String[] { "Foo", "Id(\"for\")", "Foo" });
    }

}