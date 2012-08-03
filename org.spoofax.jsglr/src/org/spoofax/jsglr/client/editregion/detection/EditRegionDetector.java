package org.spoofax.jsglr.client.editregion.detection;

import java.util.ArrayList;
import java.util.Collections;
import org.spoofax.jsglr.client.editregion.detection.LCS;
import org.spoofax.jsglr.client.editregion.detection.LCSCommand;
import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.jsglr.client.imploder.ITokenizer;
import org.spoofax.jsglr.client.imploder.ImploderAttachment;

/**
 * Detects possible erroneous code constructs and comments
 * by comparing a possibly erroneous input string with a (similar) correctly parsed input string.
 * The correctly parsed input string is typically a prior, non-erroneous snapshot of the program being edited.
 * The discardable, erroneous code constructs can be used to construct a non-correcting recovery and to 
 * locate parse errors prior to applying a correcting recovery technique.
 * @author maartje
 */
public class EditRegionDetector {

	private final LCS<Character> lcs;
	private final String erroneousInput;
	private final IStrategoTerm correctAST;
	
	//edit regions found after analyzing the correct AST/input and its deleted characters
	private ArrayList<DiscardableRegion> discardableRegions;
	private ArrayList<DiscardableRegion> discardableCommentRegions;


//methods that access the correct parse input string

	/**
	 * Returns the input string of the correctly parsed AST
	 * @return Correct input string used for comparison
	 */
	public String getCorrectInput(){
		return ImploderAttachment.getTokenizer(correctAST).getInput();
	}

	/**
	 * Returns the offsets of the characters that are deleted from the correct input (found by an LCS diff algorithm)
	 * @return offsets of deleted characters
	 */
	public ArrayList<Integer> getDeletionOffsets(){
		return lcs.getUnMatchedIndices1();		
	}

	/**
	 * Returns the substrings that are deleted from the correct input
	 * @return deleted substrings
	 */
	public ArrayList<String> getDeletedSubstrings(){
		return constructSubstringsFromOffsets(getDeletionOffsets(), getCorrectInput());
	}

	/**
	 * Returns the discarded regions of code that were edited, from the correct input 
	 * @return discarded edit regions from the correct input
	 */
	public ArrayList<DiscardableRegion> getEditedRegionsCorrect(){
		return mergeRegions(discardableRegions, discardableCommentRegions);
	}
	
	/**
	 * Returns the offsets of the chars that together form the discardable text fragments, from the correct input 
	 * @return offsets of characters in discarded regions
	 */
	public ArrayList<Integer> getDiscardOffsetsCorrectInput() {
		return getEditOffsets(getEditedRegionsCorrect());
	}
	
	/**
	 * Returns the (discardable) terms that were edited, from the correct input 
	 * @return discarded terms
	 */
	public ArrayList<IStrategoTerm> getEditedTerms(){
		ArrayList<IStrategoTerm> discardableTerms = new ArrayList<IStrategoTerm>();
		for (DiscardableRegion region : getEditedRegionsCorrect()) {
			if(!HelperFunctions.contains(discardableTerms, region.getAffectedTerm()))
				discardableTerms.add(region.getAffectedTerm());
		}
		return discardableTerms;
	}	


//methods that access the erroneous parse input string

	/**
	 * Returns the (possible) erroneous input string 
	 * @return Erroneous input
	 */
	public String getErroneousInput(){
		return erroneousInput;
	}

	/**
	 * Returns the offsets of the characters that are inserted in the erroneous input (found by an LCS diff algorithm)
	 * @return offsets of inserted characters
	 */
	public ArrayList<Integer> getInsertionOffsets(){
		return lcs.getUnMatchedIndices2();
	}

	/**
	 * Returns the substrings that are inserted in the erroneous input
	 * @return inserted substrings
	 */
	public ArrayList<String> getInsertedSubstrings(){
		return constructSubstringsFromOffsets(getInsertionOffsets(), getErroneousInput());
	}

	/**
	 * Returns the discardable regions of code that were edited in the erroneous input
	 * These regions may contain syntax errors. 
	 * @return edited regions from erroneous input
	 */
	public ArrayList<DiscardableRegion> getEditedRegionsErroneous(){
		ArrayList<DiscardableRegion> editsFromDeletions = mapRegions(getEditedRegionsCorrect(), true);
		ArrayList<DiscardableRegion> editsFromInsertions = this.constructRegionsFromOffsets(getInsertionOffsets(), getErroneousInput());
		return mergeRegions(editsFromDeletions, editsFromInsertions);
	}

	/**
	 * Returns the offsets of the chars that together form the edited text fragments, 
	 * from the erroneous input 
	 * @return offsets of edit regions from erroneous input
	 */
	public ArrayList<Integer> getDiscardOffsetsErroneousInput() {
		return getEditOffsets(getEditedRegionsErroneous());
	}

// method for accessing the recovered input
	
	/**
	 * Returns a (presumably) correct parse input based on the LCS of a correct and incorrect program.
	 * The constructed parse input forms a (non-correcting) recovery of the erroneous input. 
	 * The constructed parse input is obtained by replacing the characters in the erroneous regions by whitespaces.
	 * @return recovered program
	 */
	public String getRecoveredInput(){
		String recoveredProgram = replaceAllRegionByWhitespace(getEditedRegionsErroneous(), getErroneousInput());
		//assert recoveredProgram.equals(replaceAllRegionByWhitespace(getEditedRegionsCorrect(), correctInput)); //modulo comments and layout
		assert recoveredProgram.length() == getErroneousInput().length(): "whitespaces are inserted for characters in the edit regions";
		return recoveredProgram;
	}

	
	/**
	 * Detects possible erroneous code constructs and comments
	 * by comparing an input string with a very similar, correctly parsed, input string.
	 * The correctly parsed input string is typically a prior (non-erroneous) snapshot of the program being edited.
	 */	
	public EditRegionDetector(IStrategoTerm correctAST, String erroneousInput){
		this.correctAST = correctAST;
		this.erroneousInput = erroneousInput;
		lcs = new LCS<Character>(new LCSCommand<Character>() {
			public boolean isMatch(Character c1, Character c2) {
				return c1.charValue() == c2.charValue();
			}});
		detectEditRegions();
	}
	
	private void detectEditRegions() {
		ITokenizer tokens = ImploderAttachment.getTokenizer(correctAST);

		// calculates offsets deleted characters (correct input), and offsets inserted characters (erroneous input)
		constructCharacterMatching();
		ArrayList<Integer> offsetsDeletedChars = lcs.getUnMatchedIndices1();

		//removes from deletion offsets, all offsets of layout characters that are irrelevant for the parse result.
		//detects all edited comment regions, since these may affect the parse result if they are broken.
		LayoutEditsAnalyzer loAnalyzer = new LayoutEditsAnalyzer(tokens, lcs);
		this.discardableCommentRegions = loAnalyzer.getDamagedCommentRegions();
		loAnalyzer.filterNonLayoutOffsets(offsetsDeletedChars);
		
		//extends deletion offsets so that all possible damaged tokens are covered
		TerminalEditsAnalyzer terminalAnalyzer = new TerminalEditsAnalyzer(tokens, lcs);
		terminalAnalyzer.addDamagedTokensStartOffsets(offsetsDeletedChars);

		//detects discardable regions that correspond to edited terms.
		//NonTerminalEditsAnalyzer brokenConstructDetector = new NonTerminalEditsAnalyzer(correctAST, offsetsDeletedChars);
		//this.discardableRegions = brokenConstructDetector.getDamagedTermRegions();
		
		//detects discardable regions that correspond to edited terms.
		TermEditsAnalyzer brokenConstructDetector = new TermEditsAnalyzer(offsetsDeletedChars, correctAST);
		this.discardableRegions = brokenConstructDetector.getDamagedTermRegions();		
	}
	


// helper functions
	private void constructCharacterMatching() {
		ArrayList<Character> correctChars = asCharacterList(getCorrectInput());
		ArrayList<Character> erroneousChars = asCharacterList(getErroneousInput());
		//long startTime = System.currentTimeMillis();
		lcs.createLCSResultsOptimized(correctChars, erroneousChars);
		//System.out.println("duration lcs: " + (System.currentTimeMillis() - startTime));
	}
	
	
	private static ArrayList<Character> asCharacterList(String correctInput) {
		ArrayList<Character> correctChars = new ArrayList<Character>();
		for (int i = 0; i < correctInput.length(); i++) {
			correctChars.add(correctInput.charAt(i));
		}
		return correctChars;
	}

	private DiscardableRegion mapRegion(DiscardableRegion region, boolean isInCorrectInputString){
		int startOffset = -1;
		int endOffset = -1;
		for (int offset = region.getStartOffset(); offset <= region.getEndOffset(); offset++) {
			int correspondingOffset = lcs.getMatchIndex(offset, isInCorrectInputString);
			if(correspondingOffset != -1){
				if(startOffset == -1)
					startOffset = correspondingOffset;
				endOffset = correspondingOffset;
			}
		}
		if(startOffset >= 0){
			return new DiscardableRegion(startOffset, endOffset, null);
		}
		return null;
	}

	private ArrayList<DiscardableRegion> mapRegions(ArrayList<DiscardableRegion> regions, boolean isInCorrectInputString){
		ArrayList<DiscardableRegion> result = new ArrayList<DiscardableRegion>();		
		for (DiscardableRegion region : regions) {
			DiscardableRegion mappedRegion = mapRegion(region, isInCorrectInputString);
			if(mappedRegion != null)
				result.add(mappedRegion);
		}
		return result;
	}
	
	private ArrayList<DiscardableRegion> mergeRegions(ArrayList<DiscardableRegion> regions1, ArrayList<DiscardableRegion> regions2){
		ArrayList<DiscardableRegion> merged = new ArrayList<DiscardableRegion>();
		int index_r1 = 0;
		int index_r2 = 0;
		while (index_r1 < regions1.size() && index_r2 < regions2.size()) {
			DiscardableRegion r1 = regions1.get(index_r1);			
			DiscardableRegion r2 = regions2.get(index_r2);
			if(r1.getEndOffset() < r2.getStartOffset()){
				merged.add(r1);
				index_r1 ++;
			}
			else if(r2.getEndOffset() < r1.getStartOffset()){
				merged.add(r2);
				index_r2 ++;
			}
			else {
				int startOffset = Math.min(r1.getStartOffset(), r2.getStartOffset());
				int endOffset = Math.max(r1.getEndOffset(), r2.getEndOffset());
				DiscardableRegion mergedRegion = new DiscardableRegion(startOffset, endOffset, null);
				merged.add(mergedRegion);
				index_r1++;
				index_r2++;
			}
		}
		while (index_r1 < regions1.size()) {
			DiscardableRegion r1 = regions1.get(index_r1);
			merged.add(r1);
			index_r1 ++;
		}
		while (index_r2 < regions2.size()) {
			DiscardableRegion r2 = regions2.get(index_r2);
			merged.add(r2);
			index_r2++;
		}
		return merged;
	}

	private ArrayList<DiscardableRegion> constructRegionsFromOffsets(ArrayList<Integer> offsets, String inputString){
		ArrayList<DiscardableRegion> result = new ArrayList<DiscardableRegion>();
		int startOffset = -1;
		for (int i = 0; i < offsets.size(); i++) {
			int offset = offsets.get(i);
			if(i==0){
				startOffset = offsets.get(i);
			}
			else if (offset != offsets.get(i-1) + 1){
				DiscardableRegion region = new DiscardableRegion(startOffset, offsets.get(i-1), null);
				result.add(region);
				startOffset = offsets.get(i);
			}
		}
		if(startOffset != -1){
			DiscardableRegion region = new DiscardableRegion(startOffset, offsets.get(offsets.size()-1), null);
			result.add(region);
		}
		return result;		
	}

	private ArrayList<String> constructSubstringsFromOffsets(ArrayList<Integer> offsets, String inputString){
		ArrayList<DiscardableRegion> regions = constructRegionsFromOffsets(offsets, inputString);
		ArrayList<String> result = new ArrayList<String>();
		for (DiscardableRegion region : regions) {
			String fragment = inputString.substring(region.getStartOffset(), region.getEndOffset() + 1);
			result.add(fragment);
		}
		return result;
	}
	
	private String replaceRegionByWhitespace(DiscardableRegion region, String input) {
		char[] chars = input.toCharArray();
		for (int offset = region.getStartOffset(); offset <= region.getEndOffset(); offset++) {
			char charAtOffset = chars[offset];
			if(!Character.isWhitespace(charAtOffset)){
				chars[offset] = ' ';
			}
		}
		return String.valueOf(chars);
	}

	private String replaceAllRegionByWhitespace(ArrayList<DiscardableRegion> regions, String input) {
		String result = input;
		for (DiscardableRegion region : regions) {
			result = replaceRegionByWhitespace(region, result);
		}
		return result;
	}
		
	private ArrayList<Integer> getEditOffsets(ArrayList<DiscardableRegion> editRegions) {
		ArrayList<Integer> result = new ArrayList<Integer>();
		for (DiscardableRegion region : editRegions) {
			int startOffset = region.getStartOffset();
			int endOffset = region.getEndOffset();
			for (int offset = startOffset; offset <= endOffset; offset++) {
				// "regions can overlap, for example: term + separation and comment in separation";
				if(!result.contains(offset))
					result.add(offset);
			}
		}
		Collections.sort(result);
		return result;
	}

	
}
