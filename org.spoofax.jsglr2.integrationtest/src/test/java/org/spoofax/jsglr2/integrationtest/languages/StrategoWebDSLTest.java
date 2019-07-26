package org.spoofax.jsglr2.integrationtest.languages;

import org.junit.Test;
import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.jsglr2.integrationtest.BaseTestWithParseTableFromTermWithJSGLR1;
import org.spoofax.terms.ParseError;
import java.io.IOException;

public class StrategoWebDSLTest extends BaseTestWithParseTableFromTermWithJSGLR1 {

    public StrategoWebDSLTest() throws Exception {
        setupParseTable("StrategoWebDSL");
    }

    @Test public void testAmbByExpectedAST() throws ParseError, IOException {
        String sampleProgram = getFileAsString("Stratego/java-transformations.str");
        IStrategoTerm expectedAST = getFileAsAST("Stratego/java-transformations.aterm");

        testSuccessByAstString(sampleProgram, expectedAST.toString());
    }


}