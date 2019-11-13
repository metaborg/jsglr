package org.spoofax.jsglr2.integrationtest.languages;

import java.io.IOException;
import java.util.stream.Stream;

import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.jsglr2.integrationtest.BaseTestWithParseTableFromTermWithJSGLR1;
import org.spoofax.terms.ParseError;

public class StrategoJava15Test extends BaseTestWithParseTableFromTermWithJSGLR1 {

    public StrategoJava15Test() throws Exception {
        setupParseTable("Stratego-Java-15");
    }

    @TestFactory public Stream<DynamicTest> testMetaListVarByExpectedAST() throws ParseError, IOException {
        String sampleProgram = getFileAsString("Stratego/meta-listvar.str");
        IStrategoTerm expectedAST = getFileAsAST("Stratego/meta-listvar.aterm");

        return testSuccessByAstString(sampleProgram, expectedAST.toString());
    }

}