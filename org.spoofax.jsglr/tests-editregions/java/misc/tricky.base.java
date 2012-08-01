package detection;

import static org.spoofax.jsglr.client.imploder.ImploderAttachment.*;

import java.util.ArrayList;

import org.spoofax.interpreter.library.treediff.algorithms.LCS;
import org.spoofax.interpreter.library.treediff.algorithms.LCSCommand;
import org.spoofax.interpreter.terms.ISimpleTerm;
import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.jsglr.client.imploder.ImploderAttachment;
import org.spoofax.interpreter.core.Tools;

public class EditRegionDetector {
	public void detectEditRegions(IStrategoTerm lastErr0AST, String erroneousInput) {
		String correctInput = ImploderAttachment.getTokenizer(lastErr0AST).getInput();
		ArrayList<Character> correctChars = asCharacterList(correctInput);
		ArrayList<Character> erroneousChars = asCharacterList(erroneousInput);
		LCS<Character> lcs = new LCS<Character>(new LCSCommand<Character>() {
			public boolean isMatch(Character c1, Character c2) {
				return c1.charValue() == c2.charValue();
			}});
		lcs.createLCSResultsOptimized(correctChars, erroneousChars);
		System.out.println("LCS size: " + lcs.getLCSSize());
		System.out.println(" deleted: " + lcs.getResultUnmatched1());
		System.out.println("inserted: " + lcs.getResultUnmatched2());
		System.out.println("  unmatched_correct: " + lcs.getUnMatchedIndices1());
		System.out.println("unmatched_erroneous: " + lcs.getUnMatchedIndices2());
		ArrayList<IStrategoTerm> affectedListTerms = getAffectedListTerms(lastErr0AST, lcs.getUnMatchedIndices1(), false);
		System.out.println("affected list terms: " + affectedListTerms);
	}

	//TODO: what if incomplete?
	//TODO: may be better to return incomplete elems that are resonable small than one big area
	private ArrayList<IStrategoTerm> getAffectedListTerms(IStrategoTerm trm, ArrayList<Integer> offsetsDeletedChars, boolean hasListParent) {
		if(offsetsOutsideFragment(offsetsDeletedChars, trm) ){
			return new ArrayList<IStrategoTerm>(); //no need to traverse subterms 
		}
		ArrayList<IStrategoTerm> candidatesFromSubterms = new ArrayList<IStrategoTerm>();
		for (int i = 0; i < trm.getSubtermCount(); i++) {
			ArrayList<IStrategoTerm> candidates_subtrm_i = getAffectedListTerms(trm.getSubterm(i), offsetsDeletedChars, Tools.isTermList(trm)); 
			candidatesFromSubterms.addAll(candidates_subtrm_i);
		}
		if(hasListParent){
			ArrayList<Integer> uncoveredOffsets = getUncoveredOffsets(offsetsDeletedChars, candidatesFromSubterms);
			if(coversAtLeastOneOffset(uncoveredOffsets, trm)){
				ArrayList<IStrategoTerm> result = new ArrayList<IStrategoTerm>();
				result.add(trm);
				return result; 
			}
		}
		return candidatesFromSubterms;
	}

	private boolean offsetsOutsideFragment(ArrayList<Integer> offsets, IStrategoTerm trm) {
		int startOffset = getLeftToken(trm).getStartOffset();
		int endOffset = getRightToken(trm).getEndOffset();
		return offsets.isEmpty() || endOffset < offsets.get(0) || startOffset > offsets.get(offsets.size()-1);
	}

	private boolean coversAtLeastOneOffset(ArrayList<Integer> uncoveredOffsets, IStrategoTerm trm) {
		boolean coversExtraOffset = false;
		if(!uncoveredOffsets.isEmpty()){
			int startOffset = getLeftToken(trm).getStartOffset();
			int endOffset = getRightToken(trm).getEndOffset();
			for (int i = 0; i < uncoveredOffsets.size(); i++) {
				int uncoveredIndex = uncoveredOffsets.get(i); 
				if(startOffset <= uncoveredIndex && uncoveredIndex <= endOffset){
					//covered
					/*parent list overwrites child list results because it covers more deletions*/
					/*example: while(c){int i; int j;} whereby 'j' and '}' are deleted*/
					coversExtraOffset = true;
					break;
				}
			}
		}
		return coversExtraOffset;
	}

	private ArrayList<Integer> getUncoveredOffsets(ArrayList<Integer> offsetsDeletedChars, ArrayList<IStrategoTerm> candidatesFromSubterms) {
		ArrayList<Integer> uncoveredIndices = new ArrayList<Integer>();
		for (int i = 0; i < offsetsDeletedChars.size(); i++) {
			int deletedIndex = offsetsDeletedChars.get(i);
			boolean isCovered = false;
			for (IStrategoTerm candidate : candidatesFromSubterms) {
				if(getLeftToken(candidate).getStartOffset() <= deletedIndex && deletedIndex <= getRightToken(candidate).getEndOffset()){
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



	private static ArrayList<Character> asCharacterList(String correctInput) {
		ArrayList<Character> correctChars = new ArrayList<Character>();
		for (int i = 0; i < correctInput.length(); i++) {
			correctChars.add(correctInput.charAt(i));
		}
		return correctChars;
	}	

}
