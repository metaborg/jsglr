package org.spoofax.jsglr.tests.unicode;

import java.io.File;
import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;
import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.jsglr.unicode.preprocessor.SDFPrettyPrinter;
import org.spoofax.jsglr.unicode.preprocessor.UnicodeCleaner;
import org.spoofax.jsglr.unicode.preprocessor.UnicodeSDFParser;
import org.spoofax.jsglr.unicode.terms.UnicodeUtils;
import org.spoofax.jsglr.unicode.transformer.CFGrammarTransformer;

public class UnicodePreprocessorTest {

	private final File UNICODE_EXAMPLE_SDF = new File("tests/grammars/basic/Unicode.sdf");
	private final File UNICODE_EXAMPLE2_SDF = new File("tests/grammars/basic/Unicode2.sdf");
	
	@Test
	public void testUnicodeCleaner() throws IOException {
		File file = UnicodeCleaner.removeUnicode(UNICODE_EXAMPLE_SDF);
		String text = UnicodeUtils.readFile(file, null);
		text = text.replaceAll("[\n,\t]+", " ");
		Assert.assertEquals("module Unicode " +
				"exports context-free syntax "+
				"Hallo \"sd\\u(2f9f4)dssdf\" Hallo2 -> Hallo "+
				"[a-\\u(3c6)] Word -> SpecialWord " +
				"lexical syntax "+
				"~[a-z] -> Word", text);
		file.delete();
	}
	
	@Test
	public void testUnicodeSDFParser() throws Exception {
		File file = UnicodeCleaner.removeUnicode(UNICODE_EXAMPLE2_SDF);
		System.out.println(UnicodeUtils.readFile(file, null));
		System.out.println(UnicodeSDFParser.parseUnicodeSDF(file));
		file.delete();
	}
	
	@Test
	public void testCFGrammarTransformer() throws Exception {
		File file = UnicodeCleaner.removeUnicode(UNICODE_EXAMPLE2_SDF);
		IStrategoTerm term = UnicodeSDFParser.parseUnicodeSDF(file);
		CFGrammarTransformer transformer = new CFGrammarTransformer();
		IStrategoTerm result = transformer.transform(term);
		System.out.println(result);
		file.delete();
	}
	
	@Test
	public void testSDFPrettyPrinter2() throws Exception {
		File file = UnicodeCleaner.removeUnicode(UNICODE_EXAMPLE2_SDF);
		IStrategoTerm term = UnicodeSDFParser.parseUnicodeSDF(file);
		CFGrammarTransformer transformer = new CFGrammarTransformer();
		IStrategoTerm result = transformer.transform(term);
		System.out.println(result);
		SDFPrettyPrinter pp = new SDFPrettyPrinter();
		System.out.println(pp.prettyPrintSDF(result));
		file.delete();
	}
	
}
