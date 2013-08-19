package org.spoofax.jsglr.unicode.terms;

import org.spoofax.interpreter.terms.IStrategoTerm;

public class RestrictionsSequenceCreator extends SequenceCreator {

	@Override
	protected IStrategoTerm createSequence(IStrategoTerm first, IStrategoTerm second) {
		return UnicodeUtils.makeRestrictionSymbolSeq(first, second);
	}

	@Override
	protected IStrategoTerm wrapTerm(IStrategoTerm term) {
		return UnicodeUtils.makeSingle(term);
	}

}
