package org.spoofax.jsglr2.integrationtest.grammars;

import java.io.IOException;
import org.junit.Test;
import org.spoofax.jsglr2.integrationtest.BaseTestWithSdf3ParseTables;
import org.spoofax.jsglr2.parsetable.ParseTableReadException;
import org.spoofax.terms.ParseError;

public class KernelTest extends BaseTestWithSdf3ParseTables {

    public KernelTest() {
        super("kernel");
    }

    @Test
    public void oneX() throws ParseError, ParseTableReadException, IOException {
        // testParseSuccessByJSGLR("\"x\""); TODO: implement kernel constructors
    }

}