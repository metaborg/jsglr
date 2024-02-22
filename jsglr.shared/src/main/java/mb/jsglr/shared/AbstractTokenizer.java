package mb.jsglr.shared;

import static mb.jsglr.shared.IToken.Kind.TK_EOF;
import static mb.jsglr.shared.IToken.Kind.TK_ERROR;
import static mb.jsglr.shared.IToken.Kind.TK_ERROR_LAYOUT;
import static mb.jsglr.shared.IToken.Kind.TK_LAYOUT;

import mb.jsglr.shared.IToken.Kind;

/**
 * @author Lennart Kats <lennart add lclnet.nl>
 */
public abstract class AbstractTokenizer implements ITokenizer {

	private static final long serialVersionUID = 7697367149430201531L;

    private final TokenKindManager manager =
		new TokenKindManager();

	private boolean isAmbiguous;

	private final String filename;

	private final String input;

	private LineStartOffsetList lineStartOffsets;

	private boolean isSyntaxCorrect = true;

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

	public boolean isSyntaxCorrect() {
		return isSyntaxCorrect;
	}

	public void setSyntaxCorrect(boolean isSyntaxCorrect)  {
		this.isSyntaxCorrect = isSyntaxCorrect;
	}

	private LineStartOffsetList getLineStartOffsets() {
		if (lineStartOffsets == null)
			lineStartOffsets = new LineStartOffsetList(getInput());
		return lineStartOffsets;
	}

	public IToken makeToken(int endOffset, LabelInfo label, boolean allowEmptyToken) {
		return makeToken(endOffset, manager.getTokenKind(label), allowEmptyToken);
	}

	public boolean isAmbiguous() {
		return isAmbiguous;
	}

	public void setAmbiguous(boolean isAmbiguous) {
		this.isAmbiguous = isAmbiguous;
	}

	public int getLineAtOffset(int offset) {
		return getTokenAtOffset(offset).getLine();
	}

	public void markPossibleSyntaxError(LabelInfo label, IToken prevToken, int endOffset, ProductionAttributeReader prodReader) {
		if (label.isRecover() || label.isReject() || label.getDeprecationMessage() != null || label.isCompletion()) {
			if (prodReader.isIgnoredUnspecifiedRecoverySort(label.getSort())) {
				// Special case: don't report here, but further up the tree
				return;
			}

			if (isSyntaxCorrect)
				isSyntaxCorrect = label.getDeprecationMessage() != null;

			// TODO: make TK_ERROR_LAYOUT token from preceding first whitespaces?
			//TODO: refactor
			IToken first, last;

			if (prevToken == currentToken()) {
				first = last = makeToken(endOffset, TK_ERROR, true);
			} else {
				first = prevToken.getTokenAfter();
				if (first != currentToken() && first.getKind() == TK_LAYOUT)
					first = findRightMostLayoutToken(first);
				if (first != currentToken() && first.getKind() == TK_LAYOUT)
					first = first.getTokenAfter();
				last = currentToken();
			}
			if (first.getStartOffset() - 1 == last.getEndOffset()) {
				// bah, we need some characters to mark, to the left then...
				first = last = findLeftMostLayoutToken(first);//mark insertion errors at the end of previous token (before layout)
				if (first.getKind() == TK_LAYOUT)
					first = last = last.getTokenBefore();
			}

			String tokenText = makeTokenText(first, last);

			if (label.isCompletion()) {
				if(last.getKind() == TK_LAYOUT){
					last = findLeftMostLayoutToken(last);
					if (last.getKind() == TK_LAYOUT)
						last = last.getTokenBefore();
				}
				String completionText = makeTokenText(first, last);
				setErrorMessage(first, last, ERROR_INCOMPLETE_PREFIX
						+ ": '" + completionText + "'");
			} else if (label.isReject() || prodReader.isWaterConstructor(label.getConstructor())) {
				setErrorMessage(first, last, ERROR_WATER_PREFIX
						+ ": '" + tokenText + "'");
			} else if (prodReader.isInsertEndConstructor(label.getConstructor())) {
				setErrorMessage(first, last, ERROR_INSERT_END_PREFIX
						/*+ ": '" + tokenText + "'"*/);
			} else if (prodReader.isInsertConstructor(label.getConstructor())
					|| prodReader.isInsertOpenQuoteSort(label.getSort())) {
				// token = findLeftMostLayoutToken(token);
				setErrorMessage(first, last, ERROR_INSERT_PREFIX
						+ ": " + prodReader.getSyntaxErrorExpectedInsertion(label));
			} else if (label.getDeprecationMessage() != null) {
				setErrorMessage(first, last, ERROR_WARNING_PREFIX
						+ ": " + label.getDeprecationMessage());
			} else {
				setErrorMessage(first, last, ERROR_GENERIC_PREFIX
						+ ": '" + tokenText + "'");
			}
		}
	}

	private String makeTokenText(IToken first, IToken last) {
		String tokenText = toString(first, last);
		if (tokenText.length() > 40)
			tokenText = tokenText.substring(0, 40) + "...";
		return tokenText;
	}

	/**
	 * Sets a syntax error for the specified token range.
	 * (Setting any other kind of error would break cacheability.)
	 */
	protected abstract void setErrorMessage(IToken leftToken, IToken rightToken, String message);

	public final int getEndLine() {
		return getTokenCount() == 0 ? 1 : getTokenAt(getTokenCount() - 1).getLine();
	}

	public final int getEndColumn() {
		return getTokenCount() == 0 ? 1 : getTokenAt(getTokenCount() - 1).getColumn();
	}

	public String toString(IToken left, IToken right) {
		int startOffset = left.getStartOffset();
		int endOffset = right.getEndOffset();
		return toString(startOffset, endOffset);
	}

	public String toString(int startOffset, int endOffset) {
		return getInput() == null
			? null
			: getInput().substring(startOffset, endOffset + 1);
	}

	public static boolean isErrorInRange(IToken start, IToken end) {
		ITokenizer tokens = (ITokenizer) start.getTokenizer();
		for (int i = start.getIndex(), max = end.getIndex(); i <= max; i++) {
			Token token = tokens.getTokenAt(i);
			if (token.getError() != null)
				return true;
		}
		return false;
	}

	/**
	 * Searches towards the left of the given token for the
	 * leftmost layout or error token, returning the current token if
	 * no layout token is found.
	 */
	public static IToken findLeftMostLayoutToken(IToken token) {
		if (token == null) return null;
		ITokenizer tokens = (ITokenizer) token.getTokenizer();
		for (int i = token.getIndex() - 1; i >= 0; i--) {
			IToken neighbour = tokens.getTokenAt(i);
			switch (neighbour.getKind()) {
				case TK_LAYOUT: case TK_ERROR: case TK_ERROR_KEYWORD: case TK_ERROR_LAYOUT:
					break;
				default:
					return token;
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
		ITokenizer tokens = (ITokenizer) token.getTokenizer();
		for (int i = token.getIndex() + 1, count = tokens.getTokenCount(); i < count; i++) {
			IToken neighbour = tokens.getTokenAt(i);
			switch (neighbour.getKind()) {
				case TK_LAYOUT: case TK_ERROR: case TK_ERROR_KEYWORD: case TK_ERROR_LAYOUT:
					break;
				default:
					return token;
			}
			token = neighbour;
		}
		return token;
	}

	public static IToken findLeftMostTokenOnSameLine(IToken token) {
		if (token == null) return null;
		int line = token.getLine();
		ITokenizer tokens = (ITokenizer) token.getTokenizer();
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
		ITokenizer tokens = (ITokenizer) token.getTokenizer();
		for (int i = token.getIndex() + 1, count = tokens.getTokenCount(); i < count; i++) {
			IToken neighbour = tokens.getTokenAt(i);
			if (neighbour.getLine() != line || i == count - 1)
				return tokens.getTokenAt(i - 1);
		}
		return token;
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
		boolean onlySeenWhitespace = isSpace(input.charAt(endOffset));

		while (endOffset + 1 < getInput().length()) {
			char next = input.charAt(endOffset+1);

			if (onlySeenWhitespace) {
				onlySeenWhitespace = isSpace(next);
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
			boolean isWhitespace = isSpace(c);

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
	protected final IToken makeAdjunct(int startOffset, int endOffset, Kind tokenKind) {
		LineStartOffsetList lineStarts = getLineStartOffsets();
		int index = lineStarts.getIndex(startOffset);
		int line = lineStarts.getLine(index);
		int column = lineStarts.getColumn(index, startOffset);
		return makeAdjunct(startOffset, endOffset, tokenKind, line, column);
	}

	/**
	 * Creates a helper token that is not really part of the token stream.
	 */
	protected IToken makeAdjunct(int startOffset, int endOffset, Kind tokenKind,
			int line, int column) {
		IToken nearbyToken = getTokenAtOffset(startOffset);
		int fakeIndex = nearbyToken == null ? 0 : nearbyToken.getIndex();
		return new Token(this, filename, fakeIndex, line, column, startOffset, endOffset, tokenKind);
	}

	private static IToken findReportableErrorToken(IToken token) {
	    ITokenizer tokenizer = (ITokenizer) token.getTokenizer();
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
		while (wordStart <= endOffset && isSpace(input.charAt(wordStart)))
			wordStart++;
		if (wordStart < endOffset) // only do this if it doesn't consume the whole token
			makeToken(wordStart - 1, TK_ERROR_LAYOUT, false);

		makeToken(endOffset, TK_ERROR, false);
	}

	private static boolean isSpace(char c) {
		switch (c) {
			case ' ':
				return true;
			case '\n':
				return true;
			case '\t':
				return true;
			case '\f':
				return true;
			case '\r':
				return true;
			default:
				return false;
		}
	}
}
