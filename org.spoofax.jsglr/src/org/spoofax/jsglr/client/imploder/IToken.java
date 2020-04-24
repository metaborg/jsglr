package org.spoofax.jsglr.client.imploder;

import java.io.Serializable;

import org.spoofax.interpreter.terms.ISimpleTerm;

/**
 * @author Lennart Kats <lennart add lclnet.nl>
 */
public interface IToken extends Comparable<IToken>, Serializable {
    /** Unknown token kind. */
    int TK_UNKNOWN = 0;

    /** Token kind for a generic identifier. */
    int TK_IDENTIFIER = 1;

    /** Token kind for a generic numeric value. */
    int TK_NUMBER = 2;

    /** Token kind for a generic string literal. */
    int TK_STRING = 3;

    /** Token kind for a generic keyword token. */
    int TK_KEYWORD = 4;

    /** Token kind for a generic operator token. */
    int TK_OPERATOR = 5;

    /** Token kind for a meta-variable. */
    int TK_VAR = 6;

    /** Token kind for a layout (or comment) token. */
    int TK_LAYOUT = 7;

    /** Token kind for an EOF token. */
    int TK_EOF = 8;

    /** Token kind for an erroneous non-keyword token. */
    int TK_ERROR = 9;

    /** Token kind for an erroneous keyword token. */
    int TK_ERROR_KEYWORD = 10;

    /** Token kind for an whitespace near an erroneous token. */
    int TK_ERROR_LAYOUT = 11;

    /** Token kind for an erroneous token. */
    int TK_ERROR_EOF_UNEXPECTED = 12;

    /** Token kind for a meta-escape operator. */
    int TK_ESCAPE_OPERATOR = 13;

    /** A reserved token kind for internal use only. */
    int TK_RESERVED = 63;

    /** A special value indicating no token kind is specified or desired. */
    int TK_NO_TOKEN_KIND = 64;

    int getKind();

    void setKind(int kind);

    int getIndex();

    int getStartOffset();

    /**
     * Gets the end offset (inclusive).
     */
    int getEndOffset();

    int getLine();

    int getEndLine();

    int getColumn();

    int getEndColumn();

    int getLength();

    char charAt(int index);

    int codePointAt(int index);

    String getError();

    void setAstNode(ISimpleTerm astNode);

    ISimpleTerm getAstNode();

    String getFilename();

    ITokens getTokenizer();

    IToken clone();
}
