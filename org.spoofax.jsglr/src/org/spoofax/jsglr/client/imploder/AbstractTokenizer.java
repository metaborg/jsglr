package org.spoofax.jsglr.client.imploder;

import static org.spoofax.jsglr.client.imploder.IToken.TK_EOF;
import static org.spoofax.jsglr.client.imploder.IToken.TK_ERROR;
import static org.spoofax.jsglr.client.imploder.IToken.TK_ERROR_KEYWORD;
import static org.spoofax.jsglr.client.imploder.IToken.TK_ERROR_LAYOUT;
import static org.spoofax.jsglr.client.imploder.IToken.TK_LAYOUT;

/** 
 * @author Lennart Kats <lennart add lclnet.nl>
 */
public abstract class AbstractTokenizer implements ITokenizer {
	
	private final TokenKindManager manager =
		new TokenKindManager();
	
	private boolean isAmbiguous;

	private final String filename;
	
	private final String input;

	private LineStartOffsetList lineStartOffsets;

	public AbstractTokenizer(String input, String filename) {
		this.input = input;
		this.filename = filename;
	}
	
	public String getFilename() {
		return filename;
	}

	public String getInput() {
		return input;
	}

	private LineStartOffsetList getLineStartOffsets() {
		if (lineStartOffsets == null)
			lineStartOffsets = new LineStartOffsetList(getInput());
		return lineStartOffsets;
	}

	public IToken makeToken(int endOffset, LabelInfo label, boolean allowEmptyToken) {
		return makeToken(endOffset, manager.getTokenKind(label), allowEmptyToken);
	}

	public boolean isAmbigous() {
		return isAmbiguous;
	}

	public void setAmbiguous(boolean isAmbiguous) {
		this.isAmbiguous = isAmbiguous;
	}
	
	public int getLineAtOffset(int offset) {
		return getTokenAtOffset(offset).getLine();
	}

	public void markPossibleSyntaxError(LabelInfo label, IToken prevToken, int endOffset, ProductionAttributeReader prodReader) {
		if (label.isRecover() || label.isReject() || label.getDeprecationMessage() != null) {
			if (prodReader.isIgnoredUnspecifiedRecoverySort(label.getSort())) {
				// Special case: don't report here, but further up the tree
				return;
			}
			
			// TODO: make TK_ERROR_LAYOUT token from preceding first whitespaces?
			
			IToken token = currentToken() != prevToken
				? currentToken()
				: makeToken(endOffset, TK_ERROR, false);
			IToken lastToken = currentToken();
			String tokenText = toString(token, lastToken);
			if (tokenText.length() > 40)
				tokenText = tokenText.substring(0, 40) + "...";
			
			if (label.isReject() || prodReader.isWaterConstructor(label.getConstructor())) {
				setErrorMessage(token, lastToken, ERROR_WATER_PREFIX
						+ ": '" + tokenText + "'");
			} else if (prodReader.isInsertEndConstructor(label.getConstructor())) {
				setErrorMessage(token, lastToken, ERROR_INSERT_END_PREFIX
						/*+ ": '" + tokenText + "'"*/);
			} else if (prodReader.isInsertConstructor(label.getConstructor())) {
				token = Tokenizer.findLeftMostLayoutToken(token);
				setErrorMessage(token, lastToken, ERROR_INSERT_PREFIX
						+ ": " + prodReader.getSyntaxErrorExpectedInsertion(label.getRHS()));
			} else if (label.getDeprecationMessage() != null) {
				setErrorMessage(token, lastToken, ERROR_WARNING_PREFIX
						+ ": '" + label.getDeprecationMessage());
			} else {
				setErrorMessage(token, lastToken, ERROR_GENERIC_PREFIX
						+ ": '" + tokenText + "'");
			}
		}
	}

	public final int getEndLine() {
		return getTokenAt(getTokenCount() - 1).getLine();
	}
	
	public String toString(IToken left, IToken right) {
		int startOffset = left.getStartOffset();
		int endOffset = right.getEndOffset();
		return toString(startOffset, endOffset);
	}

	public String toString(int startOffset, int endOffset) {
		return getInput().substring(startOffset, endOffset + 1);
	}
	
	/**
	 * Searches towards the left of the given token for the
	 * leftmost layout or error token, returning the current token if
	 * no layout token is found.
	 */
	public static IToken findLeftMostLayoutToken(IToken token) {
		if (token == null) return null;
		ITokenizer tokens = token.getTokenizer();
	loop:
		for (int i = token.getIndex() - 1; i >= 0; i--) {
			IToken neighbour = tokens.getTokenAt(i);
			switch (neighbour.getKind()) {
				case TK_LAYOUT: case TK_ERROR: case TK_ERROR_KEYWORD: break;
				default: break loop;
			}
			token = neighbour;
		}
		return token;
	}
	
	/**
	 * Searches towards the right of the given token for the
	 * rightmost layout or error token, returning the current token if
	 * no layout token is found.
	 */
	public static IToken findRightMostLayoutToken(IToken token) {
		if (token == null) return null;
		ITokenizer tokens = token.getTokenizer();
	loop:
		for (int i = token.getIndex() + 1, count = tokens.getTokenCount(); i < count; i++) {
			IToken neighbour = tokens.getTokenAt(i);
			switch (neighbour.getKind()) {
				case TK_LAYOUT: case TK_ERROR: case TK_ERROR_KEYWORD: break;
				default: break loop;
			}
			token = neighbour;
		}
		return token;
	}
	
	public static IToken findLeftMostTokenOnSameLine(IToken token) {
		if (token == null) return null;
		int line = token.getLine();
		ITokenizer tokens = token.getTokenizer();
		for (int i = token.getIndex() - 1; i >= 0; i--) {
			IToken neighbour = tokens.getTokenAt(i);
			if (neighbour.getLine() != line || i == 0)
				return tokens.getTokenAt(i + 1);
		}
		return token;
	}
	
	public static IToken findRightMostTokenOnSameLine(IToken token) {
		if (token == null) return null;
		int line = token.getLine();
		ITokenizer tokens = token.getTokenizer();
		for (int i = token.getIndex() + 1, count = tokens.getTokenCount(); i < count; i++) {
			IToken neighbour = tokens.getTokenAt(i);
			if (neighbour.getLine() != line || i == count - 1)
				return tokens.getTokenAt(i - 1);
		}
		return token;
	}
	
	/**
	 * Gets the token with an offset following the given token,
	 * even in an ambiguous token stream.
	 * 
	 * @see #isAmbiguous()
	 */
	public static IToken getTokenAfter(IToken token) {
		if (token == null) return null;
		int nextOffset = token.getEndOffset();
		ITokenizer tokens = token.getTokenizer();
		for (int i = token.getIndex() + 1, max = tokens.getTokenCount(); i < max; i++) {
			IToken result = tokens.getTokenAt(i);
			if (result.getStartOffset() >= nextOffset) return result;
		}
		return null;
	}
	
	/**
	 * Gets the token with an offset preceding the given token,
	 * even in an ambiguous token stream.
	 * 
	 * @see #isAmbiguous()
	 */
	public static IToken getTokenBefore(IToken token) {
		if (token == null) return null;
		int prevOffset = token.getStartOffset();
		ITokenizer tokens = token.getTokenizer();
		for (int i = token.getIndex() - 1; i >= 0; i--) {
			IToken result = tokens.getTokenAt(i);
			if (result.getEndOffset() <= prevOffset) return result;
		}
		return null;
	}
	
	public IToken getErrorTokenOrAdjunct(int offset) {
		if (offset < getStartOffset()) { // before the start of the next to be made token
			return findReportableErrorToken(getTokenAtOffset(offset));
		} else {
			return makeErrorAdjunct(offset);
		}
	}
	
	private IToken makeErrorAdjunct(int offset) {
		if (offset == getInput().length())
		    return makeErrorAdjunctBackwards(offset - 1);
		if (offset > getInput().length())
			return makeErrorAdjunctBackwards(getInput().length() - 1);

		int endOffset = offset;
		String input = getInput();
		boolean onlySeenWhitespace = Character.isWhitespace(input.charAt(endOffset));
		
		while (endOffset + 1 < getInput().length()) {
			char next = input.charAt(endOffset+1);
			
			if (onlySeenWhitespace) {
				onlySeenWhitespace = Character.isWhitespace(next);
				offset++;
			} else if (!Character.isLetterOrDigit(next)) {
				break;
			}
			
			endOffset++;
		}
		
		return makeAdjunct(offset, endOffset, TK_ERROR);
	}
	
	private IToken makeErrorAdjunctBackwards(int offset) {
		int beginOffset = offset;
		boolean onlySeenWhitespace = true;
		
		while (offset >= getInput().length())
			offset--;
		
		String input = getInput();
		while (beginOffset > 0) {
			char c = input.charAt(beginOffset - 1);
			boolean isWhitespace = Character.isWhitespace(c);
			
			if (onlySeenWhitespace) {
				onlySeenWhitespace = isWhitespace;
			} else if (isWhitespace) {
				break;
			}
			
			beginOffset--;
		}
		
		return makeAdjunct(beginOffset, offset, TK_ERROR);
	}

	/**
	 * Creates a helper token that is not really part of the token stream.
	 */
	protected final IToken makeAdjunct(int startOffset, int endOffset, int tokenKind) {
		LineStartOffsetList lineStarts = getLineStartOffsets();
		int index = lineStarts.getIndex(startOffset);
		int line = lineStarts.getLine(index);
		int column = lineStarts.getColumn(index, startOffset);
		return makeAdjunct(startOffset, endOffset, tokenKind, line, column);
	}

	/**
	 * Creates a helper token that is not really part of the token stream.
	 */
	protected IToken makeAdjunct(int startOffset, int endOffset, int tokenKind,
			int line, int column) {
		IToken nearbyToken = getTokenAtOffset(startOffset);
		int fakeIndex = nearbyToken == null ? 0 : nearbyToken.getIndex();
		return new Token(this, fakeIndex, line, column, startOffset, endOffset, tokenKind);
	}

	private static IToken findReportableErrorToken(IToken token) {
		ITokenizer tokenizer = token.getTokenizer();
		// Search right
		for (int i = token.getIndex(), max = tokenizer.getTokenCount(); i < max; i++) {
			token = tokenizer.getTokenAt(i);
			if (token.getKind() == TK_EOF) break;
			if (token.getLength() != 0 && token.getKind() != TK_LAYOUT) return token;
		}
		// Search left
		for (int i = token.getIndex(); i > 0; i--) {
			token = tokenizer.getTokenAt(i);
			if (token.getLength() != 0 && token.getKind() != TK_LAYOUT) return token;
		}
		// Give up
		return token;
	}

	public void tryMakeLayoutToken(int endOffset, int lastOffset, LabelInfo label) {
		// Create separate tokens for >1 char layout lexicals (e.g., comments)
		if (endOffset > lastOffset + 1 && label.isLexLayout()) {
			if (getStartOffset() <= lastOffset)
				makeToken(lastOffset, TK_LAYOUT, false);
			makeToken(endOffset, TK_LAYOUT, false);
		} else {
			String sort = label.getSort();
			if ("WATERTOKEN".equals(sort) || "WATERTOKENSEPARATOR".equals(sort)) {
				makeWaterToken(endOffset, lastOffset);
			}
		}
	}

	private void makeWaterToken(int endOffset, int lastOffset) {
		if (getStartOffset() <= lastOffset)
			makeToken(lastOffset, TK_LAYOUT, false);
		
		// Make an extra token for any whitespace preceding our error
		String input = getInput();
		int wordStart = getStartOffset();
		while (wordStart <= endOffset && Character.isWhitespace(input.charAt(wordStart)))
			wordStart++;
		if (wordStart < endOffset) // only do this if it doesn't consume the whole token
			makeToken(wordStart - 1, TK_ERROR_LAYOUT, false);
		
		makeToken(endOffset, TK_ERROR, false);
	}
}
