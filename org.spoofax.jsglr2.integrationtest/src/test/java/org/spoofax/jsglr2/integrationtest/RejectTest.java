package org.spoofax.jsglr2.integrationtest;

import java.io.IOException;
import org.junit.Test;
import org.spoofax.jsglr2.parsetable.ParseTableReadException;
import org.spoofax.terms.ParseError;

public class RejectTest extends BaseTestWithSpoofaxCoreSdf3 {

    public RejectTest() {
        super("reject.sdf3");
    }

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

}