package mb.jsglr.shared;

import java.io.Serializable;

import org.spoofax.interpreter.terms.ISimpleTerm;

/**
 * @author Lennart Kats <lennart add lclnet.nl>
 */
public interface IToken extends Comparable<IToken>, Serializable {
    enum Kind {
        /** Unknown token kind. */
        TK_UNKNOWN,
        /** Token kind for a generic identifier. */
        TK_IDENTIFIER,
        /** Token kind for a generic numeric value. */
        TK_NUMBER,
        /** Token kind for a generic string literal. */
        TK_STRING,
        /** Token kind for a generic keyword token. */
        TK_KEYWORD,
        /** Token kind for a generic operator token. */
        TK_OPERATOR,
        /** Token kind for a meta-variable. */
        TK_VAR,
        /** Token kind for a layout (or comment) token. */
        TK_LAYOUT,
        /** Token kind for an EOF token. */
        TK_EOF,
        /** Token kind for an erroneous non-keyword token. */
        TK_ERROR,
        /** Token kind for an erroneous keyword token. */
        TK_ERROR_KEYWORD,
        /** Token kind for an whitespace near an erroneous token. */
        TK_ERROR_LAYOUT,
        /** Token kind for an erroneous token. */
        TK_ERROR_EOF_UNEXPECTED,
        /** Token kind for a meta-escape operator. */
        TK_ESCAPE_OPERATOR,
        /** A reserved token kind for internal use only. */
        TK_RESERVED,
        /** A special value indicating no token kind is specified or desired. */
        TK_NO_TOKEN_KIND,
    }

    Kind getKind();

    /**
     * Only Tokens returned by JSGLR1 are guaranteed to have an index, and this method may only be used by JSGLR1 legacy
     * code. <br>
     * Tokens returned by incremental JSGLR2 do not have an index.
     */
    @SuppressWarnings("DeprecatedIsStillUsed") @Deprecated int getIndex();

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

    void setAstNode(ISimpleTerm astNode);

    ISimpleTerm getAstNode();

    String getFilename();

    ITokens getTokenizer();

    /**
     * @return A token immediately preceding the current token, or null if the current token is the start token. <br>
     *         If the preceding AST node is ambiguous, any of the possible tokens will be returned. <br>
     *         This method will skip empty tokens, but calling this method on an empty token will still work. <br>
     *         The start token does <i>not</i> count as an empty token.
     */
    IToken getTokenBefore();

    /**
     * @return A token immediately following the current token, or null if the current token is the end token. <br>
     *         If the following AST node is ambiguous, any of the possible tokens will be returned. <br>
     *         This method will skip empty tokens, but calling this method on an empty token will still work. <br>
     *         The EOF token does <i>not</i> count as an empty token.
     */
    IToken getTokenAfter();

    IToken clone();
}
