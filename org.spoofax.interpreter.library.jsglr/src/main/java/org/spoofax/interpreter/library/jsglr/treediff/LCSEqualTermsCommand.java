package org.spoofax.interpreter.library.jsglr.treediff;

import org.spoofax.interpreter.terms.IStrategoTerm;

public class LCSEqualTermsCommand<T> implements LCSCommand<IStrategoTerm> {

	public boolean isMatch(IStrategoTerm t1, IStrategoTerm t2) {
		return t1.equals(t2);
	}

}
