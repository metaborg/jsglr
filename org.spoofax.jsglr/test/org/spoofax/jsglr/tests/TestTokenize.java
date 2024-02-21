package org.spoofax.jsglr.tests;

import static jsglr.shared.ImploderAttachment.getLeftToken;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;

import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.jsglr.client.imploder.TreeBuilder;

import jsglr.shared.IToken;
import jsglr.shared.ITokenizer;
import jsglr.shared.ITokens;

/**
 * @author Lennart Kats <lennart add lclnet.nl>
 */
public class TestTokenize extends ParseTestCase {

	@Override
	protected void gwtSetUp() throws Exception {
        super.gwtSetUp("Java-15", "java");
        sglr.setTreeBuilder(new TreeBuilder());
        sglr.setUseStructureRecovery(true);
        doCompare = false;
	}

    public void testJava5() throws FileNotFoundException, IOException {
    	IStrategoTerm parsed = doParseTest("java5");
    	ITokenizer tokenizer = (ITokenizer) getLeftToken(parsed).getTokenizer();
    	Iterator<IToken> tokens = tokenizer.iterator();
    	System.out.println(tokenizer);
    	
    	assertEquals(1, tokens.next().getLine());
    	IToken packageToken = getNonEmptyToken(tokens);
    	assertEquals("package", packageToken.toString());
    	assertEquals("package", packageToken.toString());
    	assertEquals(1, packageToken.getLine());
    	assertEquals(" ", getNonEmptyToken(tokens).toString());
    	assertEquals("java", getNonEmptyToken(tokens).toString());
    	assertEquals(".", getNonEmptyToken(tokens).toString());
    	assertEquals("java5", getNonEmptyToken(tokens).toString());
    	assertEquals(";", getNonEmptyToken(tokens).toString());
    	assertEquals("\n", getNonEmptyToken(tokens).toString());
    	assertEquals("\n", getNonEmptyToken(tokens).toString());
    	IToken classToken = getNonEmptyToken(tokens);
    	IToken classToken2 = getNonEmptyToken(tokens);
    	System.out.println(classToken2.getLine());
    	assertEquals("class", classToken.toString());
    	assertEquals(IToken.Kind.TK_KEYWORD, classToken.getKind());
    	assertEquals(3, classToken.getLine());
    	assertEquals(8, tokenizer.getTokenAt(tokenizer.getTokenCount() - 1).getLine());
    }

	private static IToken getNonEmptyToken(Iterator<IToken> tokens) {
		IToken token;
		do {
			token = tokens.next();
		} while (token.getEndOffset() < token.getStartOffset());
		return token;
		
	}
    
    public void testJava6() throws FileNotFoundException, IOException {
    	suffix = "java.recover";
    	IStrategoTerm parsed = doParseTest("java6");
    	ITokens tokenizer = getLeftToken(parsed).getTokenizer();
    	Iterator<IToken> tokens = tokenizer.iterator();
    	System.out.println(tokenizer);
    	
    	while (!getNonEmptyToken(tokens).toString().equals("the"));
    	
    	IToken token = getNonEmptyToken(tokens);
    	assertEquals(" ", token.toString());
    	assertEquals(IToken.Kind.TK_ERROR, token.getKind());
    	token = getNonEmptyToken(tokens);
    	assertEquals("int", token.toString());
    	assertEquals(IToken.Kind.TK_ERROR_KEYWORD, token.getKind());
    	token = getNonEmptyToken(tokens);
    	assertEquals(" ", token.toString());
    	assertEquals(IToken.Kind.TK_ERROR, token.getKind());
    	token = getNonEmptyToken(tokens);
    	assertEquals("bar", token.toString());
    	assertEquals(IToken.Kind.TK_ERROR, token.getKind());
    }

}
