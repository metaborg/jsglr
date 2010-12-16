package org.spoofax.jsglr.client.imploder;

import org.spoofax.jsglr.client.KeywordRecognizer;

/**
 * @author Lennart Kats <lennart add lclnet.nl>
 */
public interface ITokenizer extends Iterable<IToken> {

	String getInput();

	int getStartOffset();

	void setStartOffset(int startOffset);

	IToken currentToken();

	int getTokenCount();

	IToken getTokenAt(int i);

	IToken makeToken(int endOffset, LabelInfo label, boolean allowEmptyToken);

	IToken makeToken(int endOffset, int kind, boolean allowEmptyToken);

	/**
	 * Creates artificial token at keyword boundaries
	 * inside skipped regions of code when
	 * invoked for each character in a skipped/erroneous region of code.
	 * Required for keyword highlighting with {@link KeywordRecognizer}.
	 * 
	 * @param offset
	 *           The offset of the 
	 */
	void makeErrorToken(int endOffset);

	/**
	 * Creates an artificial token for every water-based recovery
	 * and for comments within layout.
	 */
	void makeLayoutToken(int endOffset, int lastOffset, LabelInfo label);

	String toString(IToken left, IToken right);

	String getFilename();

}