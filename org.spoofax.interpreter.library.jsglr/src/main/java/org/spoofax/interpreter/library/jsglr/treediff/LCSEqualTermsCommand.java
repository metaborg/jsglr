package org.spoofax.interpreter.library.jsglr.treediff;

import org.spoofax.interpreter.terms.IStrategoTerm;

/**
 * Basic matching criterion: two terms can be matched iff they are equal
 * @author maartje
 *
 * @param <T>
 */
public class LCSEqualTermsCommand<T> implements LCSCommand<IStrategoTerm> {

	/**
	 * True iff t1 and t2 are equal
	 */
	public boolean isMatch(IStrategoTerm t1, IStrategoTerm t2) {
		return t1 == t2 || t1.equals(t2);
	}

}
