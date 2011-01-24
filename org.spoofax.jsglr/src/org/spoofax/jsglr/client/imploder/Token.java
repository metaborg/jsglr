package org.spoofax.jsglr.client.imploder;

import java.util.HashMap;
import java.util.Map;

import org.spoofax.interpreter.terms.ISimpleTerm;

/**
 * @author Lennart Kats <lennart add lclnet.nl>
 * @author Karl Trygve Kalleberg <karltk near strategoxt dot org>
 */
public class Token implements IToken {
	
	private static Map<String, Integer> asyncAllTokenKinds;

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
	
	public int getEndLine() {
		return line; // our tokens span only one line
	}
	
	public int getColumn() {
		return column;
	}
	
	public int getEndColumn() {
		return column + getEndOffset() - getStartOffset();
	}
	
	public int getLength() {
		return getEndOffset() - getStartOffset() + 1;
	}
	
	public String getError() {
		if (errorMessage == null) {
			switch (getKind()) {
				case TK_ERROR_EOF_UNEXPECTED: return ITokenizer.ERROR_UNEXPECTED_EOF;
				case TK_ERROR: case TK_ERROR_KEYWORD: return ITokenizer.ERROR_GENERIC_PREFIX;
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
		int otherOffset = arg0.getStartOffset();
		if (endOffset < otherOffset) {
			return -1;
		} else if (startOffset > otherOffset) {
			return 1;
		} else {
			return 0;
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
		return "tokenKind#" + kind;
	}

	public static int valueOf(String tokenKind) {
		Integer result = getTokenKindMap().get(tokenKind);
		return result == null ? TK_NO_TOKEN_KIND : result;
	}
	
	private static Map<String, Integer> getTokenKindMap() {
		synchronized (Token.class) {
			if (asyncAllTokenKinds != null)
				return asyncAllTokenKinds;
			asyncAllTokenKinds = new HashMap<String, Integer>();
			asyncAllTokenKinds.put("TK_UNKNOWN", TK_UNKNOWN);
			asyncAllTokenKinds.put("TK_IDENTIFIER", TK_IDENTIFIER);
			asyncAllTokenKinds.put("TK_NUMBER", TK_NUMBER);
			asyncAllTokenKinds.put("TK_STRING", TK_STRING);
			asyncAllTokenKinds.put("TK_KEYWORD", TK_KEYWORD);
			asyncAllTokenKinds.put("TK_OPERATOR", TK_OPERATOR);
			asyncAllTokenKinds.put("TK_VAR", TK_VAR);
			asyncAllTokenKinds.put("TK_LAYOUT", TK_LAYOUT);
			asyncAllTokenKinds.put("TK_EOF", TK_EOF);
			asyncAllTokenKinds.put("TK_ERROR", TK_ERROR);
			asyncAllTokenKinds.put("TK_ERROR_KEYWORD", TK_ERROR_KEYWORD);
			asyncAllTokenKinds.put("TK_ERROR_EOF_UNEXPECTED", TK_ERROR_EOF_UNEXPECTED);
			asyncAllTokenKinds.put("TK_ERROR_LAYOUT", TK_ERROR_LAYOUT);
			asyncAllTokenKinds.put("TK_RESERVED", TK_RESERVED);
			asyncAllTokenKinds.put("TK_NO_TOKEN_KIND", TK_NO_TOKEN_KIND);
			return asyncAllTokenKinds;
		}
	}

}
