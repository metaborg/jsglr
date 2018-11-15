package org.spoofax.jsglr2.tests.sdf;

import java.io.IOException;
import java.net.URISyntaxException;

import org.junit.Test;
import org.spoofax.jsglr.client.InvalidParseTableException;
import org.spoofax.jsglr2.parsetable.ParseTableReadException;
import org.spoofax.jsglr2.tests.util.BaseTestWithParseTableFromTermWithJSGLR1;
import org.spoofax.jsglr2.util.WithLegacySdfGrammar;
import org.spoofax.terms.ParseError;

public class KernelTest extends BaseTestWithParseTableFromTermWithJSGLR1 implements WithLegacySdfGrammar {

    public KernelTest() throws ParseError, ParseTableReadException, IOException, InvalidParseTableException,
        InterruptedException, URISyntaxException {
        setupParseTableFromDefFile("kernel");
    }

    @Test
    public void oneX() throws ParseError, ParseTableReadException, IOException {
        // testParseSuccessByJSGLR("\"x\""); TODO: implement kernel constructors
    }

}