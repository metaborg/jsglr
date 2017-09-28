package org.spoofax.jsglr.client.editregion.detection;

import java.util.ArrayList;
import org.spoofax.jsglr.client.editregion.detection.LCS;
import org.spoofax.jsglr.client.imploder.AbstractTokenizer;
import org.spoofax.jsglr.client.imploder.IToken;
import org.spoofax.jsglr.client.imploder.ITokens;
import org.spoofax.jsglr.client.imploder.Token;

/**
 * Determines the tokens in the correct term that are (possible) damaged
 * by either the deletion of characters that form the token,
 * or the insertion of characters between the start and end character of the token.   
 * Example insertion: private => priva$te 
 * Example deletion: private => privte 
 * @author Maartje de Jonge
 *
 */
public class DamagedTokenAnalyzer {

	private final AbstractTokenizer tokens;
	private final LCS<Character> lcs;
	
	//filled in the token stream analysis
	private final ArrayList<IToken> tokensDamagedByInsertion;
	private final ArrayList<IToken> tokensDamagedByDeletion;

	/**
	 * Returns tokens in token stream
	 * @return
	 */
	public ITokens getTokens() {
		return tokens;
	}

	/**
	 * Returns all tokens in the correct term that are (possible) damaged by
	 * the insertion of characters between the start and end character of the token.   
	 * Example: private => priva$te 
	 */
	public ArrayList<IToken> getTokensDamagedByInsertion() {
		ArrayList<IToken> result = new ArrayList<IToken>();
		result.addAll(tokensDamagedByInsertion);
		return result;
	}

	/**
	 * Returns all tokens in the correct term that are (possible) damaged by
	 * the deletion of characters that form the token.   
	 * Example: private => privte 
	 */
	public ArrayList<IToken> getTokensDamagedByDeletion() {
		ArrayList<IToken> result = new ArrayList<IToken>();
		result.addAll(tokensDamagedByDeletion);
		return result;
	}
	
	/**
	 * Determines the tokens in the correct term that are (possible) damaged
	 * by edit operations.
	 */
	public DamagedTokenAnalyzer(AbstractTokenizer tokens, LCS<Character> lcs){
		this.tokens = tokens;
		this.lcs = lcs;
		this.tokensDamagedByInsertion = new ArrayList<IToken>();
		this.tokensDamagedByDeletion = new ArrayList<IToken>();
		analyzeDamagedTokens();
	}

	/**
	 * Says whether a token in the (correctly) parsed input has deleted characters in the (possible) erroneous input 
	 * @param t
	 * @return
	 */
	public boolean isDamagedByDeletion(IToken t) {
		ArrayList<Integer> deletions = getOffsetsDeletions(t);
		return !deletions.isEmpty();
	}

	/**
	 * Returns the offsets of characters in the token that are deleted in the (possible) erroneous input 
	 * @param t
	 * @return
	 */
	public ArrayList<Integer> getOffsetsDeletions(IToken t) {
		int startOffset = t.getStartOffset();
		int endOffset = t.getEndOffset();
		ArrayList<Integer> deletions = new ArrayList<Integer>();		
		for (int j = startOffset; j <= endOffset; j++) {
			if(lcs.getMatchIndexInElems2(j) == -1){
				deletions.add(j);
			}
		}
		return deletions;
	}
	
	/**
	 * Says whether a token in the (correctly) parsed input has inserted characters in the (possible) erroneous input 
	 * @param t
	 * @return
	 */
	public boolean isDamagedByInsertion(IToken t) {
		int startOffset = t.getStartOffset();
		int endOffset = t.getEndOffset();		
		int offsetPreviousMatch = -1;
		for (int j = startOffset; j <= endOffset; j++) {
			int matchIndexInElems2 = lcs.getMatchIndexInElems2(j);
			if(offsetPreviousMatch != -1 && matchIndexInElems2 > offsetPreviousMatch + 1){
				return true;
			}
			offsetPreviousMatch = matchIndexInElems2;
		}
		return false;
	}
	

	/**
	 * Says whether a token is a layout token between two non-layout tokens that has been completely removed,
	 * and therefore may affect the parse result.
	 * @param token
	 * @return
	 */
	public boolean isDamagingLayoutDeletion(IToken token) {
		int numberOfDeletions = getOffsetsDeletions(token).size();
		int startOffset = token.getStartOffset();
		int endOffset = token.getEndOffset();
		if (numberOfDeletions == endOffset - startOffset + 1){ //full deletion
			int firstMatchedOffsetInPrefix = -1;
			int firstMatchedOffsetInSuffix = -1;
			for (int offsetInPrefix = startOffset -1; offsetInPrefix >= 0; offsetInPrefix--) {
				if(lcs.getMatchIndexInElems2(offsetInPrefix) != -1){
					firstMatchedOffsetInPrefix = offsetInPrefix;
					break;
				}
			}
			for (int offsetInSuffix = endOffset + 1; offsetInSuffix < lcs.getElems1().size(); offsetInSuffix++) {
				if(lcs.getMatchIndexInElems2(offsetInSuffix) != -1){
					firstMatchedOffsetInSuffix = offsetInSuffix;
					break;
				}
			}
			int firstMatchedOffsetInPrefix_2 = lcs.getMatchIndexInElems2(firstMatchedOffsetInPrefix);
			int firstMatchedOffsetInSuffix_2 = lcs.getMatchIndexInElems2(firstMatchedOffsetInSuffix);
			return //checks if all layout is deleted between the surrounding tokens in elems2
				firstMatchedOffsetInSuffix_2 == firstMatchedOffsetInPrefix_2 + 1 && //no insertions that are discarded as whitespace
				firstMatchedOffsetInPrefix_2 != -1 &&
				firstMatchedOffsetInSuffix_2 != -1 &&
				!isOffsetOfLayoutChar((AbstractTokenizer) token.getTokenizer(), firstMatchedOffsetInPrefix) &&
				!isOffsetOfLayoutChar((AbstractTokenizer) token.getTokenizer(), firstMatchedOffsetInSuffix);
		}
		return false;
	}

	private boolean isOffsetOfLayoutChar(AbstractTokenizer tokens, int offset) {
		boolean precedingLayout = tokens.getTokenAtOffset(offset).getKind() == Token.TK_LAYOUT;
		return precedingLayout;
	}
	
	private void analyzeDamagedTokens() {
		for (int i = 0; i < tokens.getTokenCount(); i++) {
			IToken t = tokens.getTokenAt(i);
			//hack to improve the performance
			boolean possibleDamaged = 
				t.getEndOffset() >= lcs.getMatchedPrefixSize() - 1 && 
				t.getStartOffset() <= lcs.getElems1().size() - lcs.getMatchedSuffixSize() + 1; 
			if(possibleDamaged){
				if (isDamagedByDeletion(t)){
					tokensDamagedByDeletion.add(t);
				}
				if (isDamagedByInsertion(t)){
					tokensDamagedByInsertion.add(t);
				}
			}
		}
	}
}
