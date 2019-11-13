package org.spoofax.jsglr2.integrationtest.languages;

import java.io.IOException;
import java.util.stream.Stream;

import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.jsglr2.integrationtest.BaseTestWithParseTableFromTermWithJSGLR1;
import org.spoofax.terms.ParseError;

public class StrategoTest extends BaseTestWithParseTableFromTermWithJSGLR1 {

    public StrategoTest() throws Exception {
        setupParseTable("Stratego");
    }

    @TestFactory public Stream<DynamicTest> testAmbByExpectedAST() throws ParseError, IOException {
        String sampleProgram = getFileAsString("Stratego/ambiguity-issue.str");
        IStrategoTerm expectedAST = getFileAsAST("Stratego/ambiguity-issue.aterm");

        return testSuccessByAstString(sampleProgram, expectedAST.toString());
    }


}