package org.spoofax.jsglr.client.editregion.detection;

import java.util.ArrayList;

import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.jsglr.client.imploder.AbstractTokenizer;
import org.spoofax.jsglr.client.imploder.ITokens;
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
	
	//discard recovery found after analyzing the correct AST/input and its deleted characters
	private RecoverInterpretation discardRecovery;
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
		if(discardRecovery == null){
			RecoverInterpretation emptyRecovery = RecoverInterpretation.createDiscardInterpretation(correctAST, null);
			return emptyRecovery.getDamagedRegions();
		}
		return 
				DiscardableRegion.mergeSubsequentRegions(
						DiscardableRegion.mergeRegions(discardRecovery.getDamagedRegions(), discardableCommentRegions)
				);
	}
	
	/**
	 * Returns the offsets of the chars that together form the discardable text fragments, from the correct input 
	 * @return offsets of characters in discarded regions
	 */
	public ArrayList<Integer> getDiscardOffsetsCorrectInput() {
		return DiscardableRegion.getOffsets(getEditedRegionsCorrect());
	}
	
	/**
	 * Returns the (discardable) terms that were edited, from the correct input 
	 * @return discarded terms
	 */
	public ArrayList<IStrategoTerm> getEditedTerms(){
		return discardRecovery.getDamagedTerms();
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
		ArrayList<DiscardableRegion> editsFromInsertions = DiscardableRegion.constructRegionsFromOffsets(getInsertionOffsets(), this.getErroneousInput());
		return 
				DiscardableRegion.mergeSubsequentRegions(
						DiscardableRegion.mergeRegions(editsFromDeletions, editsFromInsertions)
				);
	}

	/**
	 * Returns the offsets of the chars that together form the edited text fragments, 
	 * from the erroneous input 
	 * @return offsets of edit regions from erroneous input
	 */
	public ArrayList<Integer> getDiscardOffsetsErroneousInput() {
		return DiscardableRegion.getOffsets(getEditedRegionsErroneous());
	}

// method for accessing the recovered input
	
	/**
	 * Returns a (presumably) correct parse input based on the LCS of a correct and incorrect program.
	 * The constructed parse input forms a (non-correcting) recovery of the erroneous input. 
	 * The constructed parse input is obtained by replacing the characters in the erroneous regions by whitespaces.
	 * @return recovered program
	 */
	public String getRecoveredInput(){
		String recoveredProgram = DiscardableRegion.replaceAllRegionsByWhitespace(getEditedRegionsErroneous(), getErroneousInput());
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
		ITokens tokens = ImploderAttachment.getTokenizer(correctAST);

		long time = System.currentTimeMillis();
		
		// calculates offsets deleted characters (correct input), and offsets inserted characters (erroneous input)
		constructCharacterMatching();
		ArrayList<Integer> offsetsDeletedChars = lcs.getUnMatchedIndices1();
		System.out.println("LCS: " + (System.currentTimeMillis()-time));
		time = System.currentTimeMillis();
		//TODO: LCS on lines (instead of characters) in case LCS optimized does not work to reduce the size enough
		
		DamagedTokenAnalyzer tokenEdits = new DamagedTokenAnalyzer((AbstractTokenizer) tokens, lcs);
		System.out.println("token edits: " + (System.currentTimeMillis()-time));
		time = System.currentTimeMillis();
		//TODO: Improve performance!!

		//removes from deletion offsets, all offsets of layout characters that are irrelevant for the parse result.
		//detects all edited comment regions, since these may affect the parse result if they are broken.
		LayoutEditsAnalyzer loAnalyzer = new LayoutEditsAnalyzer(tokenEdits);
		this.discardableCommentRegions = loAnalyzer.getDamagedCommentRegions();
		loAnalyzer.filterNonLayoutOffsets(offsetsDeletedChars);
		System.out.println("layout edits: " + (System.currentTimeMillis()-time));
		time = System.currentTimeMillis();

		//extends deletion offsets so that all possible damaged tokens are covered
		TerminalEditsAnalyzer terminalAnalyzer = new TerminalEditsAnalyzer(tokenEdits);
		terminalAnalyzer.addDamagedTokensStartOffsets(offsetsDeletedChars);
		System.out.println("terminal edits: " + (System.currentTimeMillis()-time));
		time = System.currentTimeMillis();

		//detects discardable regions that correspond to edited terms.
		//NonTerminalEditsAnalyzer brokenConstructDetector = new NonTerminalEditsAnalyzer(correctAST, offsetsDeletedChars);
		//this.discardableRegions = brokenConstructDetector.getDamagedTermRegions();
		
		//detects discardable regions that correspond to edited terms.
		TermEditsAnalyzer brokenConstructDetector = new TermEditsAnalyzer(offsetsDeletedChars, correctAST);
		this.discardRecovery = brokenConstructDetector.getDiscardRecovery();		
		System.out.println("Term edits: " + (System.currentTimeMillis()-time));
		time = System.currentTimeMillis();
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
		String input = this.getCorrectInput();
		if(isInCorrectInputString){
			input = this.getErroneousInput();
		}

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
			return new DiscardableRegion(startOffset, endOffset, input);
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
	

	private ArrayList<String> constructSubstringsFromOffsets(ArrayList<Integer> offsets, String inputString){
		ArrayList<DiscardableRegion> regions = DiscardableRegion.constructRegionsFromOffsets(offsets, inputString);
		return DiscardableRegion.constructFragments(regions);
	}
}
