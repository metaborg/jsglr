package org.spoofax.jsglr2.tests.sdf;

import java.io.IOException;

import org.junit.Test;
import org.spoofax.jsglr.client.InvalidParseTableException;
import org.spoofax.jsglr2.parsetable.ParseTableReadException;
import org.spoofax.jsglr2.tests.util.BaseTestWithJSGLR1;
import org.spoofax.jsglr2.util.WithGrammar;
import org.spoofax.terms.ParseError;

public class PreferAvoidTest extends BaseTestWithJSGLR1 implements WithGrammar {
	
	public PreferAvoidTest() throws ParseError, ParseTableReadException, IOException, InvalidParseTableException, InterruptedException {
		setupParseTableFromDefFile("prefer-avoid");
	}

    @Test
    public void testAvoid() throws ParseError, ParseTableReadException, IOException {
        testParseSuccessByExpansions("a x", "Avoid(amb([X2, X3]))");
    }

    @Test
    public void testPrefer() throws ParseError, ParseTableReadException, IOException {
        testParseSuccessByExpansions("p x", "Prefer(X1)");
    }

    @Test
    public void testAvoidByJSGLR1() throws ParseError, ParseTableReadException, IOException {
        testParseSuccessByJSGLR("a x");
    }

    @Test
    public void testPreferByJSGLR1() throws ParseError, ParseTableReadException, IOException {
        testParseSuccessByJSGLR("p x");
    }
  
}