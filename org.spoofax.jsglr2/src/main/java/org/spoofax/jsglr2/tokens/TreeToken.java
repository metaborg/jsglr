package org.spoofax.jsglr2.tokens;

import static org.spoofax.jsglr2.tokens.TreeTokens.EMPTY_RANGE;
import static org.spoofax.jsglr2.tokens.TreeTokens.addPosition;

import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;

import org.spoofax.interpreter.terms.ISimpleTerm;
import org.spoofax.jsglr.client.imploder.IToken;
import org.spoofax.jsglr.client.imploder.ITokens;
import org.spoofax.jsglr2.parser.Position;

public class TreeToken implements IToken, Cloneable {

    private static final long serialVersionUID = -7306530908136122951L;

    private transient TreeTokens tokens;

    private Position positionRange;

    private Kind kind;

    private String errorMessage;

    private ISimpleTerm astNode;

    // TODO IEL
    public TreeTokens.TokenTree tree;

    public TreeToken(TreeTokens tokens, Position positionRange, Kind kind, ISimpleTerm astNode,
        TreeTokens.TokenTree tree) {
        this.tokens = tokens;
        this.positionRange = positionRange;
        this.kind = kind;
        this.astNode = astNode;
        this.tree = tree;
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
        TreeTokens.TokenTree tree = this.tree;
        while(tree.parent != null) {
            TreeTokens.TokenTree parent = tree.parent;
            if(!parent.isAmbiguous) {
                Position siblingsRange = EMPTY_RANGE;
                for(TreeTokens.TokenTree sibling : parent.children) {
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
        if(kind == Kind.TK_RESERVED && tree.parent == tokens.tree)
            return null;
        return getLeftSibling().rightToken;
    }

    @Override public IToken getTokenAfter() {
        if(kind == Kind.TK_EOF && tree.parent == tokens.tree)
            return null;
        return getRightSibling().leftToken;
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

    private TreeTokens.TokenTree getLeftSibling() {
        TreeTokens.TokenTree current = this.tree;
        TreeTokens.TokenTree parent = current.parent;
        while(parent.isAmbiguous || current == parent.nonNullChildren[0]) {
            current = parent;
            parent = current.parent;
        }
        return parent.nonNullChildren[Arrays.asList(parent.nonNullChildren).indexOf(current) - 1];
    }

    private TreeTokens.TokenTree getRightSibling() {
        TreeTokens.TokenTree current = this.tree;
        TreeTokens.TokenTree parent = current.parent;
        while(parent.isAmbiguous || current == parent.nonNullChildren[parent.nonNullChildren.length - 1]) {
            current = parent;
            parent = current.parent;
        }
        return parent.nonNullChildren[Arrays.asList(parent.nonNullChildren).indexOf(current) + 1];
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
            && Objects.equals(astNode, token.astNode);
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
