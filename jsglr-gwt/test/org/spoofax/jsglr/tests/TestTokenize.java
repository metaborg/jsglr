package org.spoofax.jsglr.tests;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;

import org.spoofax.jsglr.client.imploder.IToken;
import org.spoofax.jsglr.client.imploder.ITokenizer;
import org.spoofax.jsglr.client.imploder.TreeBuilder;
import org.spoofax.jsglr.shared.terms.ATerm;

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
    	ATerm parsed = doParseTest("java5");
    	ITokenizer tokenizer = parsed.getLeftToken().getTokenizer();
    	Iterator<IToken> tokens = tokenizer.iterator();
    	System.out.println(tokenizer);
    	
    	assertEquals(0, tokens.next().getLine());
    	IToken packageToken = tokens.next();
    	assertEquals("package", packageToken.toString());
    	assertEquals(0, packageToken.getLine());
    	assertEquals(" ", tokens.next().toString());
    	assertEquals("java", tokens.next().toString());
    	assertEquals(".", tokens.next().toString());
    	assertEquals("java5", tokens.next().toString());
    	assertEquals(";", tokens.next().toString());
    	assertEquals("\n", tokens.next().toString());
    	assertEquals("\n", tokens.next().toString());
    	IToken classToken = tokens.next();
    	IToken classToken2 = tokens.next();
    	System.out.println(classToken2.getLine());
    	assertEquals("class", classToken.toString());
    	assertEquals(IToken.TK_KEYWORD, classToken.getKind());
    	assertEquals(2, classToken.getLine());
    }
    
    public void testJava6() throws FileNotFoundException, IOException {
    	suffix = "java.recover";
    	ATerm parsed = doParseTest("java6");
    	ITokenizer tokenizer = parsed.getLeftToken().getTokenizer();
    	Iterator<IToken> tokens = tokenizer.iterator();
    	System.out.println(tokenizer);
    	
    	while (!tokens.next().toString().equals("the"));
    	
    	IToken token = tokens.next();
    	assertEquals(" ", token.toString());
    	assertEquals(IToken.TK_ERROR, token.getKind());
    	token = tokens.next();
    	assertEquals("int", token.toString());
    	assertEquals(IToken.TK_ERROR_KEYWORD, token.getKind());
    	token = tokens.next();
    	assertEquals(" ", token.toString());
    	assertEquals(IToken.TK_ERROR, token.getKind());
    	token = tokens.next();
    	assertEquals("bar", token.toString());
    	assertEquals(IToken.TK_ERROR, token.getKind());
    }

}
