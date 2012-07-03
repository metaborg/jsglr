package org.spoofax.interpreter.library.jsglr.treediff;

import java.util.ArrayList;

import org.spoofax.interpreter.terms.IStrategoTerm;

/** Constructs a symmetic (partial) matching between terms of AST1 and AST2
 * based on the origin relation and heuristics.
 * Origin based matches are always prefered over heuristic matches
 * @author maartje
 *
 */
public class OriginHeuristicTreeMatcher extends AbstractTreeMatcher {
		
	private OriginTreeMatcher originTreeMatcher;
	private HeuristicTreeMatcher heuristicMatcher;
	
	public OriginHeuristicTreeMatcher(boolean useDesugaredOrigins, boolean requireSameSignature, boolean requireSameValue){
		//super(new LCSOriginTermsCommand(), true);
		super(new LCSEqualTermsCommand<IStrategoTerm>(), true);
		originTreeMatcher = new OriginTreeMatcher(useDesugaredOrigins, requireSameSignature, requireSameValue);
		heuristicMatcher = new HeuristicTreeMatcher(requireSameSignature, requireSameValue, false);
	}

	@Override
	ArrayList<IStrategoTerm> getCandidateMatchTerms(IStrategoTerm root1, IStrategoTerm t2) {
		ArrayList<IStrategoTerm> originCandidates = originTreeMatcher.getCandidateMatchTerms(root1, t2);
		if(originCandidates.size()>0)
			return originCandidates;
		return heuristicMatcher.getCandidateMatchTerms(root1, t2);
	}

	@Override
	double matchingScore(IStrategoTerm t1, IStrategoTerm t2) {
		//origin-based matches are prefered over heuristic matches
		return (3*originTreeMatcher.matchingScore(t1, t2) + heuristicMatcher.matchingScore(t1, t2))/4.0;
	}
}
