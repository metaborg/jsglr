package org.spoofax.jsglr2.integrationtest.languages;

import java.io.IOException;
import java.util.stream.Stream;

import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.jsglr2.integrationtest.BaseTestWithParseTableFromTermWithJSGLR1;
import org.spoofax.terms.ParseError;

public class StrategoJavaEBlockTest extends BaseTestWithParseTableFromTermWithJSGLR1 {

    public StrategoJavaEBlockTest() throws Exception {
        setupParseTable("Stratego-Java-EBlock");
    }

    @TestFactory public Stream<DynamicTest> testMetaListVarByExpectedAST() throws ParseError, IOException {
        String sampleProgram = getFileAsString("Stratego/s2j.str");
        IStrategoTerm expectedAST = getFileAsAST("Stratego/s2j.aterm");

        return testSuccessByAstString(sampleProgram, expectedAST.toString());
    }

}