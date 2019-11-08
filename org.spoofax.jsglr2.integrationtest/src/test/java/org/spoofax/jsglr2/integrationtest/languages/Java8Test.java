package org.spoofax.jsglr2.integrationtest.languages;

import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.jsglr2.integrationtest.BaseTestWithParseTableFromTermWithJSGLR1;
import org.spoofax.terms.ParseError;

import java.io.IOException;
import java.util.stream.Stream;

public class Java8Test extends BaseTestWithParseTableFromTermWithJSGLR1 {

    public Java8Test() throws Exception {
        setupParseTable("Java8");
    }

    @TestFactory public Stream<DynamicTest> testSampleProgramByExpectedAST() throws ParseError, IOException {
        String sampleProgram = getFileAsString("Java/sampleProgram.txt");
        IStrategoTerm expectedAST = getFileAsAST("Java/sampleProgram.ast");

        return testSuccessByAstString(sampleProgram, expectedAST.toString());
    }

    @TestFactory public Stream<DynamicTest> testSampleProgramByJSGLR1() throws ParseError, IOException {
        String sampleProgram = getFileAsString("Java/sampleProgram.txt");

        return testSuccessByJSGLR1(sampleProgram);
    }

}