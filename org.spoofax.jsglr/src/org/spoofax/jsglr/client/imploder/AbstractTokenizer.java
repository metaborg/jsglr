package org.spoofax.jsglr.client.imploder;

import static org.spoofax.jsglr.client.imploder.IToken.TK_ERROR;
import static org.spoofax.jsglr.client.imploder.IToken.TK_ERROR_KEYWORD;
import static org.spoofax.jsglr.client.imploder.IToken.TK_LAYOUT;

/** 
 * @author Lennart Kats <lennart add lclnet.nl>
 */
public abstract class AbstractTokenizer implements ITokenizer {
	
	private final TokenKindManager manager =
		new TokenKindManager();
	
	private boolean isAmbiguous;

	public IToken makeToken(int endOffset, LabelInfo label, boolean allowEmptyToken) {
		return makeToken(endOffset, manager.getTokenKind(label), allowEmptyToken);
	}

	public boolean isAmbigous() {
		return isAmbiguous;
	}

	public void setAmbiguous(boolean isAmbiguous) {
		this.isAmbiguous = isAmbiguous;
	}

	public void tryMarkSyntaxError(LabelInfo label, IToken firstToken, int endOffset, ProductionAttributeReader prodReader) {
		if (label.isRecover() || label.isReject() || label.getDeprecationMessage() != null) {
			if (firstToken == currentToken())
				firstToken = makeToken(endOffset, TK_ERROR, false);
			IToken lastToken = currentToken();
			String tokenText = toString(firstToken, lastToken);
			if (tokenText.length() > 40)
				tokenText = tokenText.substring(0, 40) + "...";
			
			if (label.isReject() || prodReader.isWaterConstructor(label.getConstructor())) {
				setErrorMessage(firstToken, lastToken, ERROR_WATER_PREFIX
						+ ": '" + tokenText + "'");
			} else if (prodReader.isInsertEndConstructor(label.getConstructor())) {
				setErrorMessage(firstToken, lastToken, ERROR_INSERT_END_PREFIX
						+ ": '" + tokenText + "'");
			} else if (prodReader.isInsertConstructor(label.getConstructor())) {
				firstToken = Tokenizer.findLeftMostLayoutToken(firstToken);
				setErrorMessage(firstToken, lastToken, ERROR_INSERT_PREFIX
						+ ": " + prodReader.getSyntaxErrorExpectedInsertion(label.getRHS()));
			} else if (label.getDeprecationMessage() != null) {
				setErrorMessage(firstToken, lastToken, ERROR_WARNING_PREFIX
						+ ": '" + label.getDeprecationMessage());
			} else if (!prodReader.isIgnoredUnspecifiedRecoverySort(label.getSort())) {
				setErrorMessage(firstToken, lastToken, ERROR_GENERIC_PREFIX
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

	protected String toString(int startOffset, int endOffset) {
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
}
