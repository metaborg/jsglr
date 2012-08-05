package org.spoofax.jsglr.client.editregion.detection;

import static org.spoofax.jsglr.client.imploder.ImploderAttachment.getLeftToken;
import static org.spoofax.jsglr.client.imploder.ImploderAttachment.getRightToken;
import static org.spoofax.jsglr.client.imploder.ImploderAttachment.getTokenizer;

import java.util.ArrayList;

import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.jsglr.client.imploder.ITokenizer;
import org.spoofax.jsglr.client.imploder.ImploderAttachment;
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
		return ImploderAttachment.getSort(term);
	}
	
	public boolean hasSameSort(IStrategoTerm term, IStrategoTerm parent) {
		String generalSort = HelperFunctions.getGeneralSort(term, parent);
		String sort = ImploderAttachment.getSort(term);
		String generalTermSort = this.getGeneralSort();
		String termSort = this.getSort();		
		return generalSort == generalTermSort && sort == termSort;
	}

	public boolean hasCompatibleSort(IStrategoTerm term, IStrategoTerm parent) {
		String generalSort = HelperFunctions.getGeneralSort(term, parent);
		String sort = ImploderAttachment.getSort(term);
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
		checkAssertions();
	}

	private void checkAssertions() {
		assert subtermRecoveries == null? (isRecovered && this.recoveryCosts == 0) : recoveryCosts > 0;
		int subtermCosts = 0;
		if(this.subtermRecoveries != null){
			for (RecoverInterpretation subtermRecovery : this.subtermRecoveries) {
				assert subtermRecovery != null;	
				subtermCosts += subtermRecovery.getRecoveryCosts();
			}
		}
		assert isRecovered? recoveryCosts == subtermCosts : recoveryCosts >= subtermCosts;
	}
	
//private functions
	
	private int calculateRecoveryCosts(){
		int costs = 0;
		if(subtermRecoveries == null){
			return 0;			
		}
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
	
	public ArrayList<DiscardableRegion> getDamagedRegions(){		
		if(this.getSubtermRecoveries() == null){ //trivial recovery (no damage)
			return new ArrayList<DiscardableRegion>(); 
		}
		else if(isRecovered){ //only damage in tokens associated to subterms 
			ArrayList<DiscardableRegion> damagedRegions = new ArrayList<DiscardableRegion>();
			for (RecoverInterpretation subtermRecovery : getSubtermRecoveries()) {
				damagedRegions.addAll(subtermRecovery.getDamagedRegions());
			}
			return damagedRegions; 
		}
		else { //damage in tokens associated to term
			return getDamagedRegionsForAffectedTerm();
		}
	}

	private ArrayList<DiscardableRegion> getDamagedRegionsForAffectedTerm() {
		String input = ImploderAttachment.getTokenizer(getTerm()).getInput();
		ArrayList<DiscardableRegion> damagedRegions = new ArrayList<DiscardableRegion>();
		int startOffset = getLeftToken(term).getStartOffset();
		for (int i = 0; i < getSubtermRecoveries().size(); i++) {
			RecoverInterpretation subtermRecovery = getSubtermRecoveries().get(i);
			int endOffset = getLeftToken(subtermRecovery.getTerm()).getStartOffset()-1;
			if(startOffset <= endOffset){ //discard tokens associated to term
				DiscardableRegion region = new DiscardableRegion(startOffset, endOffset, input);
				damagedRegions.add(region);
			}
			damagedRegions.addAll(subtermRecovery.getDamagedRegions()); //collect damaged regions in subterm
			startOffset = getRightToken(subtermRecovery.getTerm()).getEndOffset() + 1;
		}
		int endOffset = getRightToken(term).getEndOffset();
		if(startOffset <= endOffset){ //discard suffix tokens associated to term
			DiscardableRegion damagedRegion = new DiscardableRegion(startOffset, endOffset, input);
			damagedRegions.add(damagedRegion);
		}
		return damagedRegions;
	}

	public boolean isUndamagedTerm() {
		return getSubtermRecoveries() == null;
	}
	
	//TODO: GetDamagedTerms
	//TODO: ToString
}








