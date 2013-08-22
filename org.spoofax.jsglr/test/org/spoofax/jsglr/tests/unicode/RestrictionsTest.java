package org.spoofax.jsglr.tests.unicode;

import java.io.File;
import java.nio.charset.Charset;

import org.junit.BeforeClass;
import org.junit.Test;
import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.jsglr.client.ParseTable;
import org.spoofax.jsglr.client.SGLR;
import org.spoofax.jsglr.client.imploder.TreeBuilder;
import org.spoofax.jsglr.io.ParseTableManager;
import org.spoofax.jsglr.unicode.terms.UnicodeUtils;

public class RestrictionsTest {

	private static ParseTable parseTable;
	private static SGLR sglr;
	
	
	@BeforeClass
	public static void initializeParseTable()  {
		try {
		ParseTableManager parseTableManager = new ParseTableManager();
		parseTable = parseTableManager.loadFromFile("tests/grammars/basic/UnicodeRestrictionsTest.tbl");
		sglr = new SGLR(new TreeBuilder(), parseTable);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testSimpleExpressions() throws Exception{
		IStrategoTerm ast = (IStrategoTerm) sglr.parse("xyza hgs", null, null, true);
		System.out.println(ast);
	}
	
	@Test
	public void testUnicodeExpressions() throws Exception{
		IStrategoTerm ast = (IStrategoTerm) sglr.parse("xyzρξa hgπs", null, null, true);
		System.out.println(ast);
	}
	
	@Test
	public void testWrongeExpressions() throws Exception{
		IStrategoTerm ast = (IStrategoTerm) sglr.parse("xyzρξ hgπs", null, null, true);
		System.out.println(ast);
	}
}
