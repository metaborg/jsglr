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

public class UnicodeMathExpressionsTest {

	private static ParseTable parseTable;
	private static SGLR sglr;
	
	private static File file1;
	
	@BeforeClass
	public static void initializeParseTable()  {
		try {
		ParseTableManager parseTableManager = new ParseTableManager();
		parseTable = parseTableManager.loadFromFile("tests/grammars/basic/UnicodeMathExpressions.tbl");
		file1 = new File("tests/data/UnicodeMathExpressions1.txt");
		sglr = new SGLR(new TreeBuilder(), parseTable);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testSimpleExpressions() throws Exception{
		IStrategoTerm ast = (IStrategoTerm) sglr.parse(UnicodeUtils.readFile(file1, Charset.forName("UTF-8")), null, null);
		System.out.println(ast);
	}
}
