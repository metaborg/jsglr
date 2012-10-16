package org.spoofax.jsglr.client.editregion.detection;

import static org.spoofax.jsglr.client.imploder.ImploderAttachment.getLeftToken;
import static org.spoofax.jsglr.client.imploder.ImploderAttachment.getRightToken;

import java.util.ArrayList;
import java.util.IdentityHashMap;

import org.spoofax.interpreter.terms.IStrategoList;
import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.interpreter.terms.ITermFactory;
import org.spoofax.jsglr.client.imploder.TermTreeFactory;
import org.spoofax.terms.TermFactory;

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
		//int s = ImploderAttachment.getLeftToken(term).getStartOffset();
		//int e =ImploderAttachment.getRightToken(term).getEndOffset();
		//System.out.println(ImploderAttachment.getTokenizer(term).getInput().substring(s, e+1));
		RecoverInterpretation recovery = constructMinimalCostRecovery(term, parent);
		if(recovery != null)
			recoveryLookup.put(term, recovery);
	}
	
	private RecoverInterpretation constructMinimalCostRecovery(IStrategoTerm term, IStrategoTerm parent) {
		//candidates can be null
		RecoverInterpretation discardRecovery = constructDiscardRecovery(term, parent);
		RecoverInterpretation childTermsRecovery = null;
		if(!term.isList())
			childTermsRecovery = constructRepairSubtermsRecovery(term, parent);
		else {
			childTermsRecovery = sublistRecovery(0, term, parent); 
			if(childTermsRecovery != null)
				return childTermsRecovery;
			return discardRecovery;
		}
		RecoverInterpretation subTermsRecovery = constructReplaceBySubtermsRecovery(term, parent);
		ArrayList<RecoverInterpretation> candidates = new ArrayList<RecoverInterpretation>();
		candidates.add(childTermsRecovery);
		candidates.add(subTermsRecovery);
		candidates.add(discardRecovery);
		return getMinimumCostRecovery(candidates);
	}
	
	private RecoverInterpretation sublistRecovery(int startIndex, IStrategoTerm listTerm, IStrategoTerm parent) {
		assert listTerm.isList();
		assert listTerm.getSubtermCount() != 0;
		
		for (int i = startIndex; i < listTerm.getSubtermCount(); i++) {
			if(i == listTerm.getSubtermCount()-1 || hasCorrectSeparationAfterIndex(listTerm, i)){
				int endIndex = i;
				IStrategoTerm prefixSublist = createSublist(listTerm, startIndex, endIndex);
				ArrayList<RecoverInterpretation> candidates = 
						getRecoverCandidatesFromChildTerms(listTerm, startIndex, endIndex, prefixSublist, listTerm);
				RecoverInterpretation prefixRecovery = getMinimumCostRecovery(candidates);
				if(prefixRecovery != null){
					assert prefixRecovery.getTerm() == prefixSublist;
					assert prefixRecovery.getSubtermRecoveries().size() >= 1;
					assert !prefixRecovery.isDiscardRecovery();
					RecoverInterpretation suffixRecovery = sublistRecovery(endIndex + 1, listTerm, parent);
					if(suffixRecovery != null){
						assert !suffixRecovery.isDiscardRecovery();
						ArrayList<RecoverInterpretation> recoveredSubLists = new ArrayList<RecoverInterpretation>();
						recoveredSubLists.add(prefixRecovery);
						recoveredSubLists.add(suffixRecovery);
						return RecoverInterpretation.createRepairSublistsInterpretation(listTerm, parent, recoveredSubLists);
					}
					else{ //prefixRecovery != null && suffixRecovery == null
						ArrayList<RecoverInterpretation> prefixCandidates = prefixRecovery.getSubtermRecoveries();
						if(startIndex == 0) //whole list recovered with single term
							return RecoverInterpretation.createReplaceBySubtermsInterpretation(listTerm, parent, prefixCandidates);
						IStrategoTerm sublist = createSublist(listTerm, startIndex, listTerm.getSubtermCount() - 1);
						return RecoverInterpretation.createReplaceBySubtermsInterpretation(sublist, listTerm, prefixCandidates);
					}
				}
			}
		}
		return null;
	}

	private IStrategoTerm createSublist(IStrategoTerm listTerm, int startIndex,
			int endIndex) {
		IStrategoTerm prefixSublist;
		final ITermFactory termFactory = new TermFactory().getFactoryWithStorageType(IStrategoTerm.MUTABLE);
		final TermTreeFactory termTreeFactory = new TermTreeFactory(termFactory);
		IStrategoTerm firstChild = listTerm.getSubterm(startIndex);
		IStrategoTerm lastChild = listTerm.getSubterm(endIndex);
		prefixSublist = termTreeFactory.createSublist((IStrategoList)listTerm, firstChild, lastChild);
		return prefixSublist;
	}

	private boolean hasCorrectSeparationAfterIndex(IStrategoTerm listTerm, int i) {
		if(i < listTerm.getSubtermCount() - 1){
			int startOffset = getRightToken(listTerm.getSubterm(i)).getEndOffset() + 1;
			int endOffset = getLeftToken(listTerm.getSubterm(i+1)).getStartOffset() - 1;
			return getCoveredOffsets(startOffset, endOffset, offsetsDeletedChars).isEmpty();
		}
		else
			return true;
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
		if(HelperFunctions.isSomeNode(term) || term.isList()){ //|| (parent != null && parent.isList())
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
		//set recover interpretation provided by the visited term
		RecoverInterpretation fromVisitedRecovery = null;
		RecoverInterpretation candidateFromVisited = lookupRecovery(visitedTerm, null);
		if(candidateFromVisited != null && candidateFromVisited.hasCompatibleSort(term, parent)){
			assert candidateFromVisited.getTerm() == visitedTerm;
			fromVisitedRecovery = RecoverInterpretation.createReplaceBySubtermsInterpretation(term, parent, candidateFromVisited);
			assert fromVisitedRecovery != null && fromVisitedRecovery.getTerm() == term && fromVisitedRecovery.hasCompatibleSort(term, parent);
		}

		//return from visited recovery if that is known to be better than interpretations provided by its subterms
		if(fromVisitedRecovery != null && (fromVisitedRecovery.hasSameSort(term, parent) || candidateFromVisited.isUndamagedTerm())){
			//optimal interpretation for the fragment already found, no need to further traverse subterms 
			return fromVisitedRecovery;
		}

		//find candidates by traversing subterms of visited term
		RecoverInterpretation fromSubtermsRecovery = null;
		if(parent == null || !parent.isList()){
			fromSubtermsRecovery = getMinimumCostRecoveryFromChildterms(visitedTerm, term, parent);
		}
		else { //parent is list
			//TODO: separators?
			fromSubtermsRecovery = getMergedRecoveriesFromChildTerms(visitedTerm, term, parent);
		}

		//compares recovery from subterms and recovery from visited term and chooses the best of both.
		RecoverInterpretation minimumCostRecovery = getMinimumCostRecovery(fromSubtermsRecovery, fromVisitedRecovery);
		assert minimumCostRecovery == null || (minimumCostRecovery.getTerm() == term && minimumCostRecovery.hasCompatibleSort(term, parent)) ;
		return minimumCostRecovery;
	}

	private RecoverInterpretation getMergedRecoveriesFromChildTerms(
			IStrategoTerm visitedTerm, IStrategoTerm term, IStrategoTerm parent) {
		ArrayList<RecoverInterpretation> candidatesFromSubterms = getRecoverCandidatesFromChildTerms(visitedTerm, term, parent);
		ArrayList<RecoverInterpretation> mergedSubtermRecoveries = new ArrayList<RecoverInterpretation>();
		for (int i = 0; i < candidatesFromSubterms.size(); i++) {
			mergedSubtermRecoveries.addAll(candidatesFromSubterms.get(i).getSubtermRecoveries());
		}
		RecoverInterpretation fromSubtermsRecovery = null;
		if(!mergedSubtermRecoveries.isEmpty()){
			fromSubtermsRecovery = RecoverInterpretation.createReplaceBySubtermsInterpretation(term, parent, mergedSubtermRecoveries);
		}
		return fromSubtermsRecovery;
	}

	private RecoverInterpretation getMinimumCostRecoveryFromChildterms(IStrategoTerm visitedTerm, IStrategoTerm term, IStrategoTerm parent) {
		ArrayList<RecoverInterpretation> candidatesFromSubterms = getRecoverCandidatesFromChildTerms(visitedTerm, term, parent);
		RecoverInterpretation minimumCostRecovery = getMinimumCostRecovery(candidatesFromSubterms); //can be null
		return minimumCostRecovery;
	}

	private ArrayList<RecoverInterpretation> getRecoverCandidatesFromChildTerms(
			IStrategoTerm visitedTerm, IStrategoTerm term, IStrategoTerm parent) {
		if(visitedTerm.getSubtermCount() == 0)
			return new ArrayList<RecoverInterpretation>();
		int startIndex = 0;
		int endIndex = visitedTerm.getSubtermCount() - 1;
		ArrayList<RecoverInterpretation> candidatesFromSubterms = getRecoverCandidatesFromChildTerms(visitedTerm, startIndex, endIndex, term, parent);
		return candidatesFromSubterms;
	}

	private ArrayList<RecoverInterpretation> getRecoverCandidatesFromChildTerms(
			IStrategoTerm visitedTerm, int startIndex, int endIndex,
			IStrategoTerm term, IStrategoTerm parent) {
		assert endIndex <= visitedTerm.getSubtermCount()-1;
		assert startIndex >= 0;
		assert startIndex <= endIndex;
		//traverse subterms of subterm
		ArrayList<RecoverInterpretation> candidatesFromSubterms = new ArrayList<RecoverInterpretation> ();
		for (int i = startIndex; i <= endIndex; i++) {
			IStrategoTerm subterm = visitedTerm.getSubterm(i);
			//System.out.println("visited: " + subterm);
			RecoverInterpretation subtermRecovery = getRecoveryFromSubterms(subterm, visitedTerm, term, parent);
			//System.out.println("recovery: " + subtermRecovery);
			if(subtermRecovery != null && !subtermRecovery.isDiscardRecovery()){
				assert subtermRecovery.hasCompatibleSort(term, parent);
				assert subtermRecovery.getTerm() == term;
				candidatesFromSubterms.add(subtermRecovery);
			}
		}
		return candidatesFromSubterms;
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
		int startOffset = getLeftToken(term).getStartOffset();
		int endOffset = getRightToken(term).getEndOffset();
		ArrayList<Integer> coveredOffsets = getCoveredOffsets(startOffset, endOffset, offsets);
		return coveredOffsets;
	}

	private static ArrayList<Integer> getCoveredOffsets(int startOffset, int endOffset, ArrayList<Integer> offsets) {
		ArrayList<Integer> coveredOffsets = new ArrayList<Integer>();
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
