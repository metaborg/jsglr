package org.spoofax.jsglr.unicode.transformer;

import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.jsglr.unicode.terms.UnicodeUtils;
import org.spoofax.terms.TermTransformer;

/**
 * The {@link ProductionLEXTransformer} wraps LEX brackets around unicode
 * literals in a given AST. This is necessary because a unicode literal may
 * expand to a sequence of char-classes, which are not allowed to contain layout
 * and should not be placed in the ASTs.
 * 
 * @author moritzlichter
 * 
 */
public class ProductionLEXTransformer extends TermTransformer {

	public ProductionLEXTransformer() {
		super(UnicodeUtils.factory, false);
	}

	@Override
	public IStrategoTerm preTransform(IStrategoTerm arg0) {
		return arg0;
	}

	@Override
	public IStrategoTerm postTransform(IStrategoTerm arg0) {
		if (UnicodeUtils.isLit(arg0) && UnicodeUtils.isUnicode(arg0.getSubterm(0))) {
			return UnicodeUtils.makeLEXSymbol(arg0);
		}
		return super.postTransform(arg0);
	}

}
