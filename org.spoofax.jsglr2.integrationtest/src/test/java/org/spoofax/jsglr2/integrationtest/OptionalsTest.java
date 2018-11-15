package org.spoofax.jsglr2.integrationtest;

import java.io.IOException;
import org.junit.Test;
import org.spoofax.jsglr2.parsetable.ParseTableReadException;
import org.spoofax.terms.ParseError;

public class OptionalsTest extends BaseTestWithSpoofaxCoreSdf3 {

    public OptionalsTest(){
        super("optionals.sdf3");
    }

    @Test
    public void testEmpty() throws ParseError, ParseTableReadException, IOException {
        testSuccessByExpansions("", "None");
    }

    @Test
    public void testSingleX() throws ParseError, ParseTableReadException, IOException {
        testSuccessByExpansions("X", "Some(X)");
    }

}