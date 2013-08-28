package org.spoofax.jsglr.unicode.preprocessor;

import java.io.File;

import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.jsglr.unicode.terms.UnicodeUtils;
import org.spoofax.jsglr.unicode.transformer.CFGrammarTransformer;

public class UnicodeSDFPreprocessor {

	public static File preprocessUnicodeSDF(File unicodeSDF, String encoding) throws Exception{
		File file = UnicodeCleaner.removeUnicode(unicodeSDF, encoding);
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
	//	AnotationPrettyPrinter annoPP = new AnotationPrettyPrinter();
	//	unicodeCleanSDF = annoPP.prettyPrintAnnotations(unicodeCleanSDF);
		UnicodeUtils.writeFile(unicodeCleanSDF, destFile);
		return destFile;
	}
	
	public static void main(String[] args) throws Exception {
		
		if (args.length != 2) {
			System.out.println("Expected two arguments. First file, second encoding");
			throw new IllegalArgumentException("Expected two arguments. First file, second encoding");
		}
		
		File file = new File(args[0]);
		// Preprocess the file
		try {
			System.out.println("Unicode Preprocessing file: " + file);
			// UnicodeSDFPreprocessor.preprocessFile(file, encoding);
			UnicodeSDFPreprocessor.preprocessUnicodeSDF(file, args[1]);
		} catch (Exception e) {
			System.out.println("Failed.");
			throw e;
		}

	}


}
