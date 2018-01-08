package org.spoofax.jsglr2.tests.sdf;

import java.io.IOException;
import java.net.URISyntaxException;

import org.junit.Test;
import org.spoofax.jsglr.client.InvalidParseTableException;
import org.spoofax.jsglr2.parsetable.ParseTableReadException;
import org.spoofax.jsglr2.tests.util.BaseTestWithJSGLR1;
import org.spoofax.jsglr2.util.WithGrammar;
import org.spoofax.terms.ParseError;

public class PreferAvoidTest extends BaseTestWithJSGLR1 implements WithGrammar {

    public PreferAvoidTest() throws ParseError, ParseTableReadException, IOException, InvalidParseTableException,
        InterruptedException, URISyntaxException {
        setupParseTableFromDefFile("prefer-avoid");
    }

    @Test
    public void testAvoid() throws ParseError, ParseTableReadException, IOException {
        testSuccessByExpansions("a x", "Avoid(amb([X2, X3]))");
    }

    @Test
    public void testPrefer() throws ParseError, ParseTableReadException, IOException {
        testSuccessByExpansions("p x", "Prefer(X1)");
    }

    @Test
    public void testAvoidByJSGLR1() throws ParseError, ParseTableReadException, IOException {
        testSuccessByJSGLR1("a x");
    }

    @Test
    public void testPreferByJSGLR1() throws ParseError, ParseTableReadException, IOException {
        testSuccessByJSGLR1("p x");
    }

}