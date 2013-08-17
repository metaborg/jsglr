package org.spoofax.jsglr.tests.unicode;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.Arrays;

import org.junit.BeforeClass;
import org.junit.Test;
import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.jsglr.client.ParseTable;
import org.spoofax.jsglr.client.imploder.TermTreeFactory;
import org.spoofax.jsglr.client.imploder.TreeBuilder;
import org.spoofax.jsglr.io.ParseTableManager;
import org.spoofax.jsglr.io.SGLR;
import org.spoofax.jsglr.tests.unicode.interpreter.Interpreter;
import org.spoofax.jsglr.unicode.UnicodeConverter;
import org.spoofax.jsglr.unicode.UnicodeUtils;
import org.spoofax.terms.attachments.ParentTermFactory;

public class UnicodeLambdaTest {

	private static ParseTableManager parseTableManager;
	private static ParseTable unicodeLambdaParseTable;
	private static File unicodeLambdaFile;

	@BeforeClass
	public static void initializeParseTable() throws Exception {
		parseTableManager = new ParseTableManager();
		unicodeLambdaParseTable = parseTableManager.loadFromFile("tests/grammars/basic/UnicodeLambdaLanguage.tbl");
		unicodeLambdaFile = new File("tests/data/UnicodeLambdaTest.txt");
	}
	
	@Test
	public void testUnicodeLambda() throws Exception{
		for (char c : UnicodeConverter.encodeUnicodeToAscii("蜨").toCharArray()) {
			System.out.print((int)c + " ");
		}
		System.out.println();
		SGLR sglr = new SGLR(new TreeBuilder(new TermTreeFactory(new ParentTermFactory(unicodeLambdaParseTable.getFactory())),
				true), unicodeLambdaParseTable);
		String content = UnicodeUtils.readFile(unicodeLambdaFile, Charset.forName("UTF-8"));
		/**/
		IStrategoTerm term = (IStrategoTerm) sglr.parse(content, null, null, true);
		
		System.out.println(term.toString());
		Interpreter.eval(term);
	}
	
	public static void writeToFile(File file, String content) throws IOException{
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF-8"));
		writer.append(content);
		writer.flush();
		writer.close();
	}

}
