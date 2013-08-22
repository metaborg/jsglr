package org.spoofax.jsglr.unicode.preprocessor;

import java.io.File;

import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.jsglr.client.SGLR;
import org.spoofax.jsglr.client.imploder.TreeBuilder;
import org.spoofax.jsglr.io.ParseTableManager;
import org.spoofax.jsglr.unicode.terms.UnicodeUtils;

public class UnicodeSDFParser {

	private static SGLR unicodeSDFParser = null;
	
	static {
		try {
			ParseTableManager manager = new ParseTableManager();
			unicodeSDFParser = new SGLR(new TreeBuilder(), manager.loadFromStream(UnicodeSDFParser.class.getResourceAsStream("UnicodeSDF.tbl")));
		} catch (Exception e) {
			throw new RuntimeException("Failed to initialize UnicodeSDFParser" ,e);
		}
	}
	
	public static IStrategoTerm parseUnicodeSDF(File file) throws Exception {
		return (IStrategoTerm) unicodeSDFParser.parse(UnicodeUtils.readFile(file, null), file.getName(), null);
	}

}
