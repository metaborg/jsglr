package org.spoofax.jsglr.unicode.preprocessor;

import java.io.File;

import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.jsglr.unicode.UnicodeUtils;

public class UnicodePreprocessor {

	public static File preprocessUnicodeSDF(File unicodeSDF) throws Exception{
		File file = UnicodeCleaner.removeUnicode(unicodeSDF);
		IStrategoTerm term = UnicodeSDFParser.parseUnicodeSDF(file);
		file.delete();
		CFGrammarTransformer transformer = new CFGrammarTransformer();
		IStrategoTerm result = transformer.transform(term);
		SDFPrettyPrinter pp = new SDFPrettyPrinter();
		String unicodeCleanSDF = pp.prettyPrintSDF(result);
		File destFile;
		String name = unicodeSDF.getName();
		if (unicodeSDF.getName().endsWith(".sdfu")) {
			name = name.substring(0, name.length()-5) +".sdf";
			destFile = new File(unicodeSDF.getParentFile(), name);
		} else {
			destFile = new File(unicodeSDF.getParentFile(), name +".sdf");
		}
		AnotationPrettyPrinter annoPP = new AnotationPrettyPrinter();
		unicodeCleanSDF = annoPP.prettyPrintAnnotations(unicodeCleanSDF);
		UnicodeUtils.writeFile(unicodeCleanSDF, destFile);
		return destFile;
	}

}
