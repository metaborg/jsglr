package org.spoofax.interpreter.library.jsglr.treediff;

import java.util.ArrayList;
import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.terms.attachments.ParentAttachment;

public class HeuristicTreeMatcher extends AbstractTreeMatcher {
	
	public HeuristicTreeMatcher(boolean requireSameSignature, boolean requireSameValue){
		this.requireSameSignature = requireSameSignature;
		this.requireSameValue = requireSameValue;
	}
	
	private boolean requireSameSignature;
	private boolean requireSameValue;
	
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
