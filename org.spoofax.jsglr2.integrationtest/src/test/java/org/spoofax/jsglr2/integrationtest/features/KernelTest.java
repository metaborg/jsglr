package org.spoofax.jsglr2.integrationtest.features;

import org.junit.jupiter.api.Test;
import org.spoofax.jsglr2.integrationtest.BaseTestWithSdf3ParseTables;
import org.spoofax.terms.ParseError;

public class KernelTest extends BaseTestWithSdf3ParseTables {

    public KernelTest() {
        super("kernel");
    }

    @Test public void oneX() throws ParseError {
        // testParseSuccessByJSGLR("\"x\""); TODO: implement kernel constructors
    }

}