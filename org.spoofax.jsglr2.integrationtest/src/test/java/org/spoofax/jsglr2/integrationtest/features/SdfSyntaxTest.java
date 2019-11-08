package org.spoofax.jsglr2.integrationtest.features;

import org.junit.jupiter.api.Test;
import org.spoofax.jsglr2.integrationtest.BaseTestWithSdf3ParseTables;
import org.spoofax.terms.ParseError;

public class SdfSyntaxTest extends BaseTestWithSdf3ParseTables {

    public SdfSyntaxTest() {
        super("sdf-syntax.sdf3");
    }

    @Test public void identifier() throws ParseError {
        testSuccessByExpansions("x", "\"x\"");
    }

}