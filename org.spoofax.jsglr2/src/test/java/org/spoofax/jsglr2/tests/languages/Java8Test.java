package org.spoofax.jsglr2.tests.languages;

import java.io.IOException;

import org.junit.Test;
import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.jsglr.client.InvalidParseTableException;
import org.spoofax.jsglr2.parsetable.ParseTableReadException;
import org.spoofax.jsglr2.tests.util.BaseTestWithParseTableFromTermWithJSGLR1;
import org.spoofax.jsglr2.util.WithParseTableFromTerm;
import org.spoofax.terms.ParseError;

public class Java8Test extends BaseTestWithParseTableFromTermWithJSGLR1 implements WithParseTableFromTerm {

    public Java8Test()
        throws ParseError, ParseTableReadException, IOException, InvalidParseTableException, InterruptedException {
        setupParseTable("Java8");
    }

    @Test
    public void testSampleProgramByExpectedAST() throws ParseError, ParseTableReadException, IOException {
        String sampleProgram = getFileAsString("Java/sampleProgram.txt");
        IStrategoTerm expectedAST = getFileAsAST("Java/sampleProgram.ast");

        testSuccessByAstString(sampleProgram, expectedAST.toString());
    }

    @Test
    public void testSampleProgramByJSGLR1() throws ParseError, ParseTableReadException, IOException {
        String sampleProgram = getFileAsString("Java/sampleProgram.txt");

        testSuccessByJSGLR1(sampleProgram);
    }

}