package org.spoofax.jsglr.client.imploder;

import org.spoofax.interpreter.terms.ISimpleTerm;

/**
 * @author Lennart Kats <lennart add lclnet.nl>
 * @author Karl Trygve Kalleberg <karltk near strategoxt dot org>
 */
public class Token implements IToken {

	private final ITokenizer tokenizer;
	
	// TODO: Optimize - line and column should be determined on demand, not stored everywhere!
	private final int index, startOffset, endOffset, line, column;
	
	private int kind;
	
	private String errorMessage;
	
	private ISimpleTerm astNode;

	public Token(ITokenizer tokenizer, int index, int line, int column, int startOffset, int endOffset, int kind) {
		this.tokenizer = tokenizer;
		this.index = index;
		this.line = line;
		this.column = column;
		this.startOffset = startOffset;
		this.endOffset = endOffset;
		this.kind = kind;
	}
	
	public ITokenizer getTokenizer() {
		return tokenizer;
	}
	
	public int getKind() {
		return kind;
	}
	
	public void setKind(int kind) {
		this.kind = kind;
	}

	public int getIndex() {
		return index;
	}

	public int getStartOffset() {
		return startOffset;
	}

	public int getEndOffset() {
		return endOffset;
	}

	public int getLine() {
		return line;
	}
	
	public int getColumn() {
		return column;
	}
	
	public String getError() {
		if (errorMessage == null) {
			switch (getKind()) {
				case TK_ERROR_EOF_UNEXPECTED: return "Unexpected end of file";
				case TK_ERROR: case TK_ERROR_KEYWORD: return "Syntax error";
				default: return null;
			}
		} else {
			return errorMessage;
		}
	}
	
	public void setError(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	
	public void setAstNode(ISimpleTerm astNode) {
		this.astNode = astNode;
	}
	
	public ISimpleTerm getAstNode() {
		return astNode;
	}
	
	@Override
	public String toString() {
		return tokenizer.toString(this, this);
	}

	public int compareTo(IToken arg0) {
		int otherIndex = arg0.getIndex();
		if (index < otherIndex) {
			return -1;
		} else if (index == otherIndex) {
			return 0;
		} else {
			return 1;
		}
	}
	
	public static int indexOf(IToken token, char c) {
		String stream = token.getTokenizer().getInput();
		for (int i = token.getStartOffset(), last = token.getEndOffset(); i <= last; i++) { 
			if (stream.charAt(i) == c)
				return i;
		}
		return -1;
	}
	
	public static boolean isWhiteSpace(IToken token) {
		String input = token.getTokenizer().getInput();
		for (int i = token.getStartOffset(), last = token.getEndOffset(); i <= last; i++) { 
			if (!Character.isWhitespace(input.charAt(i)))
				return false;
		}
		return true;
	}

	public static String tokenKindToString(int kind) {
		// TODO: proper token kind to string?
		return "tokenKind#" + kind;
	}

}
