package org.spoofax.jsglr.client.imploder;

import static java.lang.Math.min;
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

	private ArrayList<IToken> tokens;
	
	/** Start of the next token. */
	private int startOffset;
	
	/** Line number of the next token. */
	private int line; // TODO: first line zero or one??
	
	public void initialize(char[] inputChars) {
		this.inputChars = inputChars;
		this.tokens = new ArrayList<IToken>(inputChars.length / EXPECTED_TOKENS_DIVIDER);
		startOffset = 0;
		// Ensure there's at least one token
		tokens.add(new Token(this, 0, line, 0, -1, TK_RESERVED));
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
			: tokens.get(tokens.size() - 1);
	}
	
	public int getTokenCount() {
		return tokens.size();
	}
	
	public IToken getTokenAt(int i) {
		return tokens.get(i);
	}

	public IToken makeToken(int endOffset, LabelInfo label) {
		return makeToken(endOffset, label, false);
	}

	public IToken makeToken(int endOffset, LabelInfo label, boolean allowEmptyToken) {
		return makeToken(endOffset, manager.getTokenKind(label), allowEmptyToken);
	}
	
	public IToken makeToken(int endOffset, int kind, boolean allowEmptyToken) {
		if (!allowEmptyToken && startOffset > endOffset) // empty token
			return null;
		
		assert endOffset >= startOffset || (kind == TK_RESERVED && startOffset == 0);
		
		char[] chars = inputChars;
		int offset;
		IToken token = null;
		for (offset = min(startOffset, endOffset); offset < endOffset; offset++) {
			if (chars[offset] == '\n') {
				if (offset - 1 > startOffset)
					token = internalMakeToken(kind, offset - 1);
				internalMakeToken(kind, offset); // newline
			}
		}
		
		if (token == null || offset <= endOffset) {
			return internalMakeToken(kind, offset);
		} else {
			return token;
		}
	}

	private IToken internalMakeToken(int kind, int offset) {
		IToken result = new Token(this, tokens.size() - 1, line, startOffset, offset, kind);
		tokens.add(result);
		startOffset = offset + 1;
		line++;
		return result;
	}

	/**
	 * Creates an artificial token at keyword boundaries
	 * inside skipped regions of code.
	 * Required for keyword highlighting with {@link KeywordRecognizer}.
	 */
	public IToken createSkippedToken(int offset, char inputChar, char prevChar) {
		// FIXME: off-by-one errors?? offset passed is now offset - 1 c.f. old SGLRTokenizer
		boolean isInputKeywordChar = isKeywordChar(inputChar);
		if (offset > 0 && offset > getStartOffset()) {
			if ((isInputKeywordChar && !isKeywordChar(prevChar))
					|| (!isInputKeywordChar && isKeywordChar(prevChar))) {
				return makeToken(offset, TK_ERROR, false);
			}
		}
		if (offset + 2 < inputChars.length) {
			char nextChar = inputChars[offset + 2];
			if ((isInputKeywordChar && !isKeywordChar(nextChar))
					|| (!isInputKeywordChar && isKeywordChar(nextChar))) {
				return makeToken(offset + 2, TK_ERROR, false);
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
	
	public String toString(IToken left, IToken right) {
		int offset = left.getStartOffset();
		return new String(inputChars, offset, right.getEndOffset() - offset + 1);
	}
	
	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		result.append('[');
		for (IToken token : tokens) {
			int offset = token.getStartOffset();
			result.append(inputChars, offset, token.getEndOffset() - offset + 1);
			result.append(',');
		}
		if (tokens.size() > 0)
			result.deleteCharAt(result.length() - 1);
		result.append(']');
		return result.toString();
	}

}
