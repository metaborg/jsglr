package org.spoofax.jsglr2.integrationtest.grammars;

import java.io.IOException;
import org.junit.Test;
import org.spoofax.jsglr2.integrationtest.BaseTestWithSdf3ParseTables;
import org.spoofax.jsglr2.parsetable.ParseTableReadException;
import org.spoofax.terms.ParseError;

public class SdfSyntaxTest extends BaseTestWithSdf3ParseTables {

    public SdfSyntaxTest() {
        super("sdf-syntax.sdf3");
    }

    @Test
    public void identifier() throws ParseError, ParseTableReadException, IOException {
        testSuccessByExpansions("x", "\"x\"");
    }

}