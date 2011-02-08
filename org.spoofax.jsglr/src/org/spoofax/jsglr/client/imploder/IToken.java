package org.spoofax.jsglr.client.imploder;

import java.io.Serializable;

import org.spoofax.interpreter.terms.ISimpleTerm;

/**
 * @author Lennart Kats <lennart add lclnet.nl>
 */
public interface IToken extends Comparable<IToken>, Serializable {
	/** Unknown token kind. */
	public static final int TK_UNKNOWN = 0;
	
	/** Token kind for a generic identifier. */
	public static final int TK_IDENTIFIER = 1;
	
	/** Token kind for a generic numeric value. */
	public static final int TK_NUMBER = 2;
	
	/** Token kind for a generic string literal. */
	public static final int TK_STRING = 3;
	
	/** Token kind for a generic keyword token. */
	public static final int TK_KEYWORD = 4;
	
	/** Token kind for a generic keyword token. */
	public static final int TK_OPERATOR = 5;
	
	/** Token kind for a meta-variable. */
	public static final int TK_VAR = 6;
	
	/** Token kind for a layout (or comment) token. */
	public static final int TK_LAYOUT = 7;
	
	/** Token kind for an EOF token. */
	public static final int TK_EOF = 8;
	
	/** Token kind for an erroneous non-keyword token. */
	public static final int TK_ERROR = 9;
	
	/** Token kind for an erroneous keyword token. */
	public static final int TK_ERROR_KEYWORD = 10;
	
	/** Token kind for an whitespace near an erroneous token. */
	public static final int TK_ERROR_LAYOUT = 11;
	
	/** Token kind for an erroneous token. */
	public static final int TK_ERROR_EOF_UNEXPECTED = 12;
	
	/** A reserved token kind for internal use only. */
	public static final int TK_RESERVED = 13;
	
	/** A special value indicating no token kind is specified or desired. */
	public static final int TK_NO_TOKEN_KIND = 14;
	
	int getKind();
	
	void setKind(int kind);
	
	int getIndex();

	int getStartOffset();

	int getEndOffset();

	int getLine();
	
	int getEndLine();
	
	int getColumn();

	int getEndColumn();
	
	int getLength();
	
	String getError();
	
	ISimpleTerm getAstNode();
	
	ITokenizer getTokenizer();
	
	IToken clone();
}
