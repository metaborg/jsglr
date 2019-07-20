package org.spoofax.jsglr2.integrationtest.languages;

import java.io.IOException;

import org.junit.Ignore;
import org.junit.Test;
import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.jsglr2.integrationtest.BaseTestWithParseTableFromTermWithJSGLR1;
import org.spoofax.terms.ParseError;

public class StrategoJava15Test extends BaseTestWithParseTableFromTermWithJSGLR1 {

    public StrategoJava15Test() throws Exception {
        setupParseTable("Stratego-Java-15");
    }

    @Ignore @Test public void testMetaListVarByExpectedAST() throws ParseError, IOException {
        String sampleProgram = getFileAsString("Stratego/meta-listvar.str");
        IStrategoTerm expectedAST = getFileAsAST("Stratego/meta-listvar.aterm");

        testSuccessByAstString(sampleProgram, expectedAST.toString());
    }

}