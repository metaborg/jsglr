package org.spoofax.jsglr.client.editregion.detection;

import static org.spoofax.jsglr.client.imploder.ImploderAttachment.getElementSort;
import static org.spoofax.jsglr.client.imploder.ImploderAttachment.getLeftToken;
import static org.spoofax.jsglr.client.imploder.ImploderAttachment.getRightToken;
import static org.spoofax.jsglr.client.imploder.ImploderAttachment.getTokenizer;

import java.util.ArrayList;

import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.jsglr.client.imploder.ITokenizer;
import org.spoofax.jsglr.client.imploder.Token;

public class RecoverInterpretation {

//private fields

	private final IStrategoTerm term;
	private final IStrategoTerm parentTerm;
	private final boolean isRecovered;
	private final ArrayList<RecoverInterpretation> subtermRecoveries;

	private final int recoveryCosts;

//public field accessors 

	public IStrategoTerm getTerm() {
		return term;
	}
	
	public ArrayList<RecoverInterpretation> getSubtermRecoveries() {
		return subtermRecoveries;
	}

	public int getRecoveryCosts() {
		return recoveryCosts;
	}

//public methods
		
	//	public boolean isListSort(String sort) {
	//		return sort.endsWith("*");
	//	}
	
	public String getGeneralSort(){
		return HelperFunctions.getGeneralSort(term, parentTerm);
	}

	public String getSort(){
		return getElementSort(term);
	}
	
	public boolean hasSameSort(IStrategoTerm term, IStrategoTerm parent) {
		String generalSort = HelperFunctions.getGeneralSort(term, parent);
		String sort = getElementSort(term);
		String generalTermSort = this.getGeneralSort();
		String termSort = this.getSort();		
		return generalSort == generalTermSort && sort == termSort;
	}

	public boolean hasCompatibleSort(IStrategoTerm term, IStrategoTerm parent) {
		String generalSort = HelperFunctions.getGeneralSort(term, parent);
		String sort = getElementSort(term);
		String generalTermSort = this.getGeneralSort();
		String termSort = this.getSort();
		boolean hasCompatibleSort = 
				generalSort.equals(termSort) ||
				generalSort.equals(generalTermSort.replace("*", "")) ||
				generalSort.equals(generalTermSort) ||
				sort.equals(termSort) ||
				sort.equals(generalTermSort.replace("*", "")) ||
				sort.equals(generalTermSort) ||
				generalSort.replace("*", "").equals(termSort) ||
				generalSort.replace("*", "").equals(generalTermSort.replace("*", "")) ||
				generalSort.replace("*", "").equals(generalTermSort);
				
		return hasCompatibleSort;
	}

	
// constructors
	
	public static RecoverInterpretation createOriginalTermInterpretation(IStrategoTerm term, IStrategoTerm parentTerm){
		return new RecoverInterpretation(term, parentTerm, true, null);
		//Remark: recursively constructing original term recoveries for subterms seems not needed. 
	}

	public static RecoverInterpretation createDiscardInterpretation(IStrategoTerm term, IStrategoTerm parentTerm){
		assert parentTerm.isList() || HelperFunctions.isSomeNode(term);
		return new RecoverInterpretation(term, parentTerm, false, new ArrayList<RecoverInterpretation>());
	}

	public static RecoverInterpretation createRepairSubtermsInterpretation(IStrategoTerm term, IStrategoTerm parentTerm, ArrayList<RecoverInterpretation> recoveredSubterms){
		assert recoveredSubterms.size() == term.getSubtermCount() && !recoveredSubterms.contains(null);
		return new RecoverInterpretation(term, parentTerm, true, recoveredSubterms);
	}

	public static RecoverInterpretation createReplaceBySubtermsInterpretation(IStrategoTerm term, IStrategoTerm parentTerm, RecoverInterpretation recoveredSubterm){
		assert recoveredSubterm.hasCompatibleSort(term, parentTerm);
		ArrayList<RecoverInterpretation> subRecoveries = new ArrayList<RecoverInterpretation>();
		subRecoveries.add(recoveredSubterm);
		return createReplaceBySubtermsInterpretation(term, parentTerm, subRecoveries);
	}

	public static RecoverInterpretation createReplaceBySubtermsInterpretation(IStrategoTerm term, IStrategoTerm parentTerm, ArrayList<RecoverInterpretation> recoveredSubterms){
		assert parentTerm.isList() || recoveredSubterms.size() == 1;
		return new RecoverInterpretation(term, parentTerm, false, recoveredSubterms);
	}

	private RecoverInterpretation(
			IStrategoTerm term, 
			IStrategoTerm parentTerm,
			boolean isRecovered,
			ArrayList<RecoverInterpretation> recoveredSubterms
	) {
		this.term = term;
		this.parentTerm = parentTerm;
		this.isRecovered = isRecovered;
		this.subtermRecoveries = recoveredSubterms;
		this.recoveryCosts = calculateRecoveryCosts(); 
	}
	
//private functions
	
	private int calculateRecoveryCosts(){
		int costs = 0;
		if(!isRecovered){
			int nrOfNonLayoutInSubterms = 0;
			for (RecoverInterpretation subtermRecovery : this.subtermRecoveries) {
				nrOfNonLayoutInSubterms += numberOfNonLayoutTokens(subtermRecovery.getTerm());
			}
			costs = numberOfNonLayoutTokens(this.getTerm()) - nrOfNonLayoutInSubterms;
		}
		for (RecoverInterpretation subtermRecovery : subtermRecoveries) {
			costs += subtermRecovery.getRecoveryCosts();
		}
		return costs;
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
