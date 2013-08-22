package org.spoofax.jsglr.unicode.transformer;

import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.interpreter.terms.ITermFactory;
import org.spoofax.jsglr.unicode.terms.UnicodeUtils;
import org.spoofax.terms.TermTransformer;

public class ProductionCFTransformer extends TermTransformer {

	public ProductionCFTransformer() {
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
