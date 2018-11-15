package org.spoofax.jsglr2.integration.test;

import java.io.IOException;
import org.junit.Test;
import org.spoofax.jsglr2.parsetable.ParseTableReadException;
import org.spoofax.terms.ParseError;

public class LexicalTest extends BaseTestWithSpoofaxCoreSdf3 {

    public LexicalTest() {
        super("lexical-id.sdf3");
    }

    @Test
    public void identifiers() throws ParseError, ParseTableReadException, IOException {
        testSuccessByExpansions("a", "\"a\")");
        testSuccessByExpansions("aaaaa", "\"aaaaa\")");
    }

}