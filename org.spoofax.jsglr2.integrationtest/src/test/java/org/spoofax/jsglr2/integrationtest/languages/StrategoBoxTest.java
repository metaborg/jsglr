package org.spoofax.jsglr2.integrationtest.languages;

import java.io.IOException;
import java.util.stream.Stream;

import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.jsglr2.integrationtest.BaseTestWithParseTableFromTermWithJSGLR1;
import org.spoofax.terms.ParseError;

public class StrategoBoxTest extends BaseTestWithParseTableFromTermWithJSGLR1 {

    public StrategoBoxTest() throws Exception {
        setupParseTable("Stratego-Box");
    }

    @TestFactory public Stream<DynamicTest> testMixBoxByExpectedAST() throws ParseError, IOException {
        String sampleProgram = getFileAsString("Stratego/mix-syntax-box.str");
        IStrategoTerm expectedAST = getFileAsAST("Stratego/mix-syntax-box.aterm");

        return testSuccessByAstString(sampleProgram, expectedAST.toString());
    }

}