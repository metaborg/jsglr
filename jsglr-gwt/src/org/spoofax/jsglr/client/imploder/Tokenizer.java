package org.spoofax.jsglr.client.imploder;

import static java.lang.Math.min;
import static org.spoofax.jsglr.client.imploder.IToken.TK_ERROR;
import static org.spoofax.jsglr.client.imploder.IToken.TK_LAYOUT;
import static org.spoofax.jsglr.client.imploder.IToken.TK_RESERVED;

import java.util.ArrayList;
import java.util.Iterator;

import org.spoofax.jsglr.client.KeywordRecognizer;

/**
 * @author Lennart Kats <lennart add lclnet.nl>
 * @author Karl Trygve Kalleberg <karltk near strategoxt dot org>
 */
public class Tokenizer implements ITokenizer {
	
	private static final int EXPECTED_TOKENS_DIVIDER = 5;
	
	private final KeywordRecognizer keywords;
	
	private final TokenKindManager manager =
		new TokenKindManager();
	
	private String filename;
	
	private String input;

	private ArrayList<IToken> tokens;
	
	/** Start of the next token. */
	private int startOffset;
	
	/** Line number of the next token. */
	private int line;

	private int offsetAtLineStart;
	
	public Tokenizer(KeywordRecognizer keywords) {
		this.keywords = keywords;
	}
	
	public void initialize(String filename, String input) {
		this.filename = filename;
		this.input = input;
		this.tokens = new ArrayList<IToken>(input.length() / EXPECTED_TOKENS_DIVIDER);
		startOffset = 0;
		line = 0;
		offsetAtLineStart = 0;
		// Ensure there's at least one token
		tokens.add(new Token(this, 0, line, 0, 0, -1, TK_RESERVED));
	}
	
	public final String getInput() {
		return input;
	}
	
	public String getFilename() {
		return filename;
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

	public IToken makeToken(int endOffset, LabelInfo label, boolean allowEmptyToken) {
		return makeToken(endOffset, manager.getTokenKind(label), allowEmptyToken);
	}
	
	public IToken makeToken(int endOffset, int kind, boolean allowEmptyToken) {
		if (!allowEmptyToken && startOffset > endOffset) // empty token
			return null;
		
		assert endOffset >= startOffset || (kind == TK_RESERVED && startOffset == 0);
		
		int offset;
		IToken token = null;
		for (offset = min(startOffset, endOffset); offset < endOffset; offset++) {
			if (input.charAt(offset) == '\n') {
				if (offset - 1 > startOffset)
					token = internalMakeToken(kind, offset - 1);
				internalMakeToken(TK_LAYOUT, offset); // newline
				line++;
				offsetAtLineStart = startOffset;
			}
		}
		
		if (token == null || offset <= endOffset) {
			token = internalMakeToken(kind, offset);
			if (input.charAt(offset) == '\n') {
				line++;
				offsetAtLineStart = startOffset;
			}
			return token;
		} else {
			return token;
		}
	}

	private IToken internalMakeToken(int kind, int endOffset) {
		IToken result = new Token(this, tokens.size() - 1, line, startOffset - offsetAtLineStart, startOffset, endOffset, kind);
		tokens.add(result);
		startOffset = endOffset + 1;
		return result;
	}

	public void makeErrorToken(int offset) {
		char inputChar = input.charAt(offset);
		
		boolean isInputKeywordChar = KeywordRecognizer.isPotentialKeywordChar(inputChar);
		if (isAtPotentialKeywordEnd(offset, isInputKeywordChar)) {
			if (keywords.isKeyword(toString(startOffset, offset - 1))) {
				makeToken(offset - 1, TK_ERROR_KEYWORD, false);
			} else {
				makeToken(offset - 1, TK_ERROR, false);
			}
		}
		if (isAtPotentialKeywordStart(offset, isInputKeywordChar)) {
			if (keywords.isKeyword(toString(startOffset, offset))) {
				makeToken(offset, TK_ERROR_KEYWORD, false);
			} else {
				makeToken(offset, TK_ERROR, false);
			}
		}
	}

	private boolean isAtPotentialKeywordEnd(int offset, boolean isInputKeywordChar) {
		if (offset >= 1 && offset > startOffset) {
			char prevChar = input.charAt(offset - 1);
			return 	(isInputKeywordChar && !isKeywordChar(prevChar))
					|| (!isInputKeywordChar && isKeywordChar(prevChar));
		}
		return false;
	}

	private boolean isAtPotentialKeywordStart(int offset, boolean isInputKeywordChar) {
		if (offset + 1 < input.length()) {
			char nextChar = input.charAt(offset + 1);
			if ((isInputKeywordChar && !isKeywordChar(nextChar))
					|| (!isInputKeywordChar && isKeywordChar(nextChar))) {
				return true;
			}
		}
		return false;
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
	public void makeLayoutToken(int offset, int lastOffset, LabelInfo label) {
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
		int startOffset = left.getStartOffset();
		int endOffset = right.getEndOffset();
		return toString(startOffset, endOffset);
	}

	private String toString(int startOffset, int endOffset) {
		return new StringBuilder(endOffset - startOffset + 1).append(input, startOffset, endOffset + 1).toString();
	}
	
	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		result.append('[');
		for (IToken token : tokens) {
			int offset = token.getStartOffset();
			result.append(input, offset, token.getEndOffset() + 1);
			result.append(',');
		}
		if (tokens.size() > 0)
			result.deleteCharAt(result.length() - 1);
		result.append(']');
		return result.toString();
	}

	protected KeywordRecognizer getKeywordRecognizer() {
		return keywords;
	}

	public Iterator<IToken> iterator() {
		return tokens.iterator();
	}

}
