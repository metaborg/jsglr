package org.spoofax.jsglr2.integrationtest.languages;

import java.io.IOException;
import java.util.stream.Stream;

import org.junit.jupiter.api.Disabled;
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

    @Disabled("The {indentpadding} attribute is not supported by JSGLR2 imploding due to concerns around incremental parsing")
    @TestFactory public Stream<DynamicTest> testIndentPadding() throws ParseError, IOException {
        String sampleProgram = getFileAsString("Stratego/test112.str");
        IStrategoTerm expectedAST = getFileAsAST("Stratego/test112.aterm");

        return testSuccessByAstString(sampleProgram, expectedAST.toString());
    }


}