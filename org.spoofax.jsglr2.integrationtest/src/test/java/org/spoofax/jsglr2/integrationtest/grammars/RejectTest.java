package org.spoofax.jsglr2.integrationtest.grammars;

import org.junit.Ignore;
import org.junit.Test;
import org.spoofax.jsglr2.incremental.EditorUpdate;
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

    @Ignore @Test public void incrementalReject() throws ParseError {
        testIncrementalSuccessByExpansions("foo",
            new EditorUpdate[] { new EditorUpdate(2, 3, "r"), new EditorUpdate(2, 3, "o") },
            new String[] { "Foo", "Id(\"for\")", "Foo" });
    }

}