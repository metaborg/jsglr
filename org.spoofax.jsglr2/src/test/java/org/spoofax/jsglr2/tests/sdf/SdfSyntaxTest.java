package org.spoofax.jsglr2.tests.sdf;

import java.io.IOException;
import java.net.URISyntaxException;

import org.junit.Test;
import org.spoofax.jsglr.client.InvalidParseTableException;
import org.spoofax.jsglr2.parsetable.ParseTableReadException;
import org.spoofax.jsglr2.tests.util.BaseTest;
import org.spoofax.jsglr2.util.WithGrammar;
import org.spoofax.jsglr2.util.WithJSGLR1;
import org.spoofax.terms.ParseError;

public class SdfSyntaxTest extends BaseTest implements WithJSGLR1, WithGrammar {

    public SdfSyntaxTest() throws ParseError, ParseTableReadException, IOException, InvalidParseTableException,
        InterruptedException, URISyntaxException {
        setupParseTableFromDefFile("sdf-syntax");
    }

    @Test
    public void identifier() throws ParseError, ParseTableReadException, IOException {
        testSuccessByExpansions("x", "\"x\"");
    }

}