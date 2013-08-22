package org.spoofax.jsglr.unicode.transformer;

import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.jsglr.unicode.terms.UnicodeUtils;
import org.spoofax.terms.TermVisitor;

/**
 * The {@link UnicodeSymbolVisitor} checks whether a given AST contains a
 * unicode node.
 * 
 * @author moritzlichter
 * 
 */
public class UnicodeSymbolVisitor extends TermVisitor {

	private boolean containsUnicode;

	public UnicodeSymbolVisitor() {
		this.reset();
	}

	public void reset() {
		this.containsUnicode = false;
	}

	public boolean isContainingUnicode() {
		return containsUnicode;
	}

	public void preVisit(IStrategoTerm term) {
		if (UnicodeUtils.isUnicode(term)) {
			this.containsUnicode = true;
		}
	}

}
