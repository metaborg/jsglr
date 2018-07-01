package org.spoofax.jsglr2.tests.languages;

import java.io.IOException;

import org.junit.Test;
import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.jsglr.client.InvalidParseTableException;
import org.spoofax.jsglr2.parsetable.ParseTableReadException;
import org.spoofax.jsglr2.tests.util.BaseTestWithJSGLR1;
import org.spoofax.jsglr2.util.WithParseTable;
import org.spoofax.terms.ParseError;

/**
* With these tests we compare against the AST produced by the parse-stratego tool.
* This tool uses an older imploder called asfix, written in Stratego.
* Asfix is more complete and bug-free than the JSGLR1 imploder in Java, so we don't compare against JSGLR1
*/
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
    public void testMixBoxByExpectedAST() throws ParseError, ParseTableReadException, IOException, InvalidParseTableException {
        setupParseTable("Stratego-Box");
        String sampleProgram = getFileAsString("Stratego/mix-syntax-box.str");
        IStrategoTerm expectedAST = getFileAsAST("Stratego/mix-syntax-box.aterm");

        testSuccessByAstString(sampleProgram, expectedAST.toString());
    }

    @Test
    public void testMixSugarByExpectedAST() throws ParseError, ParseTableReadException, IOException, InvalidParseTableException {
        setupParseTable("Stratego-Sugar-in-Stratego");
        String sampleProgram = getFileAsString("Stratego/mix-syntax-sugar.str");
        IStrategoTerm expectedAST = getFileAsAST("Stratego/mix-syntax-sugar.aterm");

        testSuccessByAstString(sampleProgram, expectedAST.toString());
    }

    @Test
    public void testMetaListVarByExpectedAST() throws ParseError, ParseTableReadException, IOException, InvalidParseTableException {
        setupParseTable("Stratego-Java-15");
        String sampleProgram = getFileAsString("Stratego/meta-listvar.str");
        IStrategoTerm expectedAST = getFileAsAST("Stratego/meta-listvar.aterm");

        testSuccessByAstString(sampleProgram, expectedAST.toString());
    }

}
