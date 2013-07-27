package org.spoofax.jsglr.tests.unicode;

import java.io.File;
import java.nio.charset.Charset;
import java.text.ParseException;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.jsglr.client.ParseTable;
import org.spoofax.jsglr.client.imploder.TermTreeFactory;
import org.spoofax.jsglr.client.imploder.TreeBuilder;
import org.spoofax.jsglr.io.ParseTableManager;
import org.spoofax.jsglr.io.SGLR;
import org.spoofax.jsglr.unicode.UnicodeSDFPreprocessor;
import org.spoofax.jsglr.unicode.UnicodeUtils;
import org.spoofax.terms.attachments.ParentTermFactory;

import sun.tools.tree.NewInstanceExpression;

public class TestUnicode {

	private static ParseTableManager parseTableManager;
	private static ParseTable simpleUTF8Table;
	private static File simpleUTF8TestFile1;

	@BeforeClass
	public static void initializeParseTable() throws Exception {
		parseTableManager = new ParseTableManager();
		simpleUTF8Table = parseTableManager.loadFromFile("tests/grammars/basic/UTF8.tbl");
		simpleUTF8TestFile1 = new File("tests/data/SimpleUTF8.txt");
	}

	@Test
	public void testParseSimpleUTF8() throws Exception {
		SGLR sglr = new SGLR(new TreeBuilder(new TermTreeFactory(new ParentTermFactory(simpleUTF8Table.getFactory())),
				true), simpleUTF8Table);
		String content = UnicodeUtils.readFile(simpleUTF8TestFile1, Charset.forName("UTF-8"));
		IStrategoTerm term = (IStrategoTerm) sglr.parse(content, null, null, true);
		Assert.assertEquals("(K(\"Øc\"),Z([\"𝄞\",\"𝄞\"]))", term.toString());
	}
	
	@Test
	public void testSplits() throws Exception{
		String input = "$Unicode([¡-⟧])";
		String result = UnicodeSDFPreprocessor.preprocess(input);
		Assert.assertEquals("[\\7](([\\0-\\0][\\161-\\255])|([\\1-\\38][\\0-\\255])|([\\39-\\39][\\0-\\231]))", result);
		input = "$Unicode([" + (char)0x3a04 + " - " + (char)0x3a45 + "])";
		result = UnicodeSDFPreprocessor.preprocess(input);
		Assert.assertEquals("[\\7](([\\58-\\58][\\4-\\69]))", result);
	}

}
