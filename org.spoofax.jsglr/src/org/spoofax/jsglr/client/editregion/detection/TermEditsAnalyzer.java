package org.spoofax.jsglr.client.editregion.detection;

import static org.spoofax.jsglr.client.imploder.ImploderAttachment.getLeftToken;
import static org.spoofax.jsglr.client.imploder.ImploderAttachment.getRightToken;

import java.util.ArrayList;
import java.util.IdentityHashMap;

import org.spoofax.interpreter.terms.IStrategoTerm;

/**
 * Analyzes the terms in the correct AST that are affected during editing and therefore (possible) damaged.
 * Constructs a recovery based on discarding tokens associated to erroneous (discardable) terms. 
 * @author Maartje de Jonge
 *
 */
public class TermEditsAnalyzer {
	private final IStrategoTerm correctAST;
	private final ArrayList<Integer> offsetsDeletedChars;
	private final IdentityHashMap<IStrategoTerm, RecoverInterpretation> recoveryLookup;

	/**
	 * Constructs a recovery based on discarding tokens associated to erroneous (discardable) terms. 
	 */
	public TermEditsAnalyzer(ArrayList<Integer> offsetsDeletedChars, IStrategoTerm correctAST){
		this.offsetsDeletedChars = offsetsDeletedChars;
		this.recoveryLookup = new IdentityHashMap<IStrategoTerm, RecoverInterpretation>();
		this.correctAST = correctAST;
	}
	
	/**
	 * Returns discardable regions that are edited 
	 */
	public RecoverInterpretation getDiscardRecovery(){
		//System.out.println(correctAST);
		recoveryLookup.clear();
		collectRecoveries(correctAST, null);
		RecoverInterpretation discardRecovery = lookupRecovery(correctAST, null); //TODO: if null, alltd look for regions in subterms
		assert discardRecovery == null || discardRecovery.getTerm() == correctAST;
		return discardRecovery;
	}

	private void collectRecoveries(IStrategoTerm term, IStrategoTerm parent){
		if(!hasDeletions(term)){
			RecoverInterpretation originalTermRecovery = RecoverInterpretation.createOriginalTermInterpretation(term, parent);
			assert recoveryLookup.get(term) == null;
			recoveryLookup.put(term, originalTermRecovery); //we do not discard in unaffected terms.
			return;
		}
		for (int i = 0; i < term.getSubtermCount(); i++) {
			collectRecoveries(term.getSubterm(i), term);
		}
		RecoverInterpretation recovery = constructMinimalCostRecovery(term, parent);		
		recoveryLookup.put(term, recovery);
	}
	
	private RecoverInterpretation constructMinimalCostRecovery(IStrategoTerm term, IStrategoTerm parent) {
		//candidates can be null
		RecoverInterpretation candidate1 = constructRepairSubtermsRecovery(term, parent);
		if(!hasAssociatedDeletions(term) && term.isList()){
			return candidate1;
		}
		RecoverInterpretation candidate2 = constructReplaceBySubtermsRecovery(term, parent);
		RecoverInterpretation candidate3 = constructDiscardRecovery(term, parent);
		ArrayList<RecoverInterpretation> candidates = new ArrayList<RecoverInterpretation>();
		candidates.add(candidate1);
		candidates.add(candidate2);
		candidates.add(candidate3);
		return getMinimumCostRecovery(candidates);
	}
	
	private RecoverInterpretation constructRepairSubtermsRecovery(IStrategoTerm term, IStrategoTerm parent) {
		if(hasAssociatedDeletions(term))
			return null; //term itself is broken thus it can not be recovered by repairing subterms
		ArrayList<RecoverInterpretation> subtermRecoveries = new ArrayList<RecoverInterpretation>();
		for (int i = 0; i < term.getSubtermCount(); i++) {
			IStrategoTerm subterm = term.getSubterm(i);
			RecoverInterpretation subtermRecovery = lookupRecovery(subterm, term);
			if(subtermRecovery == null){
				return null; //subterm can not be recovered, thus term can not be recovered from its subterms
			}
			assert subterm == subtermRecovery.getTerm();
			subtermRecoveries.add(subtermRecovery);
		}
		return RecoverInterpretation.createRepairSubtermsInterpretation(term, parent, subtermRecoveries);
	}
	
	private RecoverInterpretation constructDiscardRecovery(IStrategoTerm term, IStrategoTerm parent) {
		if(HelperFunctions.isSomeNode(term) || (parent != null && parent.isList())){
			return RecoverInterpretation.createDiscardInterpretation(term, parent);
		}
		return null;
	}

	private RecoverInterpretation constructReplaceBySubtermsRecovery(IStrategoTerm term, IStrategoTerm parent) {
		//System.out.println("recover: " + term);
		RecoverInterpretation recovery = getRecoveryFromSubterms(term, parent, term, parent);
		//System.out.println("recovery: " + recovery);
		return recovery;
	}

	private RecoverInterpretation getRecoveryFromSubterms(IStrategoTerm visitedTerm, IStrategoTerm visitedParent, IStrategoTerm term, IStrategoTerm parent) {
		//visited term may provide a candidate recovery
		RecoverInterpretation candidateFromVisited = lookupRecovery(visitedTerm, null);
		assert candidateFromVisited == null || candidateFromVisited.getTerm() == visitedTerm;
		if(candidateFromVisited != null && candidateFromVisited.hasSameSort(term, parent)){
			//optimal interpretation for the fragment already found, no need to further traverse subterms 
			return RecoverInterpretation.createReplaceBySubtermsInterpretation(term, parent, candidateFromVisited);
		}
		if(candidateFromVisited != null && candidateFromVisited.isUndamagedTerm()){
			if(candidateFromVisited.hasCompatibleSort(term, parent)){
				return RecoverInterpretation.createReplaceBySubtermsInterpretation(term, parent, candidateFromVisited);
			}
			//return null;
		}
		
		//traverse subterms of subterm
		ArrayList<RecoverInterpretation> candidatesFromSubterms = new ArrayList<RecoverInterpretation> ();
		for (int i = 0; i < visitedTerm.getSubtermCount(); i++) {
			IStrategoTerm subterm = visitedTerm.getSubterm(i);
			//System.out.println("visited: " + subterm);
			RecoverInterpretation subtermRecovery = getRecoveryFromSubterms(subterm, visitedTerm, term, parent);
			//System.out.println("recovery: " + subtermRecovery);
			if(subtermRecovery != null){
				candidatesFromSubterms.add(subtermRecovery);
				assert subtermRecovery.hasCompatibleSort(term, parent);
			}
		}
		
		//non-list elements: pick best
		//TODO: what if term is list, what about separators
		if(parent == null || !parent.isList()){
			ArrayList<RecoverInterpretation> candidates = new ArrayList<RecoverInterpretation>();
			if(candidateFromVisited != null && candidateFromVisited.hasCompatibleSort(term, parent)){
				candidates.add(RecoverInterpretation.createReplaceBySubtermsInterpretation(term, parent, candidateFromVisited));
			}
			for (RecoverInterpretation subRecovery : candidatesFromSubterms) {
				candidates.add(RecoverInterpretation.createReplaceBySubtermsInterpretation(term, parent, subRecovery));
			}
			RecoverInterpretation minimumCostRecovery = getMinimumCostRecovery(candidates);
			if(minimumCostRecovery != null)
				return RecoverInterpretation.createReplaceBySubtermsInterpretation(term, parent, minimumCostRecovery);
			return null;
		}
		else { //parent is list
			ArrayList<RecoverInterpretation> mergedSubtermRecoveries = new ArrayList<RecoverInterpretation>();
			for (int i = 0; i < candidatesFromSubterms.size(); i++) {
				mergedSubtermRecoveries.addAll(candidatesFromSubterms.get(i).getSubtermRecoveries());
			}
			RecoverInterpretation fromSubtermsRecovery = null;
			if(!mergedSubtermRecoveries.isEmpty()){
				fromSubtermsRecovery = RecoverInterpretation.createReplaceBySubtermsInterpretation(term, parent, mergedSubtermRecoveries);
			}
			if(candidateFromVisited != null && candidateFromVisited.hasCompatibleSort(term, parent)){
				RecoverInterpretation fromVisistedRecovery = RecoverInterpretation.createReplaceBySubtermsInterpretation(term, parent, candidateFromVisited);
				return this.getMinimumCostRecovery(fromSubtermsRecovery, fromVisistedRecovery);				
			}
			return fromSubtermsRecovery;
		}
	}

	private RecoverInterpretation lookupRecovery(IStrategoTerm visitedTerm, IStrategoTerm parentTerm) {
		if(recoveryLookup.containsKey(visitedTerm))
			return recoveryLookup.get(visitedTerm);
		if(!hasDeletions(visitedTerm))
			return RecoverInterpretation.createOriginalTermInterpretation(visitedTerm, parentTerm);
		return null;
	}

	private RecoverInterpretation getMinimumCostRecovery(ArrayList<RecoverInterpretation> recoveries) {
		RecoverInterpretation minimumCostInterpretation = null;
		for (RecoverInterpretation recovery : recoveries) {
			minimumCostInterpretation = getMinimumCostRecovery(minimumCostInterpretation, recovery);
		}
		return minimumCostInterpretation;
	}

	private RecoverInterpretation getMinimumCostRecovery(RecoverInterpretation recovery1, RecoverInterpretation recovery2) {
		if(recovery2 == null)
			return recovery1;
		if(recovery1 == null)
			return recovery2;
		if(recovery2.getRecoveryCosts() < recovery1.getRecoveryCosts()){
			return recovery2;
		}
		return recovery1;
	}



	private boolean hasDeletions(IStrategoTerm term) {
		return !getCoveredOffsets(term, this.offsetsDeletedChars).isEmpty();
	}

	private boolean hasAssociatedDeletions(IStrategoTerm term) {
		return !getAssociatedCoveredOffsets(term, this.offsetsDeletedChars).isEmpty();
	}


	private static ArrayList<Integer> getAssociatedCoveredOffsets(IStrategoTerm term, ArrayList<Integer> offsets) {
		ArrayList<Integer> associatedCoveredOffsets = getCoveredOffsets(term, offsets);
		for (int i = 0; i < term.getSubtermCount(); i++) {
			IStrategoTerm subterm = term.getSubterm(i);
			ArrayList<Integer> subtermOffsets = getCoveredOffsets(subterm, offsets);
			associatedCoveredOffsets.removeAll(subtermOffsets);
		}
		return associatedCoveredOffsets;
	}

	private static ArrayList<Integer> getCoveredOffsets(IStrategoTerm term, ArrayList<Integer> offsets) {
		ArrayList<Integer> coveredOffsets = new ArrayList<Integer>();
		int startOffset = getLeftToken(term).getStartOffset();
		int endOffset = getRightToken(term).getEndOffset();
		for (int i = 0; i < offsets.size(); i++) {
			int offset = offsets.get(i); 
			if(startOffset <= offset && offset <= endOffset){
				//covered
				coveredOffsets.add(offset);
			}
		}
		return coveredOffsets;
	}
}
