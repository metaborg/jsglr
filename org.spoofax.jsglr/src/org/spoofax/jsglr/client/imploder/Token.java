package org.spoofax.jsglr.client.imploder;

import java.util.List;

import org.spoofax.interpreter.terms.ISimpleTerm;

/**
 * @author Lennart Kats <lennart add lclnet.nl>
 * @author Karl Trygve Kalleberg <karltk near strategoxt dot org>
 */
public class Token implements IToken, Cloneable {

    private static final long serialVersionUID = -6972938219235720902L;

    private transient ITokens tokens;

    private final String filename;

    private final int line;

    private final int column;

    private int startOffset;

    private int endOffset;

    private int index;

    private Kind kind;

    private String errorMessage;

    private ISimpleTerm astNode;

    public Token(ITokens tokens, String filename, int index, int line, int column, int startOffset, int endOffset,
        Kind kind) {
        this.tokens = tokens;
        this.filename = filename;
        this.index = index;
        this.line = line;
        this.column = column;
        this.startOffset = startOffset;
        this.endOffset = endOffset;
        this.kind = kind;
    }

    public ITokens getTokenizer() {
        return tokens;
    }

    protected void setTokenizer(ITokens tokenizer) {
        this.tokens = tokenizer;
    }

    public Kind getKind() {
        return kind;
    }

    @SuppressWarnings("DeprecatedIsStillUsed") @Deprecated public int getIndex() {
        return index;
    }

    protected void setIndex(int index) {
        this.index = index;
    }

    public final int getStartOffset() {
        return startOffset;
    }

    public void setStartOffset(int startOffset) {
        this.startOffset = startOffset;
    }

    public final int getEndOffset() {
        return endOffset;
    }

    public void setEndOffset(int endOffset) {
        this.endOffset = endOffset;
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
        return column - 1
            + (tokens != null && tokens.getInput() != null && getStartOffset() >= 0
                && getEndOffset() < tokens.getInput().length() && getStartOffset() <= getEndOffset()
                    ? tokens.getInput().codePointCount(getStartOffset(), getEndOffset() + 1) : getLength());
    }

    public int getLength() {
        return getEndOffset() - getStartOffset() + 1;
    }

    @Override public String getFilename() {
        return filename;
    }

    /**
     * Gets the error message associated with this token, if any.
     *
     * Note that this message is independent from the token kind, which may also indicate an error.
     */
    public String getError() {
        return errorMessage;
    }

    /**
     * Sets a syntax error for this token. (Setting any other kind of error would break cacheability.)
     */
    public void setError(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public void setAstNode(ISimpleTerm astNode) {
        this.astNode = astNode;
    }

    public ISimpleTerm getAstNode() {
        if(astNode == null) {
            ITokens tokens = getTokenizer();

            // This is a hack. For jsglr1 the AST binding might not be done yet.
            // For jsglr2 it is always done during imploding.
            if(tokens instanceof AbstractTokenizer)
                ((AbstractTokenizer) getTokenizer()).initAstNodeBinding();
        }
        return astNode;
    }

    @Override public IToken getTokenBefore() {
        int prevOffset = this.getStartOffset();
        List<IToken> tokens = (List<IToken>) this.getTokenizer().allTokens();
        for(int i = this.getIndex() - 1; i >= 0; i--) {
            IToken result = tokens.get(i);
            if(result.getEndOffset() < prevOffset)
                return result;
        }
        return null;
    }

    @Override public IToken getTokenAfter() {
        int nextOffset = this.getEndOffset();
        List<IToken> tokens = (List<IToken>) this.getTokenizer().allTokens();
        for(int i = this.getIndex() + 1, max = tokens.size(); i < max; i++) {
            IToken result = tokens.get(i);
            if(result.getStartOffset() > nextOffset)
                return result;
        }
        return null;
    }

    @Override public String toString() {
        return tokens.toString(this, this);
    }

    @Override public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + column;
        result = prime * result + endOffset;
        result = prime * result + index;
        result = prime * result + kind.ordinal();
        result = prime * result + line;
        result = prime * result + startOffset;
        return result;
    }

    @Override public boolean equals(Object obj) {
        if(this == obj)
            return true;
        if(obj == null)
            return false;
        if(getClass() != obj.getClass())
            return false;
        Token other = (Token) obj;
        if(column != other.column)
            return false;
        if(endOffset != other.endOffset)
            return false;
        if(index != other.index)
            return false;
        if(kind != other.kind)
            return false;
        if(line != other.line)
            return false;
        if(startOffset != other.startOffset)
            return false;
        return true;
    }

    public int compareTo(IToken other) {
        if(endOffset <= other.getEndOffset()) {
            return -1;
        } else if(startOffset > other.getStartOffset()) {
            return 1;
        } else {
            return 0;
        }
    }

    public static int indexOf(IToken token, int c) {
        String stream = token.getTokenizer().getInput();
        for(int i = token.getStartOffset(), last = token.getEndOffset(); i <= last; i++) {
            if(stream.codePointAt(i) == c)
                return i;
        }
        return -1;
    }



    public static boolean isWhiteSpace(IToken token) {
        String input = token.getTokenizer().getInput();
        for(int i = token.getStartOffset(), last = token.getEndOffset(); i <= last; i++) {
            switch(input.charAt(i)) {
                case ' ':
                case '\n':
                case '\t':
                case '\f':
                case '\r':
                    continue;
                default:
                    return false;
            }
        }
        return true;
    }

    @Override public Token clone() {
        try {
            return (Token) super.clone();
        } catch(CloneNotSupportedException e) {
            // Must be supported for IToken
            throw new RuntimeException(e);
        }
    }

}
