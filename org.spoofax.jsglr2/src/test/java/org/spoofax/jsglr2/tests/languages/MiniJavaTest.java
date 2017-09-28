package org.spoofax.jsglr2.tests.languages;

import java.io.IOException;

import org.junit.Test;
import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.jsglr.client.InvalidParseTableException;
import org.spoofax.jsglr2.parsetable.ParseTableReadException;
import org.spoofax.jsglr2.tests.util.BaseTestWithJSGLR1;
import org.spoofax.jsglr2.util.WithParseTable;
import org.spoofax.terms.ParseError;

public class MiniJavaTest extends BaseTestWithJSGLR1 implements WithParseTable {
	
	public MiniJavaTest() throws ParseError, ParseTableReadException, IOException, InvalidParseTableException, InterruptedException {
	    setupParseTable("MiniJava");
	}
    
    @Test
    public void testSampleProgramByExpectedAST() throws ParseError, ParseTableReadException, IOException {
        String sampleProgram = getFileAsString("MiniJava/sampleProgram.txt");
        IStrategoTerm expectedAST = getFileAsAST("MiniJava/sampleProgram.ast");
        
        testParseSuccessByAstString(sampleProgram, expectedAST.toString());
    }
    
    @Test
    public void testSampleProgramByJSGLR1() throws ParseError, ParseTableReadException, IOException {
        String sampleProgram = getFileAsString("MiniJava/sampleProgram.txt");
        
        testParseSuccessByJSGLR(sampleProgram);
    }
  
}