package org.spoofax.interpreter.library.jsglr.treediff;

import java.util.ArrayList;

import org.spoofax.interpreter.terms.IStrategoTerm;

public class LeafnodeTreeMatcher extends AbstractTreeMatcher {

	public LeafnodeTreeMatcher(LCSCommand<IStrategoTerm> lcsCommand, boolean tryMatchingMovedTerms) {
		super(lcsCommand, tryMatchingMovedTerms);
		// TODO Auto-generated constructor stub
	}

	@Override
	ArrayList<IStrategoTerm> getCandidateMatchTerms(IStrategoTerm root1, IStrategoTerm t2) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	double matchingScore(IStrategoTerm t1, IStrategoTerm t2) {
		// TODO Auto-generated method stub
		return 0;
	}

}
