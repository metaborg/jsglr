package org.spoofax.interpreter.library.jsglr.treediff;

import java.util.ArrayList;
import org.spoofax.interpreter.terms.IStrategoTerm;

/**
 * Constructs a (symmetric) matching relation between terms in AST1 and AST2.
 * The matching is stored as attachments to matched terms.
 * @author maartje
 *
 */
public abstract class AbstractTreeMatcher {
	
	private LCS<IStrategoTerm> lcs;
	private boolean tryMatchingMovedTerms;
	
	public AbstractTreeMatcher(LCSCommand<IStrategoTerm> lcsCommand, boolean tryMatchingMovedTerms) {
		lcs = new LCS<IStrategoTerm>(lcsCommand);
		this.tryMatchingMovedTerms = tryMatchingMovedTerms;
	}
	
	/**
	 * Constructs a symmetric (partial) matching between terms in term1 with terms in trm2
	 * @param trm1 AST1
	 * @param trm2 AST2
	 * @param tryMatchingMovedTerms Set this argument to true if the diff between AST1 and AST2
	 * may contain code fragments that are moved to another location
	 */
	public void constructMatching(IStrategoTerm root1, IStrategoTerm root2) {
		HelperFunctions.setParentAttachments(root1);
		HelperFunctions.setParentAttachments(root2);
		matchLeafnodesUsingLCS(root1, root2);
		matchTerminalsBottomUp(root1, root2);
		matchTermsTopdown(root1, root2);
	}

	/**
	 * Matches the leaf nodes that form the longest common subsequence 
	 * given a certain match criterion
	 * @param trm1
	 * @param trm2
	 * @param tryMatchingMovedTerms True means that a new LCS procedure tries to match unmatched terms
	 */
	private void matchLeafnodesUsingLCS(IStrategoTerm trm1, IStrategoTerm trm2) {
		ArrayList<IStrategoTerm> leafNodes1 = HelperFunctions.collectLeafnodes(trm1);
		ArrayList<IStrategoTerm> leafNodes2 = HelperFunctions.collectLeafnodes(trm2);
		lcs.createLCSResultsOptimized(leafNodes1, leafNodes2);
		//lcs.createLCSResults(leafNodes1, leafNodes2);
		for (int i = 0; i < lcs.getLCSSize(); i++) {
			TermMatchAttachment.forceMatchTerms(lcs.getResultLCS1().get(i), lcs.getResultLCS2().get(i));
		}
		if(tryMatchingMovedTerms){
			lcs.createLCSResults(lcs.getResultUnmatched1(), lcs.getResultUnmatched2());
			for (int i = 0; i < lcs.getLCSSize(); i++) {
				TermMatchAttachment.forceMatchTerms(lcs.getResultLCS1().get(i), lcs.getResultLCS2().get(i));
			}
		}
	}


	/**
	 * Returns all candidate matches in root1 for term t2 in root2. 
	 * This function is called during a bottom-up traversal, e.g., the sub-terms of t2 are already matched (the parent is not).
	 * Typical implementations may follow sub term links or use origin-tracking information.
	 * @param root1
	 * @param t2
	 * @return
	 */
	abstract ArrayList<IStrategoTerm> getCandidateMatchTerms(IStrategoTerm root1, IStrategoTerm t2);

	/**
	 * Calculates a matching score between t1 and t2. 
	 * This function is first called during a bottom-up traversal, e.g. the sub-terms of t2 are already matched (the parent is not).
	 * Then it is called a second time during a topdown traversal, now the parent term as well as the child terms are (possible) matched.
	 * The matching algorithm tries to match terms to the candidate term with the highest matching score.
	 * Typical implementations use the following information:
	 * 	- origin information
	 * 	- number of matched subterms
	 *  - parent nodes matched?
	 *  - structural similarity 
	 * @param t1
	 * @param t2
	 * @return Score that expresses the likeliness that t1 and t2 must be matched 
	 */
	abstract double matchingScore(IStrategoTerm t1, IStrategoTerm t2);
	
	/**
	 * Traverses all terms in t2 in bottum-up order and tries to construct a matching for those.
	 * @param root1
	 * @param t2
	 */
	private void matchTerminalsBottomUp(IStrategoTerm root1, IStrategoTerm t2) {
		for (int i = 0; i < t2.getSubtermCount(); i++) {
			matchTerminalsBottomUp(root1, t2.getSubterm(i));
		}
		tryMatchTerminalNode(root1, t2);
	}

	/**
	 * Tries to construct the optimal matching for t2.
	 * If t2 is matched to a term t1 that was matched previously,
	 * the previous match of t1 is broken and t1 is re-matched (if possible). 
	 * @param root1
	 * @param t2
	 */
	private void tryMatchTerminalNode(IStrategoTerm root1, IStrategoTerm t2){
		IStrategoTerm t1 = findBestMatch(root1, t2);
		if(t1 != null){
			matchTerminalNode(root1, t2, t1);
		}
	}

	private void matchTerminalNode(IStrategoTerm root1, IStrategoTerm t2,
			IStrategoTerm t1) {
		IStrategoTerm prev_t2 = TermMatchAttachment.getMatchedTerm(t1);
		TermMatchAttachment.forceMatchTerms(t1, t2);
		if(prev_t2 != null){
			assert TermMatchAttachment.getMatchedTerm(prev_t2) == null;
			assert matchingScore(t1, t2) > matchingScore(t1, prev_t2) : "matching structure must be improved after rematching"; //ensures termination
			tryMatchTerminalNode(root1, prev_t2);
		}
	}

	/**
	 * Find the best match for t2 based on matching score. 
	 * Effects of breaking other previous accomplished matches are taken into account.  
	 * @param root1
	 * @param t2
	 * @return
	 */
	private IStrategoTerm findBestMatch(IStrategoTerm root1, IStrategoTerm t2) {
		ArrayList<IStrategoTerm> candidates = getCandidateMatchTerms(root1, t2);
		IStrategoTerm t1 = null;
		for (int i = 0; i < candidates.size(); i++) {
			IStrategoTerm t1_candidate = candidates.get(i);
			if(isBetterCandidate(t1, t1_candidate, t2)){
				t1 = t1_candidate;
			}
		}
		return t1;
	}
	
	/**
	 * Returns true if c1 is a better candidate than prevc1, 
	 * taking into account the matching relations that are possible broken by rematching
	 * @param prevc1
	 * @param c1
	 * @param t2
	 * @return
	 */
	private boolean isBetterCandidate(IStrategoTerm prevc1, IStrategoTerm c1, IStrategoTerm t2){
		if(prevc1 == c1)
			return false; //prevc1 and c1 are the same term
		double prevc1_t2 = matchingScore(prevc1, t2);
		double c1_t2 = matchingScore(c1, t2);
		if(prevc1_t2 > c1_t2)
			return false; //prevc1 is a better match for t2
		IStrategoTerm c2 = TermMatchAttachment.getMatchedTerm(c1);
		double c1_c2 = matchingScore(c1, c2);
		if(c1_c2 >= c1_t2)
			return false; //rematching c1_t2 would break the c1-c2 match which is better (or equal).
		if(c1_t2 > prevc1_t2)
			return true; //c1_t2 is a better match than prevc1_t2 and c1_c2 
		//c1_t2 == prevc1_t2
		IStrategoTerm prevc2 = TermMatchAttachment.getMatchedTerm(prevc1);
		double prevc1_prevc2 = matchingScore(prevc1, prevc2);
		assert prevc1_prevc2 <= prevc1_t2 : "term should not be a candidate for rematching with a term that has lower score";
		return c1_c2 < prevc1_prevc2; //c1_t2 == prevc1_t2, break the match with the lowest score
	}
	
	private void matchTermsTopdown(IStrategoTerm root1, IStrategoTerm trm2) {
		IStrategoTerm trm1 = TermMatchAttachment.getMatchedTerm(trm2);
		if(HelperFunctions.haveSameSignature(trm1, trm2) && trm1.getSubtermCount() == trm2.getSubtermCount()){
			for (int i = 0; i < trm2.getSubtermCount(); i++) {
				IStrategoTerm trm2Child = trm2.getSubterm(i);
				IStrategoTerm trm1Child = trm1.getSubterm(i);
				IStrategoTerm trm2ChildPartner = TermMatchAttachment.getMatchedTerm(trm2Child);
				if(
					trm1Child != trm2ChildPartner && 
					!isBetterCandidate(trm1Child, trm2ChildPartner, trm2Child) &&
					matchingScore(trm1Child, trm2Child) > 0
				){
					//trm1Child has the same child index as trm2Child, and matched parents M(trm1,trm2)
					//Therefore, trm1child is the preferred candidate for trm2Child, 
					//unless trm2ChildPartner is better
					matchTerminalNode(root1, trm1Child, trm2Child);
				}
			}
		}
		for (int i = 0; i < trm2.getSubtermCount(); i++) {
			matchTermsTopdown(root1, trm2.getSubterm(i));
		}
	}

}
