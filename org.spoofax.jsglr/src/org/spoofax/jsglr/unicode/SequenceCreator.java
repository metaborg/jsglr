package org.spoofax.jsglr.unicode;

import java.util.Arrays;
import java.util.Iterator;

import org.spoofax.interpreter.terms.IStrategoTerm;

/**
 * The {@link SequenceCreator} creates a sequence of terms by nesting them into
 * each other.
 * 
 * @author moritzlichter
 * 
 */
public abstract class SequenceCreator {

	/**
	 * Called for every term passed into the {@link SequenceCreator}. This
	 * method mey be used to wrap the term into another.
	 * 
	 * @param term
	 *            the term to be wrapped
	 * @return the wrapped term
	 */
	protected abstract IStrategoTerm wrapTerm(IStrategoTerm term);

	/**
	 * Creates a sequence unit from first and second. The second one may be
	 * result of this method.
	 * 
	 * @param first
	 *            the first term in the sequence
	 * @param second
	 *            the second term in the sequence
	 * @return the united sequence term
	 */
	protected abstract IStrategoTerm createSequence(IStrategoTerm first, IStrategoTerm second);

	public final IStrategoTerm createSequence(IStrategoTerm... terms) {
		return createSequence(Arrays.asList(terms));
	}

	public final IStrategoTerm createSequence(Iterable<IStrategoTerm> terms) {
		return createSequence(terms.iterator());
	}

	protected final void validateIterator(Iterator<IStrategoTerm> terms) {
		if (!terms.hasNext()) {
			throw new IllegalArgumentException("Cannot create a sequence from zero terms");
		}
	}

	/**
	 * Default implementation for creating a sequence of terms: Concatenate the
	 * terms in linear sequence using createSequence
	 * 
	 * @param terms
	 *            the terms to be packed into a sequence
	 * @return the sequence term
	 */
	protected IStrategoTerm createSequence(Iterator<IStrategoTerm> terms) {
		this.validateIterator(terms);
		IStrategoTerm first = wrapTerm(terms.next());
		if (terms.hasNext()) {
			IStrategoTerm second = createSequence(terms);
			return createSequence(first, second);
		} else {
			return wrapTerm(first);
		}

	}
}