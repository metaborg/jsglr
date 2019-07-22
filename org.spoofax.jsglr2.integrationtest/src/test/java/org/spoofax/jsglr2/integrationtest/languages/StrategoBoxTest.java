package org.spoofax.jsglr2.integrationtest.languages;

import java.io.IOException;

import org.junit.Test;
import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.jsglr2.integrationtest.BaseTestWithParseTableFromTermWithJSGLR1;
import org.spoofax.terms.ParseError;

public class StrategoBoxTest extends BaseTestWithParseTableFromTermWithJSGLR1 {

    public StrategoBoxTest() throws Exception {
        setupParseTable("Stratego-Box");
    }

    @Test public void testMixBoxByExpectedAST() throws ParseError, IOException {
        String sampleProgram = getFileAsString("Stratego/mix-syntax-box.str");
        IStrategoTerm expectedAST = getFileAsAST("Stratego/mix-syntax-box.aterm");

        testSuccessByAstString(sampleProgram, expectedAST.toString());
    }

}