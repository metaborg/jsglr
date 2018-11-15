package org.spoofax.jsglr2.integration.test;

import java.io.IOException;
import org.junit.Test;
import org.spoofax.jsglr2.parsetable.ParseTableReadException;
import org.spoofax.terms.ParseError;

public class KernelTest extends BaseTestWithSpoofaxCoreSdf3 {

    public KernelTest() {
        super("kernel");
    }

    @Test
    public void oneX() throws ParseError, ParseTableReadException, IOException {
        // testParseSuccessByJSGLR("\"x\""); TODO: implement kernel constructors
    }

}