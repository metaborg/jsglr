package org.spoofax.jsglr.unicode.transformer;

import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.jsglr.unicode.terms.UnicodeUtils;
import org.spoofax.terms.TermVisitor;

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
		if (UnicodeUtils.isConstructors(term, ProductionAST.UNICODE_CONS)) {
			this.containsUnicode = true;
		}
	}

}
