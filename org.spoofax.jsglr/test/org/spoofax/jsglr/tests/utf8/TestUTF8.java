package org.spoofax.jsglr.tests.utf8;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Arrays;

import org.junit.BeforeClass;
import org.junit.Test;
import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.jsglr.client.ITreeBuilder;
import org.spoofax.jsglr.client.Label;
import org.spoofax.jsglr.client.ParseTable;
import org.spoofax.jsglr.client.imploder.TermTreeFactory;
import org.spoofax.jsglr.client.imploder.TreeBuilder;
import org.spoofax.jsglr.io.ParseTableManager;
import org.spoofax.jsglr.io.SGLR;
import org.spoofax.jsglr.unicode.UnicodeConverter;
import org.spoofax.terms.attachments.ParentTermFactory;

import com.sun.tools.javac.util.Paths;

public class TestUTF8 {

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
	public void testTableContent() {
		for (int i = 0; i < simpleUTF8Table.getStateCount(); i++) {
			System.out.println(simpleUTF8Table.getState(i));
		}
	}

	@Test
	public void resrParseSimpleUTF8() throws Exception {
		SGLR sglr = new SGLR(new TreeBuilder(new TermTreeFactory(new ParentTermFactory(
				simpleUTF8Table.getFactory())), true), simpleUTF8Table);
		String content = readFile(simpleUTF8TestFile1, Charset.forName("UTF-8"));
		for (char c : content.toCharArray()) {
			System.out.println(c + " " + (int) c);
		}
		IStrategoTerm term = (IStrategoTerm) sglr.parse(content
		, null, null, true);
		System.out.println(term);
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
