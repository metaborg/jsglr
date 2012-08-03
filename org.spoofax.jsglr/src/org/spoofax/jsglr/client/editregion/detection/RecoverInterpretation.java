package org.spoofax.jsglr.client.editregion.detection;

import static org.spoofax.jsglr.client.imploder.ImploderAttachment.getLeftToken;
import static org.spoofax.jsglr.client.imploder.ImploderAttachment.getRightToken;
import static org.spoofax.jsglr.client.imploder.ImploderAttachment.getTokenizer;

import java.util.ArrayList;
import java.util.HashMap;

import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.jsglr.client.imploder.ITokenizer;
import org.spoofax.jsglr.client.imploder.Token;

public class RecoverInterpretation {

//private fields

	private final IStrategoTerm term;
	private final boolean isRecovered;
	private final ArrayList<RecoverInterpretation> recoveredSubterms;
	

	private final ArrayList<Integer> coveredDeletionOffsets;
	private final int recoveryCosts;

//public fields

	public IStrategoTerm getTerm() {
		return term;
	}

	public ArrayList<Integer> getCoveredDeletionOffsets() {
		return coveredDeletionOffsets;
	}

	public int getRecoveryCosts() {
		return recoveryCosts;
	}

// constructor
	
	public RecoverInterpretation(
			IStrategoTerm term, 
			ArrayList<RecoverInterpretation> recoveredSubterms, 
			ArrayList<Integer> deletionOffsets
	) {
		this.term = term;
		this.coveredDeletionOffsets = getCoveredOffsets(term, deletionOffsets);
		this.recoveredSubterms = recoveredSubterms;
		this.recoveryCosts = calculateRecoveryCosts(); 
	}
	
//private functions

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
	
	private int calculateRecoveryCosts(){
		ArrayList<ArrayList<IStrategoTerm>>
		int costs = numberOfNonLayoutTokens(this.getTerm()) - numberOfNonLayoutTokens(this.recoveredSubterms);
		for (RecoverInterpretation subtermRecovery : recoveredSubterms) {
			costs += subtermRecovery.getRecoveryCosts();
		}
		return costs;
	}
	
	private int numberOfNonLayoutTokens(ArrayList<IStrategoTerm> terms) {
		int result = 0;
		for (IStrategoTerm recoveredTerm : terms) {
			int nrOfNonLayoutTokens = numberOfNonLayoutTokens(recoveredTerm);
			result += nrOfNonLayoutTokens;
		}
		return result;
	}

	private int numberOfNonLayoutTokens(IStrategoTerm term) {
		ITokenizer tokens = getTokenizer(term);
		int leftIndex =  getLeftToken(term).getIndex();
		int rightIndex =  getRightToken(term).getIndex();
		int nrOfNonLayoutTokens = 0;
		for (int i = leftIndex; i <= rightIndex; i++) {
			if(tokens.getTokenAt(i).getKind() != Token.TK_LAYOUT){
				nrOfNonLayoutTokens ++;
			}
		}
		return nrOfNonLayoutTokens;
	}

}
