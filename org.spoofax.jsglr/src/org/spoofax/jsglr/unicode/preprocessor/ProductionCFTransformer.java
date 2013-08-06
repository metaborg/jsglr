package org.spoofax.jsglr.unicode.preprocessor;

import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.interpreter.terms.ITermFactory;
import org.spoofax.jsglr.tests.unicode.MyTermTransformer;
import org.spoofax.jsglr.unicode.UnicodeUtils;
import org.spoofax.terms.TermTransformer;

public class ProductionCFTransformer extends MyTermTransformer {

	public ProductionCFTransformer() {
		super(UnicodeUtils.factory, false);
	}

	@Override
	public IStrategoTerm preTransform(IStrategoTerm arg0) {
		if (UnicodeUtils.isLit(arg0) && UnicodeUtils.isUnicode(arg0.getSubterm(0))) {
			this.doNotRecur = true;
			return UnicodeUtils.makeLEXSymbol(arg0);
		}
		return arg0;
	}


}
