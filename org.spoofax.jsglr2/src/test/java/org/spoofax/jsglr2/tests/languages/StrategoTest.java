package org.spoofax.jsglr2.tests.languages;

import java.io.IOException;

import org.junit.Test;
import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.jsglr.client.InvalidParseTableException;
import org.spoofax.jsglr2.parsetable.ParseTableReadException;
import org.spoofax.jsglr2.tests.util.BaseTestWithJSGLR1;
import org.spoofax.jsglr2.util.WithParseTable;
import org.spoofax.terms.ParseError;

public class StrategoTest extends BaseTestWithJSGLR1 implements WithParseTable {

    public StrategoTest()
        throws ParseError, ParseTableReadException, IOException, InvalidParseTableException, InterruptedException {
    }

    @Test
    public void testAmbByExpectedAST() throws ParseError, ParseTableReadException, IOException, InvalidParseTableException {
        setupParseTable("Stratego");
        String sampleProgram = getFileAsString("Stratego/ambiguity-issue.str");
        IStrategoTerm expectedAST = getFileAsAST("Stratego/ambiguity-issue.aterm");

        testSuccessByAstString(sampleProgram, expectedAST.toString());
    }

    @Test
    public void testAmbByJSGLR1() throws ParseError, ParseTableReadException, IOException, InvalidParseTableException {
        setupParseTable("Stratego");
        String sampleProgram = getFileAsString("Stratego/ambiguity-issue.str");

        testSuccessByJSGLR1(sampleProgram);
    }

    @Test
    public void testMixByExpectedAST() throws ParseError, ParseTableReadException, IOException, InvalidParseTableException {
        setupParseTable("Stratego-Box");
        String sampleProgram = getFileAsString("Stratego/mix-syntax.str");
        IStrategoTerm expectedAST = getFileAsAST("Stratego/mix-syntax.aterm");

        testSuccessByAstString(sampleProgram, expectedAST.toString());
    }

    // testMixByJSGLR1 would fail because the JSGLR1 (Java-based) imploder is horrible with mix syntax

    @Test
    public void testMetaListVarByExpectedAST() throws ParseError, ParseTableReadException, IOException, InvalidParseTableException {
        setupParseTable("Stratego-Java-15");
        String sampleProgram = getFileAsString("Stratego/meta-listvar.str");
        IStrategoTerm expectedAST = getFileAsAST("Stratego/meta-listvar.aterm");

        testSuccessByAstString(sampleProgram, expectedAST.toString());
    }

    // testMetaListVarByJSGLR1 would fail without Martijn's fix. Better to just switch to JSGLR2

}