package org.spoofax.interpreter.library.jsglr.treediff;

import java.util.ArrayList;
import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.terms.attachments.ParentAttachment;

/**
 * Constructs a (symmetric) matching relation between terms in AST1 and AST2.
 * The matching is stored as attachments to matched terms.
 * The matching is constructed in three phases: 
 * 1. LCS on based on leaf nodes
 * 2. Bottom-up matching of terms to other terms based on the number of matched subterms
 * 3. improvement of the matching relation by matching terms that occur
 * at the same child index of their matched parents (with the same signature)  
 * @author maartje
 *
 */
public class HeuristicTreeMatcher extends AbstractTreeMatcher {
	
	private boolean requireSameSignature;
	private boolean requireSameValue;
	
	/**
	 * Matches terms in AST1 and AST2 by looking at common subterms
	 * @param requireSameSignature Only allows matching of non-terminals with the same signature
	 * @param requireSameValue Only allows matching of leaf nodes if they have the same value
	 */
	public HeuristicTreeMatcher(boolean requireSameSignature, boolean requireSameValue, boolean tryMatchingMovedTerms){
		super(new LCSEqualTermsCommand<IStrategoTerm>(), tryMatchingMovedTerms);
		this.requireSameSignature = requireSameSignature;
		this.requireSameValue = requireSameValue;
	}
	
	/**
	 * Collects as candidates for matching terms that are the parent of matched subterms
	 */
	ArrayList<IStrategoTerm> getCandidateMatchTerms(IStrategoTerm root1, IStrategoTerm t2) {
		ArrayList<IStrategoTerm> candidateMatches = new ArrayList<IStrategoTerm>();
		for (int i = 0; i < t2.getSubtermCount(); i++) {
			IStrategoTerm subTermMatch1 = TermMatchAttachment.getMatchedTerm(t2.getSubterm(i));
			if(subTermMatch1 != null){
				candidateMatches.add(ParentAttachment.getParent(subTermMatch1));
			}
		}
		return candidateMatches;
	}
	
	/**
	 * Calculates a weight matching score based on:
	 * 1. number of matched subterms
	 * 2. matching of the parent terms
	 * 3. same signature and/or equal
	 */
	double matchingScore(IStrategoTerm t1, IStrategoTerm t2){
		if(t1 == null || t2 == null)
			return -1;
		if(requireSameSignature && !HelperFunctions.haveSameSignature(t1, t2)){
			return -1;
		}
		if(requireSameValue && HelperFunctions.isPrimitiveType(t1) && !HelperFunctions.isPrimitiveWithSameValue(t1, t2)){
			return -1;
		}		
		double value = 0.0;
		double maxValue = 2.0 + 2.0 + 2.0 + t1.getSubtermCount() + t2.getSubtermCount();
		if(HelperFunctions.haveSameSignature(t1, t2) || HelperFunctions.isPrimitiveWithSameValue(t1, t2)){
			value += 2; //+2 for equal signatures
			if(t1.equals(t2)){
				value +=2; //+2 for equal terms
			}
		}
		IStrategoTerm parent1 = ParentAttachment.getParent(t1);
		IStrategoTerm parent2 = ParentAttachment.getParent(t2);
		if(TermMatchAttachment.getMatchedTerm(parent2) == parent1){
			value +=2; //+2 for matched parent terms (no penalty for unknown parents)
			if(parent1 != null)
				value +=1; //+1 bonus for matched parent terms 
		}
		for (int i = 0; i < t2.getSubtermCount(); i++) {
			IStrategoTerm subtermMatch = TermMatchAttachment.getMatchedTerm(t2.getSubterm(i));
			if(subtermMatch != null && ParentAttachment.getParent(subtermMatch) == t1){
				value += 2.0; //+2 for matched subterms
			}
		}
		return 1.0 * value/maxValue;
	}
}
