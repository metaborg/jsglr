package org.spoofax.jsglr2.integrationtest.grammars;

import org.spoofax.jsglr2.integrationtest.BaseTestWithSdf3ParseTables;

public class RejectTest extends BaseTestWithSdf3ParseTables {

    public RejectTest() {
        super("reject.sdf3");
    }

    /*
    @Test
    public void testReject() throws ParseError, ParseTableReadException, IOException {
        testParseFailure("foo");
    }

    @Test
    public void testNestedReject() throws ParseError, ParseTableReadException, IOException {
        testParseFailure("bar");
    }

    @Test
    public void testNonReject() throws ParseError, ParseTableReadException, IOException {
        testSuccessByAstString("baz", "Id(\"baz\")");
    }
    */

}