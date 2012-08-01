package org.spoofax.jsglr.client.editregion.detection;

import static org.spoofax.jsglr.client.imploder.ImploderAttachment.getLeftToken;
import static org.spoofax.jsglr.client.imploder.ImploderAttachment.getRightToken;

import java.util.ArrayList;

import org.spoofax.interpreter.terms.IStrategoAppl;
import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.jsglr.client.imploder.ImploderAttachment;

/**
 * Detects edited code constructs in a (correctly parsed) AST,
 * by analyzing the offsets of the deleted characters.
 * The edited constructs are returned as damaged regions, e.g.
 * fragments in the source code that represent constructs
 * that can be discarded safely, e.g., without invalidating the parse input.
 * @author maartje
 *
 */
public class NonTerminalEditsAnalyzer {
		
	private final IStrategoTerm term;
	private final ArrayList<Integer> offsetsDeletedChars;
	
	/**
	 * Detects edited (discardable) code constructs in a (correctly parsed) AST,
	 * by analyzing the offsets of the deleted characters.
	 */
	public NonTerminalEditsAnalyzer(IStrategoTerm term, ArrayList<Integer> offsetsDeletedChars){
		this.term = term;
		this.offsetsDeletedChars = offsetsDeletedChars;
	}


	/**
	 * Returns discardable regions that are edited 
	 */
	public ArrayList<DiscardableRegion> getDamagedTermRegions(){
		return getAffectedDiscardableRegions(term, null, null, false);
	}

	//TODO: what if incomplete?
	//TODO: may be better to return incomplete elems that are resonable small than one big area	
	private ArrayList<DiscardableRegion> getAffectedDiscardableRegions(
			IStrategoTerm trm, 
			IStrategoTerm prevSibTrm, 
			IStrategoTerm nextSibTrm, 
			boolean hasListParent) {
		DiscardableRegion regionOfTerm = getRegionOfTerm(trm, nextSibTrm, hasListParent);
		ImploderAttachment.getElementSort(trm);
		ImploderAttachment.getSort(trm);
		if(offsetsOutsideFragment(offsetsDeletedChars, regionOfTerm) ){
			return new ArrayList<DiscardableRegion>(); //no need to traverse subterms 
		}		
		ArrayList<DiscardableRegion> candidatesFromSubterms = getCandidateRegionsFromSubterms(trm, offsetsDeletedChars);
		ArrayList<Integer> uncoveredOffsets = getUncoveredOffsets(offsetsDeletedChars, candidatesFromSubterms);
		ArrayList<Integer> offsetsCoveredByTerm = getCoveredOffsets(uncoveredOffsets, regionOfTerm);
		/*parent overwrites child results because it covers more deletions*/
		/*example: while(c){int i; int j;} whereby 'j' and '}' are deleted*/
		if(!offsetsCoveredByTerm.isEmpty()){ //term associated to some deleted (unmatched) characters 
			assert (nextSibTrm != null): "last list element should be correct after removal procedure";
			if(hasListParent){
				String sort = ImploderAttachment.getElementSort(trm);
				ArrayList<IStrategoTerm> replacementCandidates = getUnaffectedSubtermsOfSameSort(trm, sort, offsetsDeletedChars); 
				return splitRegion(regionOfTerm, replacementCandidates); //example: erroneous while statement, keep all non-erroneous sub statements  
				
				//TODO: separators??
//				ArrayList<DiscardableRegion> result = new ArrayList<DiscardableRegion>();
//				result.add(regionOfTerm);
//				return result; 				
			}
			else if(isSomeNode(trm)){
				ArrayList<DiscardableRegion> result = new ArrayList<DiscardableRegion>();
				result.add(regionOfTerm);
				return result;				
			}
			else {
				String sort = ImploderAttachment.getElementSort(trm);
				ArrayList<IStrategoTerm> replacementCandidates = getUnaffectedSubtermsOfSameSort(trm, sort, offsetsDeletedChars); 
				IStrategoTerm replacement = getTermWithLargestFragment(replacementCandidates);
				if(replacement != null){
					ArrayList<IStrategoTerm> subTerms = new ArrayList<IStrategoTerm>();
					subTerms.add(replacement);
					return splitRegion(regionOfTerm, subTerms);
				}
			}
		}
		return candidatesFromSubterms;
	}
	
	private ArrayList<DiscardableRegion> getCandidateRegionsFromSubterms(IStrategoTerm trm, ArrayList<Integer> offsetsDeletedChars) {
		//to deal with separators: first remove damaged elements at suffix including their separation
		int indexLastAffectedSubterm = getLastEffectedListElementIndex(trm, offsetsDeletedChars);		
		ArrayList<DiscardableRegion> candidatesFromSubterms = new ArrayList<DiscardableRegion>();
		for (int i = 0; i < indexLastAffectedSubterm; i++) {
			IStrategoTerm nextSib = null;
			IStrategoTerm prevSib = null;
			if(i > 0){
				prevSib = trm.getSubterm(i-1);
			}
			if(i < indexLastAffectedSubterm - 1){
				nextSib = trm.getSubterm(i+1);
			}
			ArrayList<DiscardableRegion> candidates_subtrm_i = 
					getAffectedDiscardableRegions(trm.getSubterm(i), prevSib, nextSib, trm.isList()); 
			candidatesFromSubterms.addAll(candidates_subtrm_i);
		}
		for (int i = indexLastAffectedSubterm; i < trm.getSubtermCount(); i++) {
			IStrategoTerm subterm = trm.getSubterm(indexLastAffectedSubterm);
			IStrategoTerm subterm_prev = trm.getSubterm(indexLastAffectedSubterm-1);
			int endOffset = getEndOffset(subterm);
			int startOffset = getRightToken(subterm_prev).getEndOffset() + 1;
			candidatesFromSubterms.add(new DiscardableRegion(startOffset, endOffset, subterm));			
		}
		return candidatesFromSubterms;
	}

	private int getLastEffectedListElementIndex(IStrategoTerm trm, ArrayList<Integer> offsetsDeletedChars) {
		int indexLastAffectedSubterm = trm.getSubtermCount();
		while (trm.isList() && indexLastAffectedSubterm > 1) {
			IStrategoTerm subterm = trm.getSubterm(indexLastAffectedSubterm-1);
			ArrayList<DiscardableRegion> subterm_candidates = 
				getAffectedDiscardableRegions(subterm, null, null, true); 
			if(subterm_candidates.size() == 1 && subterm_candidates.get(0).getAffectedTerm() == subterm){
				indexLastAffectedSubterm -= 1;
			}
			else
				break;
		}
		return indexLastAffectedSubterm;
	}

	
	private ArrayList<IStrategoTerm> getUnaffectedSubtermsOfSameSort(IStrategoTerm trm, String sort, ArrayList<Integer> offsetsDeletedChars) {
		ArrayList<IStrategoTerm> candidateSubterms = new ArrayList<IStrategoTerm>();
		if(sort == null)
			return null;
		for (int i = 0; i < trm.getSubtermCount(); i++) {
			IStrategoTerm subterm = trm.getSubterm(i);
			String subtermSort = ImploderAttachment.getSort(subterm);
			ArrayList<Integer> offsetsCoveredByTerm = getCoveredOffsets(offsetsDeletedChars, new DiscardableRegion(getStartOffset(subterm), getEndOffset(subterm), subterm));
			if(sort == subtermSort && offsetsCoveredByTerm.isEmpty()){
				candidateSubterms.add(subterm);
			}
			else{
				ArrayList<IStrategoTerm> candidates = getUnaffectedSubtermsOfSameSort(subterm, sort, offsetsDeletedChars);
				candidateSubterms.addAll(candidates);
			}
		}
		return candidateSubterms;
	}

	private IStrategoTerm getTermWithLargestFragment(ArrayList<IStrategoTerm> terms) {
		IStrategoTerm bestCandidate = null;
		int bestSize = -1;
		for (int i = 0; i < terms.size(); i++) {
			IStrategoTerm alternativeCandidate = terms.get(i);
			int startOffset = getLeftToken(alternativeCandidate).getStartOffset();
			int endOffset = getEndOffset(alternativeCandidate);
			int size = endOffset - startOffset;
			if(size > bestSize){
				bestCandidate = alternativeCandidate;
				bestSize = size;
			}
		}
		return bestCandidate;
	}

	private DiscardableRegion getRegionOfTerm(IStrategoTerm trm, IStrategoTerm nextSibTrm, boolean hasListParent) {
		int startOffset = getLeftToken(trm).getStartOffset();
		int endOffset; 
		if(hasListParent && nextSibTrm != null)
			endOffset = getLeftToken(nextSibTrm).getStartOffset() - 1;
		else
			endOffset = getRightToken(trm).getEndOffset();
		DiscardableRegion regionOfTerm = new DiscardableRegion(startOffset, endOffset, trm);
		return regionOfTerm;
	}

	private boolean isSomeNode(IStrategoTerm trm) {
		if(trm.getTermType() == IStrategoTerm.APPL){
			return trm.getSubtermCount() == 1 && ((IStrategoAppl)trm).getConstructor().getName().equals("Some");
		}
		return false;
	}

	private boolean offsetsOutsideFragment(ArrayList<Integer> offsets, DiscardableRegion region) {
		int startOffset = region.getStartOffset();
		int endOffset = region.getEndOffset();
		return offsets.isEmpty() || endOffset < offsets.get(0) || startOffset > offsets.get(offsets.size()-1);
	}

	private ArrayList<DiscardableRegion> splitRegion(DiscardableRegion regionOfTerm, ArrayList<IStrategoTerm> subTerms) {
		ArrayList<DiscardableRegion> result = new ArrayList<DiscardableRegion>();
		int regionStartOffset = regionOfTerm.getStartOffset();
		for (int i = 0; i < subTerms.size(); i++) {
			IStrategoTerm subTerm = subTerms.get(i);
			assert subTerm != null;
			if(regionStartOffset < getStartOffset(subTerm)){
				DiscardableRegion discard = new DiscardableRegion(regionStartOffset, getStartOffset(subTerm) - 1, regionOfTerm.getAffectedTerm());
				result.add(discard);
			}
			regionStartOffset = getEndOffset(subTerm) + 1;
		}
		if(regionStartOffset <= regionOfTerm.getEndOffset()){
			DiscardableRegion discard = new DiscardableRegion(regionStartOffset, regionOfTerm.getEndOffset(), regionOfTerm.getAffectedTerm());
			result.add(discard);
		}
		return result;
	}


	private ArrayList<Integer> getCoveredOffsets(ArrayList<Integer> offsets, DiscardableRegion region) {
		ArrayList<Integer> coveredOffsets = new ArrayList<Integer>();
		int startOffset = region.getStartOffset();
		int endOffset = region.getEndOffset();
		for (int i = 0; i < offsets.size(); i++) {
			int offset = offsets.get(i); 
			if(startOffset <= offset && offset <= endOffset){
				//covered
				coveredOffsets.add(offset);
			}
		}
		return coveredOffsets;
	}

	private ArrayList<Integer> getUncoveredOffsets(ArrayList<Integer> offsetsDeletedChars, ArrayList<DiscardableRegion> candidatesFromSubterms) {
		ArrayList<Integer> uncoveredIndices = new ArrayList<Integer>();
		for (int i = 0; i < offsetsDeletedChars.size(); i++) {
			int deletedIndex = offsetsDeletedChars.get(i);
			boolean isCovered = false;
			for (DiscardableRegion candidate : candidatesFromSubterms) {
				if(candidate.getStartOffset() <= deletedIndex && deletedIndex <= candidate.getEndOffset()){
					isCovered = true;
					break;
				}
			}
			if(!isCovered){
				uncoveredIndices.add(deletedIndex);
			}
		}
		return uncoveredIndices;
	}

	private int getEndOffset(IStrategoTerm trm) {
		int endOffset = getRightToken(trm).getEndOffset();
		return endOffset;
	}
	
	private int getStartOffset(IStrategoTerm trm) {
		int startOffset = getLeftToken(trm).getStartOffset();
		return startOffset;
	}



}
