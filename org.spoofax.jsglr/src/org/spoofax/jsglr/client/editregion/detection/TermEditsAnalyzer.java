package org.spoofax.jsglr.client.editregion.detection;

import java.util.ArrayList;
import java.util.HashMap;

import org.spoofax.interpreter.terms.IStrategoAppl;
import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.jsglr.client.imploder.ITokenizer;
import org.spoofax.jsglr.client.imploder.Token;

import static org.spoofax.jsglr.client.imploder.ImploderAttachment.*;
import sun.security.action.GetLongAction;

public class TermEditsAnalyzer {
	private ArrayList<Integer> offsetsDeletedChars;
	private HashMap<IStrategoTerm, RecoverInterpretation> recoveryLookup;

	public TermEditsAnalyzer(){
		offsetsDeletedChars = new ArrayList<Integer>(); //TODO
		recoveryLookup = new HashMap<IStrategoTerm, RecoverInterpretation>();
	}
	
	private ArrayList<IStrategoTerm> findRecoveryTerms(IStrategoTerm term, IStrategoTerm parent){
		if(isSomeNode(term)){
			return new ArrayList<IStrategoTerm>();	//discarding whole term			
		}
		String generalSort = getGeneralSort(term, parent); //more universal sort, example: Blockstm vs Exprstm
		String sort = getSort(term);
		if(parent.isList()){
			ArrayList<IStrategoTerm> replacementCandidates = getRecoveryFromSubterms(term, parent, sort, generalSort); 
			//TODO: separators??
		}

		
		return null;
	}
	
	private ArrayList<IStrategoTerm> getRecoveryFromSubterms(IStrategoTerm term, IStrategoTerm parent, String sort, String generalSort) {
		ArrayList<IStrategoTerm> result = new ArrayList<IStrategoTerm>();

		//determines if the term has a compatible sort, which is a required to be a recover candidate,
		//and determines if it has the same sort, which means that the already found recovery (if any) 
		//is optimal, e.g., no need to traverse the subterms.
		String generalTermSort = getGeneralSort(term, parent);
		String termSort = getElementSort(term);
		boolean hasCompatibleSort = 
				generalSort.equals(generalTermSort) ||
				generalSort.replace("*", "").equals(generalTermSort) ||
				sort.equals(termSort);
		boolean hasSameSort = generalSort == generalTermSort && sort == termSort; 
		
		//terms of a compatible sort with a recover interpretation are candidates for recovery.
		IStrategoTerm termCandidate = null;
		if(hasCompatibleSort && hasRecoverInterpretation(term)){
			termCandidate = term;
		}
		
		//terms with the same sort (1), or terms that are not affected by editing (2) are not further inspected.
		//1) already constructed recover interpretation is also the best interpretation for the current search, or
		//2. we do not break interpretations that are not affected, e.g., outside the edit region. 
		//thus, the candidate is the best recovery, or no recovery is found in this region.
		if(hasSameSort || !hasDeletions(term)){
			if(termCandidate != null){
				result.add(termCandidate);
				return result;
			}
			if(isSomeNode(term) || parent.isList()){
				return new ArrayList<IStrategoTerm>(); //represents a full skip recovery, e.g., no sub-terms are preserved 
			}
			return null; //region of term does not provide a recovery 
		}

		//recursively look for recover interpretations in subterms
		ArrayList<IStrategoTerm> subtermCandidate = new ArrayList<IStrategoTerm>();
		for (int i = 0; i < term.getSubtermCount(); i++) {
			IStrategoTerm subterm = term.getSubterm(i);
			ArrayList<IStrategoTerm> subtermRecovery = getRecoveryFromSubterms(subterm, term, sort, generalSort);
			if(isListSort(generalSort)){// pick all compatible sub terms for elements in a list (which can be recovered by multiple elements)
				subtermCandidate.addAll(subtermRecovery);
			}
			else { //pick the best recovery for non list terms
				subtermCandidate = getMinimumCostRecovery(subtermCandidate, subtermRecovery);
			}
		}
		
		
	}

	private ArrayList<IStrategoTerm> getMinimumCostRecovery(RecoverInterpretation recovery1, RecoverInterpretation recovery2) {
		//TODO
		int recoverCost1 = 0;
		for (IStrategoTerm term : recovery1) {
			ArrayList<IStrategoTerm> recoveredTerms = this.recoveryLookup.get(term);
			int recoverCostTerm = getRecoverCost(recoveredTerms);
		}
		return null;
	}


	public boolean isListSort(String sort) {
		return sort.endsWith("*");
	}

	public String getGeneralSort(IStrategoTerm term, IStrategoTerm parent) {
		String termSort = ImploderAttachment.getElementSort(term);
		if(parent.isList())
			termSort = ImploderAttachment.getElementSort(parent);
		assert (term.isList() || parent.isList())? termSort.endsWith("*") : !termSort.endsWith("*");
		return termSort;
	}

	private boolean hasDeletions(IStrategoTerm term) {
		return !getCoveredOffsets(term, this.offsetsDeletedChars).isEmpty();
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


	public boolean hasRecoverInterpretation(IStrategoTerm term) {
		return recoveryLookup.containsKey(term);
	}

	private boolean isSomeNode(IStrategoTerm trm) {
		if(trm.getTermType() == IStrategoTerm.APPL){
			return trm.getSubtermCount() == 1 && ((IStrategoAppl)trm).getConstructor().getName().equals("Some");
		}
		return false;
	}


}
