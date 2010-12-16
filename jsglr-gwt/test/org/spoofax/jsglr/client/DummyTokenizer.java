package org.spoofax.jsglr.client;

/**
 * @author Lennart Kats <lennart add lclnet.nl>
 */
public class DummyTokenizer implements ITokenizer {
	
	private final IToken onlyToken = new Token(this, 0, 0, 0, IToken.TK_UNKNOWN);
	
	private char[] inputChars;
	
	/**
	 * @param inputChars
	 *           The input characters (used for {indentpadding} productions
	 *           and error recovery)
	 */
	public void initialize(char[] inputChars) {
		this.inputChars = inputChars;
	}

	public char[] getInputChars() {
		return inputChars;
	}

	public int getStartOffset() {
		return 0;
	}

	public void setStartOffset(int startOffset) {
		// Do nothing		
	}

	public IToken currentToken() {
		return onlyToken;
	}

	public int getTokenCount() {
		return 0;
	}

	public IToken getTokenAt(int i) {
		return onlyToken;
	}

	public IToken makeToken(int offset, LabelInfo label) {
		return onlyToken;
	}

	public IToken makeToken(int offset, LabelInfo label, boolean allowEmptyToken) {
		return onlyToken;
	}

	public IToken makeToken(int offset, int kind, boolean allowEmptyToken) {
		return onlyToken;
	}

	public IToken createSkippedToken(int offset, char inputChar, char prevChar) {
		return onlyToken;
	}

	public void createLayoutToken(int offset, int lastOffset, LabelInfo label) {
		// Do nothing
	}

}
