package org.spoofax.jsglr.client.imploder;

import org.spoofax.interpreter.terms.ISimpleTerm;
import org.spoofax.jsglr.client.IKeywordRecognizer;

/**
 * @author Lennart Kats <lennart add lclnet.nl>
 */
public interface ITokenizer {

	int getStartOffset();

	void setStartOffset(int startOffset);

	IToken currentToken();
	
	int getEndLine();
	
	int getEndColumn();
	
	int getLineAtOffset(int offset);

	IToken makeToken(int endOffset, LabelInfo label, boolean allowEmptyToken);

	IToken makeToken(int endOffset, int kind, boolean allowEmptyToken);
	
	/**
	 * Gets a token at the given offset, or creates an adjunct
	 * token with that offset, used for error reporting.
	 */
	IToken getErrorTokenOrAdjunct(int offset);
	
	/**
	 * Creates artificial token at keyword boundaries
	 * inside skipped regions of code when
	 * invoked for each character in a skipped/erroneous region of code.
	 * Required for keyword highlighting with {@link IKeywordRecognizer}.
	 * 
	 * Additionally, ensures that {@link #isSyntaxCorrect()} returns false.
	 * 
	 * @param offset
	 *           The offset of the 
	 */
	void tryMakeSkippedRegionToken(int endOffset);

	/**
	 * Creates an artificial token for every water-based recovery
	 * and for comments within layout.
	 */
	void tryMakeLayoutToken(int endOffset, int lastOffset, LabelInfo label);
	
	/**
	 * Marks a possible syntax error (if indicated by the given label),
	 * starting *after* the given token, ending at the given offset.
	 */
	void markPossibleSyntaxError(LabelInfo label, IToken firstToken, int endOffset, ProductionAttributeReader prodReader);
	
	boolean isSyntaxCorrect();
	
	void setSyntaxCorrect(boolean syntaxCorrect);
	
	void setAmbiguous(boolean isAmbiguous);

	void setAst(ISimpleTerm ast);

	/**
	 * Initializes the {@link IToken#getAstNode()} method of each
	 * token in this token stream.
	 */
	void initAstNodeBinding();
}