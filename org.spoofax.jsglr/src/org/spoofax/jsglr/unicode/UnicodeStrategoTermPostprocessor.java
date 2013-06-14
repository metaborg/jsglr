package org.spoofax.jsglr.unicode;

import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.interpreter.terms.TermConverter;

/**
 * The {@link UnicodeStrategoTermPostprocessor} postprocesses the resulting term
 * trees from pasring a unicode file to contains the usual unicode characters
 * again.
 * 
 * @author moritzlichter
 * 
 */
public class UnicodeStrategoTermPostprocessor {

	/**
	 * Postprocesses the resulting IStrategoTerm from pasring a unicode file.
	 * After parsing the term contains the encoded unicode character. This
	 * method modifies the term tree to contains unicode character again. The
	 * tree is rebuild because the contents connot be modified.
	 * 
	 * @param term
	 *            the input parse tree which contains encoded unicode to ascii
	 * @return a new term which contains the decoded string again with usuall
	 *         unicode characters.
	 */
	public static IStrategoTerm postprocess(IStrategoTerm term) {
		// Convert the tree with a factory which decodes the strings
		TermConverter c = new TermConverter(new UnicodeTermFactory());
		return c.convert(term);
	}

}
