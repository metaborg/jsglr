package org.spoofax.jsglr2.integrationtest;

import java.io.IOException;
import org.junit.Test;
import org.spoofax.jsglr2.parsetable.ParseTableReadException;
import org.spoofax.terms.ParseError;

public class SdfSyntaxTest extends BaseTestWithSpoofaxCoreSdf3 {

    public SdfSyntaxTest() {
        super("sdf-syntax.sdf3");
    }

    @Test
    public void identifier() throws ParseError, ParseTableReadException, IOException {
        testSuccessByExpansions("x", "\"x\"");
    }

}