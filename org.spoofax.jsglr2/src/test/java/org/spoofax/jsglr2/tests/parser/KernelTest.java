package org.spoofax.jsglr2.tests.parser;

import java.io.IOException;

import org.junit.Test;
import org.spoofax.jsglr.client.InvalidParseTableException;
import org.spoofax.jsglr2.parsetable.ParseTableReadException;
import org.spoofax.jsglr2.tests.util.BaseTestWithJSGLR1;
import org.spoofax.jsglr2.util.WithGrammar;
import org.spoofax.terms.ParseError;

public class KernelTest extends BaseTestWithJSGLR1 implements WithGrammar {
	
	public KernelTest() throws ParseError, ParseTableReadException, IOException, InvalidParseTableException, InterruptedException {
	    setupParseTableFromDefFile("kernel");
	}
    
    @Test
    public void oneX() throws ParseError, ParseTableReadException, IOException {
        testParseSuccessByJSGLR("\"x\"");
    }
  
}