package org.spoofax.jsglr.tests.unicode;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
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
import org.spoofax.terms.attachments.ParentTermFactory;

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
	public void testUnicodeSDFPreprocessor() throws ParseException {
		String testString = "XYZABC $Unicode(Ø,∀) HIJKLMNO $Unicode(∀) $Unicode(∀-水,𝄞) $Unicode(𝄞)";
		String result = UnicodeSDFPreprocessor.preprocess(testString);
		Assert.assertEquals(
				"XYZABC [\\7]([\\0][\\216]|[\\34][\\0]) HIJKLMNO [\\7]([\\34][\\0]) [\\7](([\\34-\\108][\\0-\\52])|([\\216][\\52][\\221][\\30])) [\\7]([\\216][\\52][\\221][\\30])",
				result);
	}

	@Test
	public void testParseSimpleUTF8() throws Exception {
		SGLR sglr = new SGLR(new TreeBuilder(new TermTreeFactory(new ParentTermFactory(simpleUTF8Table.getFactory())),
				true), simpleUTF8Table);
		String content = readFile(simpleUTF8TestFile1, Charset.forName("UTF-8"));
		IStrategoTerm term = (IStrategoTerm) sglr.parse(content, null, null, true);
		Assert.assertEquals("(K(\"Øc\"),Z([\"𝄞\",\"𝄞\"]))", term.toString());
	}

	private static String readFile(File path, Charset encoding) throws IOException {
		BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(path), encoding));
		StringBuilder builder = new StringBuilder();
		String temp = in.readLine();
		while (temp != null) {
			builder.append(temp);
			temp = in.readLine();
			if (temp != null) {
				builder.append("\n");
			}
		}
		in.close();
		return builder.toString();
	}

}
