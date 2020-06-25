package org.spoofax.jsglr2.tokens.treeshaped;

import static org.spoofax.jsglr2.tokens.treeshaped.TreeTokens.EMPTY_RANGE;
import static org.spoofax.jsglr2.tokens.treeshaped.TreeTokens.addPosition;

import java.util.Collection;
import java.util.Objects;

import org.spoofax.interpreter.terms.ISimpleTerm;
import org.spoofax.jsglr.client.imploder.IToken;
import org.spoofax.jsglr.client.imploder.ITokens;
import org.spoofax.jsglr2.parser.Position;

public class TreeToken implements IToken, Cloneable {

    private static final long serialVersionUID = -7306530908136122951L;

    private final transient TreeTokens tokens;

    final Position positionRange;

    private final Kind kind;

    private ISimpleTerm astNode;

    /**
     * A reference to a leaf of the token tree. Should be set right after attaching this token to this TokenTree. This
     * currently happens in the constructor of TokenTree.
     */
    public TokenTree tree;

    public TreeToken(TreeTokens tokens, Position positionRange, Kind kind, ISimpleTerm astNode) {
        this.tokens = tokens;
        this.positionRange = positionRange;
        this.kind = kind;
        this.astNode = astNode;
    }

    @Override public ITokens getTokenizer() {
        return tokens;
    }

    @Override public Kind getKind() {
        return kind;
    }

    @Override public int getIndex() {
        throw new UnsupportedOperationException();
    }

    private Position getStartPosition() {
        Position positionRange = EMPTY_RANGE;
        TokenTree tree = this.tree;
        while(tree.parent != null) {
            TokenTree parent = tree.parent;
            if(!parent.isAmbiguous) {
                Position siblingsRange = EMPTY_RANGE;
                for(TokenTree sibling : parent.children) {
                    if(sibling == tree) {
                        positionRange = TreeTokens.addPosition(siblingsRange, positionRange);
                        break;
                    }
                    siblingsRange = TreeTokens.addPosition(siblingsRange, sibling.positionRange);
                }
            }
            tree = parent;
        }
        return addPosition(Position.START_POSITION, positionRange);
    }

    private Position getEndPosition() {
        return TreeTokens.addPosition(getStartPosition(), positionRange);
    }

    @Override public int getStartOffset() {
        return getStartPosition().offset;
    }

    @Override public int getEndOffset() {
        return getEndPosition().offset - 1;
    }

    @Override public int getLine() {
        return getStartPosition().line;
    }

    @Override public int getEndLine() {
        return getEndPosition().line;
    }

    @Override public int getColumn() {
        return getStartPosition().column;
    }

    @Override public int getEndColumn() {
        return getEndPosition().column - 1;
    }

    @Override public int getLength() {
        return positionRange.offset;
    }

    @Override public String getFilename() {
        return tokens.getFilename();
    }

    @Override public void setAstNode(ISimpleTerm astNode) {
        this.astNode = astNode;
    }

    @Override public ISimpleTerm getAstNode() {
        return astNode;
    }

    @Override public IToken getTokenBefore() {
        TreeToken previousToken = this;
        while(true) {
            if(kind == Kind.TK_RESERVED && tree.parent == tokens.tree)
                return null;
            previousToken = previousToken.getLeftSibling().rightToken;
            if(previousToken.positionRange.offset > 0 || previousToken.getKind() == Kind.TK_RESERVED)
                return previousToken;
        }
    }

    @Override public IToken getTokenAfter() {
        TreeToken nextToken = this;
        while(true) {
            if(kind == Kind.TK_EOF && tree.parent == tokens.tree)
                return null;
            nextToken = nextToken.getRightSibling().leftToken;
            if(nextToken.positionRange.offset > 0 || nextToken.getKind() == Kind.TK_EOF)
                return nextToken;
        }
    }

    // These two methods might come in handy in the future, but currently have no use yet.
    // The implementation of this method for JSGLR1 Tokens will be much more complicated than this,
    // since that would involve iterating over the token stream to find all tokens at the same offset.
    // If you want to remove these methods, you can also remove the `leftTokens` and `rightTokens` fields on TokenTree.
    public Collection<IToken> getTokensBefore() {
        if(kind == Kind.TK_RESERVED && tree.parent == tokens.tree)
            return null;
        return getLeftSibling().rightTokens;
    }

    public Collection<IToken> getTokensAfter() {
        if(kind == Kind.TK_EOF && tree.parent == tokens.tree)
            return null;
        return getRightSibling().leftTokens;
    }

    private TokenTree getLeftSibling() {
        TokenTree current = this.tree;
        TokenTree parent = current.parent;
        while(parent.isAmbiguous || current == parent.nonNullChildren.get(0)) {
            current = parent;
            parent = current.parent;
        }
        return parent.nonNullChildren.get(parent.nonNullChildren.indexOf(current) - 1);
    }

    private TokenTree getRightSibling() {
        TokenTree current = this.tree;
        TokenTree parent = current.parent;
        while(parent.isAmbiguous || current == parent.nonNullChildren.get(parent.nonNullChildren.size() - 1)) {
            current = parent;
            parent = current.parent;
        }
        return parent.nonNullChildren.get(parent.nonNullChildren.indexOf(current) + 1);
    }

    @Override public String toString() {
        return tokens.toString(this, this);
    }

    @Override public int hashCode() {
        return Objects.hash(positionRange, kind, astNode);
    }

    @Override public boolean equals(Object o) {
        if(this == o)
            return true;
        if(o == null || getClass() != o.getClass())
            return false;
        TreeToken token = (TreeToken) o;
        return kind == token.kind && Objects.equals(positionRange, token.positionRange)
        // Using reference equality for the astNode, else tokens at different positions are considered equal
            && astNode == token.astNode;
    }

    @Override public int compareTo(IToken other) {
        return getStartOffset() - other.getStartOffset();
    }

    @Override public TreeToken clone() {
        try {
            return (TreeToken) super.clone();
        } catch(CloneNotSupportedException e) {
            // Must be supported for IToken
            throw new RuntimeException(e);
        }
    }

}
