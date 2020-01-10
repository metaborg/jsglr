package org.spoofax.jsglr2.integrationtest.languages;

import java.io.IOException;
import java.util.stream.Stream;

import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.jsglr2.integrationtest.BaseTestWithParseTableFromTermWithJSGLR1;
import org.spoofax.terms.ParseError;

public class StrategoWebDSLTest extends BaseTestWithParseTableFromTermWithJSGLR1 {

    public StrategoWebDSLTest() throws Exception {
        setupParseTable("StrategoWebDSL");
    }

    @TestFactory public Stream<DynamicTest> testAmbByExpectedAST() throws ParseError, IOException {
        String sampleProgram = getFileAsString("Stratego/java-transformations.str");
        IStrategoTerm expectedAST = getFileAsAST("Stratego/java-transformations.aterm");

        return testSuccessByAstString(sampleProgram, expectedAST.toString());
    }

    @TestFactory public Stream<DynamicTest> testDeriveCrud() throws ParseError, IOException {
        String sampleProgram = getFileAsString("StrategoWebDSL/webdsl-src-org-webdsl-dsl-modules-derive-crud.str");

        return testSuccessByJSGLR1(sampleProgram);
    }


}