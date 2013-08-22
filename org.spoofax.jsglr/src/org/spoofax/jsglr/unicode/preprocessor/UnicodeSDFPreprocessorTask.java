package org.spoofax.jsglr.unicode.preprocessor;

import java.io.File;

/**
 * The {@link UnicodeSDFPreprocessorTask} integrates
 * {@link UnicodeSDFPreprocessor} to Ant.<br>
 * The task has to attributes: onlysdfufiles, which specifies, whether onlt
 * files with the sdfu extension are processed (default true), and encoding,
 * which specifies the encoding of the file (default UTF-8). File are added with
 * an nested fileset.
 * 
 * @author moritzlichter
 * 
 */
public class UnicodeSDFPreprocessorTask {

	public static void main(String[] args) throws Exception {

		if (args.length != 2) {
			throw new IllegalArgumentException("Expected two arguments. First file, second encoding");
		}
		
		File file = new File(args[0]);
		// Preprocess the file
		try {
			System.out.println("Unicode Preprocessing file: " + file);
			// UnicodeSDFPreprocessor.preprocessFile(file, encoding);
			UnicodePreprocessor.preprocessUnicodeSDF(file, args[1]);
		} catch (Exception e) {
			System.out.println("Failed.");
			throw e;
		}

	}

}
