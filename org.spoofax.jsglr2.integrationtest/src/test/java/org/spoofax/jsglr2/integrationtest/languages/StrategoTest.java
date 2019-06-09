package org.spoofax.jsglr2.integrationtest.languages;

import org.junit.Test;
import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.jsglr2.integrationtest.BaseTestWithParseTableFromTermWithJSGLR1;
import org.spoofax.terms.ParseError;

import java.io.IOException;

public class StrategoTest extends BaseTestWithParseTableFromTermWithJSGLR1 {

    public StrategoTest() throws Exception {
        setupParseTable("Stratego");
    }

    @Test public void testSampleProgramByExpectedAST() throws ParseError, IOException {
        String sampleProgram = getFileAsString("Stratego/sampleProgram.txt");
        IStrategoTerm expectedAST = getFileAsAST("Stratego/sampleProgram.ast");

        testSuccessByAstString(sampleProgram, expectedAST.toString());
    }

    @Test public void testSampleProgramByJSGLR1() throws ParseError, IOException {
        String sampleProgram = getFileAsString("Stratego/sampleProgram.txt");

        testSuccessByJSGLR1(sampleProgram);
    }

}