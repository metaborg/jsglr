package org.spoofax.jsglr.client.editregion.detection;

import static org.spoofax.jsglr.client.imploder.ImploderAttachment.getLeftToken;
import static org.spoofax.jsglr.client.imploder.ImploderAttachment.getRightToken;
import static org.spoofax.jsglr.client.imploder.ImploderAttachment.getTokenizer;

import java.util.ArrayList;

import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.jsglr.client.imploder.ITokens;
import org.spoofax.jsglr.client.imploder.ImploderAttachment;
import org.spoofax.jsglr.client.imploder.Token;

/**
 * Represents a recovery based on discarding tokens associated to the term and/or its subterms.
 * @author maartje
 *
 */
public class RecoverInterpretation {

//private fields

	private final IStrategoTerm term;
	private final IStrategoTerm parentTerm;
	private final boolean isRecovered;
	private final ArrayList<RecoverInterpretation> subtermRecoveries;
	private final int recoveryCosts;

//public field accessors 

	/**
	 * Returns the recovered term
	 * @return
	 */
	public IStrategoTerm getTerm() {
		return term;
	}
	
	/**
	 * Returns the recoveries associated to the subterms
	 * @return
	 */
	public ArrayList<RecoverInterpretation> getSubtermRecoveries() {
		return subtermRecoveries;
	}

	/**
	 * Returns the cost of the recovery calculated as the number of non-layout tokens that are discarded from the correct input
	 * fragment associated to the recovered term 
	 * @return
	 */
	public int getRecoveryCosts() {
		return recoveryCosts;
	}

//public methods	
	
	/**
	 * Gets the sort of the (parent) list for lists and list elems, and the sort of the term for other terms 
	 * @return
	 */
	public String getGeneralSort(){
		return HelperFunctions.getGeneralSort(term, parentTerm);
	}

	/**
	 * Gets the sort of the term
	 * @return
	 */
	public String getSort(){
		return ImploderAttachment.getSort(term);
	}
	
	/**
	 * Says wether the recovered term has the same sort and general sort as the term given as parameter
	 * @param term
	 * @param parent, needed to determine the general sort of the term 
	 * @return
	 */
	public boolean hasSameSort(IStrategoTerm term, IStrategoTerm parent) {
		String generalSort = HelperFunctions.getGeneralSort(term, parent);
		String sort = ImploderAttachment.getSort(term);
		String generalTermSort = this.getGeneralSort();
		String termSort = this.getSort();		
		return generalSort == generalTermSort && sort == termSort;
	}

	/**
	 * Says whether the recovery has a sort that is compatible with the sort of the input term,
	 * e.g., the recovered term can be used as a recover interpretation for the input term 
	 * @param term
	 * @param parent
	 * @return
	 */
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

	/**
	 * Says wether the recovery is a trivial recover interpretation,
	 *  e.g. the original interpretation of an undamaged term. 
	 * @return
	 */
	public boolean isUndamagedTerm() {
		return getSubtermRecoveries() == null;
	}
	
	/**
	 * Returns a recursively constructed list of discarded regions that together form a recovery for the term 
	 * @return
	 */
	public ArrayList<DiscardableRegion> getDamagedRegions(){
		if(this.getSubtermRecoveries() == null){ //trivial recovery (no damage)
			return new ArrayList<DiscardableRegion>(); 
		}
		else if(isRecovered){ //only damage in tokens associated to subterms 
			return getDamagedRegionsForUnaffectedTerm(); 
		}
		else { //damage in tokens associated to term
			return getDamagedRegionsForAffectedTerm();
		}
	}

	/**
	 * Returns a recursively constructed list of terms for which the associate tokens are discarded as a recovery 
	 * @return
	 */
	public ArrayList<IStrategoTerm> getDamagedTerms(){
		ArrayList<IStrategoTerm> damagedTerms = new ArrayList<IStrategoTerm>();
		if(this.getSubtermRecoveries() == null){ //trivial recovery (no damage)
			return new ArrayList<IStrategoTerm>();
		}
		if(!isRecovered && hasNonLayoutTokenDiscards()){
			damagedTerms.add(getTerm());
		}
		for (int i = 0; i < this.getSubtermRecoveries().size(); i++) {
			damagedTerms.addAll(getSubtermRecoveries().get(i).getDamagedTerms());
		}
		return damagedTerms;
	}

	private boolean hasNonLayoutTokenDiscards() {
		int recoverCostsSubterms = 0;
		for (RecoverInterpretation subtermRecovery : getSubtermRecoveries()) {
			recoverCostsSubterms += subtermRecovery.getRecoveryCosts();
		}
		assert recoveryCosts >= recoverCostsSubterms;
		return recoveryCosts > recoverCostsSubterms;
	}
	
	@Override
	public String toString(){
		ArrayList<DiscardableRegion> damagedRegions = getDamagedRegions();
		String inputString = getInputString();
		String recoveredProgram = DiscardableRegion.replaceAllRegionsByWhitespace(damagedRegions, inputString);
		int startOffset = ImploderAttachment.getLeftToken(term).getStartOffset();
		int endOffset = ImploderAttachment.getRightToken(term).getEndOffset();
		String recoveredTermFragment = recoveredProgram.substring(startOffset, endOffset + 1);
		return recoveredTermFragment;
	}


	
// constructors
	
	/**
	 * Returns a trivial recover interpretation by taking the original interpretation.
	 * Intended for undamaged terms. 
	 * @param term
	 * @param parentTerm
	 * @return
	 */
	public static RecoverInterpretation createOriginalTermInterpretation(IStrategoTerm term, IStrategoTerm parentTerm){
		return new RecoverInterpretation(term, parentTerm, true, null);
		//Remark: recursively constructing original term recoveries for subterms seems not needed. 
	}

	/**
	 * Returns a recover interpretation based on discarding the characters associated to the term and all its subterms.
	 * Intended as fall back recovery for list elements and 'Some(_)' terms.  
	 * @param term
	 * @param parentTerm
	 * @return
	 */
	public static RecoverInterpretation createDiscardInterpretation(IStrategoTerm term, IStrategoTerm parentTerm){
		assert term.isList() || HelperFunctions.isSomeNode(term) || parentTerm == null; //parentTerm.isList()
		return new RecoverInterpretation(term, parentTerm, false, new ArrayList<RecoverInterpretation>());
	}

	/**
	 * Returns a recover interpretation based on repairing sublists with correct separation in between.
	 * @param term
	 * @param parentTerm
	 * @param recoveredSubterms
	 * @return
	 */
	public static RecoverInterpretation createRepairSublistsInterpretation(IStrategoTerm term, IStrategoTerm parentTerm, ArrayList<RecoverInterpretation> recoveredSublists){
		assert !recoveredSublists.contains(null);
		//assert sublists cover list (?)
		return new RecoverInterpretation(term, parentTerm, true, recoveredSublists);
	}

	/**
	 * Returns a recover interpretation based on repairing the subterms
	 * Intended for terms that are not affected themselves, but do have changes in their subterms,
	 * whereby the child terms can be recovered.
	 * @param term
	 * @param parentTerm
	 * @param recoveredSubterms
	 * @return
	 */
	public static RecoverInterpretation createRepairSubtermsInterpretation(IStrategoTerm term, IStrategoTerm parentTerm, ArrayList<RecoverInterpretation> recoveredSubterms){
		assert recoveredSubterms.size() == term.getSubtermCount() && !recoveredSubterms.contains(null);
		return new RecoverInterpretation(term, parentTerm, true, recoveredSubterms);
	}

	/**
	 * Returns a recover interpretation by replacing the term with a sub-term of the same sort.
	 * Intended for terms that are affected themselves, or terms whereby the child terms can not be repaired.
	 * @param term
	 * @param parentTerm
	 * @param recoveredSubterm
	 * @return
	 */
	public static RecoverInterpretation createReplaceBySubtermsInterpretation(IStrategoTerm term, IStrategoTerm parentTerm, RecoverInterpretation recoveredSubterm){
		assert recoveredSubterm.hasCompatibleSort(term, parentTerm);
		ArrayList<RecoverInterpretation> subRecoveries = new ArrayList<RecoverInterpretation>();
		subRecoveries.add(recoveredSubterm);
		return createReplaceBySubtermsInterpretation(term, parentTerm, subRecoveries);
	}

	/**
	 * Returns a recover interpretation by replacing the terms with a list of sub terms of the same sort.
	 * Intended for list elements that are affected themselves, or list elements whereby the child terms can not be repaired.
	 * @param term
	 * @param parentTerm
	 * @param recoveredSubterms
	 * @return
	 */
	public static RecoverInterpretation createReplaceBySubtermsInterpretation(IStrategoTerm term, IStrategoTerm parentTerm, ArrayList<RecoverInterpretation> recoveredSubterms){
		assert (parentTerm.isList() && recoveredSubterms.size() >= 1) || recoveredSubterms.size() == 1;
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
		assert subtermRecoveries == null? (isRecovered && this.recoveryCosts == 0) : recoveryCosts >= 0;
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
		ITokens tokens = getTokenizer(term);
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
	
	private ArrayList<DiscardableRegion> getDamagedRegionsForAffectedTerm() {
		String input = getInputString();
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
	
	private ArrayList<DiscardableRegion> getDamagedRegionsForUnaffectedTerm() {
		ArrayList<DiscardableRegion> damagedRegions = new ArrayList<DiscardableRegion>();
		for (RecoverInterpretation subtermRecovery : getSubtermRecoveries()) {
			damagedRegions.addAll(subtermRecovery.getDamagedRegions());
		}
		return damagedRegions;
	}

	private String getInputString() {
		return ImploderAttachment.getTokenizer(getTerm()).getInput();
	}

	public boolean isDiscardRecovery() {
		return getSubtermRecoveries() != null && getSubtermRecoveries().isEmpty();
	}
}
