package org.spoofax.jsglr.client.imploder;

/**
 * @author Lennart Kats <lennart add lclnet.nl>
 */
public interface ITokenizer {
	
	void initialize(char[] inputChars);

	char[] getInputChars();

	int getStartOffset();

	void setStartOffset(int startOffset);

	IToken currentToken();

	int getTokenCount();

	IToken getTokenAt(int i);

	IToken makeToken(int offset, LabelInfo label);

	IToken makeToken(int offset, LabelInfo label, boolean allowEmptyToken);

	IToken makeToken(int offset, int kind, boolean allowEmptyToken);

	/**
	 * Creates an artificial token at keyword boundaries
	 * inside skipped regions of code.
	 * Required for keyword highlighting with {@link KeywordRecognizer}.
	 */
	IToken createSkippedToken(int offset, char inputChar, char prevChar);

	/**
	 * Creates an artificial token for every water-based recovery
	 * and for comments within layout.
	 */
	void createLayoutToken(int offset, int lastOffset, LabelInfo label);

}