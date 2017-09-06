package org.spoofax.jsglr2.tests.languages;

import java.io.IOException;

import org.junit.Test;
import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.jsglr.client.InvalidParseTableException;
import org.spoofax.jsglr2.parsetable.ParseTableReadException;
import org.spoofax.jsglr2.tests.util.BaseTestWithJSGLR1;
import org.spoofax.jsglr2.util.WithParseTable;
import org.spoofax.terms.ParseError;

public class WebDSLTest extends BaseTestWithJSGLR1 implements WithParseTable {
	
	public WebDSLTest() throws ParseError, ParseTableReadException, IOException, InvalidParseTableException, InterruptedException {
	    setupParseTable("WebDSL");
	}
    
    @Test
    public void testSampleProgramByJSGLR1() throws ParseError, ParseTableReadException, IOException {
        String sampleProgram = getFileAsString("WebDSL/built-in.app");
        
        testParseSuccessByJSGLR(sampleProgram);
    }
  
}