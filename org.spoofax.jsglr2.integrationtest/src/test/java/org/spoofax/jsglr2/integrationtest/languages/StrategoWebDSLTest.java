package org.spoofax.jsglr2.integrationtest.languages;

import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.jsglr2.integrationtest.BaseTestWithParseTableFromTermWithJSGLR1;
import org.spoofax.terms.ParseError;

import java.io.IOException;
import java.util.stream.Stream;

public class StrategoWebDSLTest extends BaseTestWithParseTableFromTermWithJSGLR1 {

    public StrategoWebDSLTest() throws Exception {
        setupParseTable("StrategoWebDSL");
    }

    @TestFactory public Stream<DynamicTest> testAmbByExpectedAST() throws ParseError, IOException {
        String sampleProgram = getFileAsString("Stratego/java-transformations.str");
        IStrategoTerm expectedAST = getFileAsAST("Stratego/java-transformations.aterm");

        return testSuccessByAstString(sampleProgram, expectedAST.toString());
    }


}