package org.spoofax.jsglr.unicode.terms;

import java.util.Iterator;
import java.util.LinkedList;

import org.spoofax.interpreter.terms.IStrategoTerm;

public class DefaultSequenceCreator extends SequenceCreator {

	@Override
	protected IStrategoTerm createSequence(IStrategoTerm first, IStrategoTerm second) {
		return UnicodeUtils.makeSymbolSeq(first, second);
	}
	
	@Override
	protected IStrategoTerm wrapTerm(IStrategoTerm term) {
		return term;
	}
	
	@Override
	protected IStrategoTerm createSequence(Iterator<IStrategoTerm> terms) {
		this.validateIterator(terms);
		IStrategoTerm first = terms.next();
		LinkedList<IStrategoTerm> rest = new LinkedList<IStrategoTerm>();
		while (terms.hasNext()) {
			rest.add(terms.next());
		}
		return UnicodeUtils.makeSymbolSeq(first, rest);
	}
	
}
