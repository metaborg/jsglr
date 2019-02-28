package org.spoofax.jsglr2.integrationtest.languages;

import java.io.IOException;

import org.junit.Test;
import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.jsglr2.integrationtest.BaseTestWithParseTableFromTermWithJSGLR1;
import org.spoofax.jsglr2.parsetable.ParseTableReadException;
import org.spoofax.terms.ParseError;

public class Java8Test extends BaseTestWithParseTableFromTermWithJSGLR1 {

    public Java8Test() throws Exception {
    	setupParseTable("Java8");
    }

    @Test
    public void testSampleProgramByExpectedAST() throws ParseError, IOException {
        String sampleProgram = getFileAsString("Java/sampleProgram.txt");
        IStrategoTerm expectedAST = getFileAsAST("Java/sampleProgram.ast");

        testSuccessByAstString(sampleProgram, expectedAST.toString());
    }

    @Test
    public void testSampleProgramByJSGLR1() throws ParseError, IOException {
        String sampleProgram = getFileAsString("Java/sampleProgram.txt");

        testSuccessByJSGLR1(sampleProgram);
    }

}