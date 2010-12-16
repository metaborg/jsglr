package org.spoofax.jsglr.client.imploder;

import static org.spoofax.jsglr.client.imploder.IToken.*;

import java.util.ArrayList;

/**
 * @author Lennart Kats <lennart add lclnet.nl>
 */
public class Tokenizer implements ITokenizer {
	
	private static final int EXPECTED_TOKENS_DIVIDER = 5;
	
	private final TokenKindManager manager =
		new TokenKindManager();
	
	private char[] inputChars;
	
	/** Start of the next token. */
	private int startOffset;

	private ArrayList<IToken> tokens;
	
	public void initialize(char[] inputChars) {
		this.inputChars = inputChars;
		this.tokens = new ArrayList<IToken>(inputChars.length / EXPECTED_TOKENS_DIVIDER);
		startOffset = 0;
	}
	
	public final char[] getInputChars() {
		return inputChars;
	}
	
	public final int getStartOffset() {
		return startOffset;
	}

	public void setStartOffset(int startOffset) {
		this.startOffset = startOffset;
	}

	public IToken currentToken() {
		return tokens.size() == 0
			? null
			:tokens.get(tokens.size() - 1);
	}
	
	public int getTokenCount() {
		return tokens.size();
	}
	
	public IToken getTokenAt(int i) {
		return tokens.get(i);
	}

	public IToken makeToken(int offset, LabelInfo label) {
		return makeToken(offset, label, false);
	}

	public IToken makeToken(int endOffset, LabelInfo label, boolean allowEmptyToken) {
		return makeToken(endOffset, manager.getTokenKind(label), allowEmptyToken);
	}
	
	public IToken makeToken(int endOffset, int kind, boolean allowEmptyToken) {
		if (!allowEmptyToken && startOffset >= endOffset) // empty token
			return null;
		
		assert endOffset >= startOffset || (kind == TK_RESERVED && startOffset == 0);
		IToken result = new Token(this, tokens.size() - 1, endOffset, endOffset, kind);
		tokens.add(result);
		return result;
	}

	/**
	 * Creates an artificial token at keyword boundaries
	 * inside skipped regions of code.
	 * Required for keyword highlighting with {@link KeywordRecognizer}.
	 */
	public IToken createSkippedToken(int offset, char inputChar, char prevChar) {
		boolean isInputKeywordChar = isKeywordChar(inputChar);
		if (offset > 0 && offset - 1 > getStartOffset()) {
			if ((isInputKeywordChar && !isKeywordChar(prevChar))
					|| (!isInputKeywordChar && isKeywordChar(prevChar))) {
				return makeToken(offset - 1, TK_ERROR, false);
			}
		}
		if (offset + 1 < inputChars.length) {
			char nextChar = inputChars[offset + 1];
			if ((isInputKeywordChar && !isKeywordChar(nextChar))
					|| (!isInputKeywordChar && isKeywordChar(nextChar))) {
				return makeToken(offset + 1, TK_ERROR, false);
			}
		}
		return null;
	}
	
	/**
	 * Determines whether the given character could possibly 
	 * be part of a keyword (as opposed to an operator).
	 */
	private boolean isKeywordChar(char c) {
		return Character.isLetterOrDigit(c) || c == '_';
	}

	/**
	 * Creates an artificial token for every water-based recovery
	 * and for comments within layout.
	 */
	public void createLayoutToken(int offset, int lastOffset, LabelInfo label) {
		// Create separate tokens for >1 char layout lexicals (e.g., comments)
		if (offset > lastOffset + 1 && label.isLexLayout()) {
			if (startOffset <= lastOffset)
				makeToken(lastOffset, TK_LAYOUT, false);
			makeToken(offset, TK_LAYOUT, false);
		} else {
			String sort = label.getSort();
			if ("WATERTOKEN".equals(sort) || "WATERTOKENSEPARATOR".equals(sort)) {
				if (getStartOffset() <= lastOffset)
					makeToken(lastOffset, TK_LAYOUT, false);
				makeToken(offset, TK_ERROR, false);
			}
		}
	}

}
