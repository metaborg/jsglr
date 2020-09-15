package org.spoofax.jsglr2.tokens.treeshaped;

import static org.spoofax.jsglr.client.imploder.IToken.Kind.TK_NO_TOKEN_KIND;
import static org.spoofax.jsglr2.tokens.treeshaped.TreeTokens.EMPTY_RANGE;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.jsglr.client.imploder.IToken;
import org.spoofax.jsglr2.imploder.ITokenizer;
import org.spoofax.jsglr2.imploder.TreeImploder;
import org.spoofax.jsglr2.parser.Position;

public abstract class AbstractTreeShapedTokenizer<TokensResult extends TreeTokens>
    implements ITokenizer<TreeImploder.SubTree<IStrategoTerm>, TokensResult> {

    private static Position positionRange(Position beginPosition, Position endPosition) {
        return new Position(endPosition.offset - beginPosition.offset, endPosition.line - beginPosition.line + 1,
            beginPosition.line == endPosition.line ? endPosition.column - beginPosition.column : endPosition.column);
    }

    protected final void finalize(TreeImploder.SubTree<IStrategoTerm> tree, TreeTokens tokens, TokenTree tokenTree) {
        TokenTree res = new TokenTree(null,
            Arrays.asList(new TokenTree(null, tokens.startToken), tokenTree, new TokenTree(null, tokens.endToken)),
            tokens.startToken, tokens.endToken, tokenTree.positionRange);
        for(TokenTree child : res.children) {
            child.parent = res;
        }
        tokens.startToken.setAstNode(tree.tree);
        tokens.endToken.setAstNode(tree.tree);

        tokens.startToken.tree = res.children.get(0);
        tokens.endToken.tree = res.children.get(2);
        tokens.tree = res;
    }

    public TokenTree tokenizeInternal(TreeTokens tokens, TreeImploder.SubTree<IStrategoTerm> tree,
        Position pivotPosition) {
        if(tree.production != null && !tree.production.isContextFree() || tree.isCharacterTerminal) {
            if(tree.width > 0) {
                Position endPosition = pivotPosition.step(tokens.getInput(), tree.width);
                Position positionRange = positionRange(pivotPosition, endPosition);
                return new TokenTree(tree,
                    new TreeToken(tokens, positionRange, IToken.getTokenKind(tree.production), tree.tree));
            } else
                return new TokenTree(tree,
                    tree.tree == null ? null : new TreeToken(tokens, EMPTY_RANGE, TK_NO_TOKEN_KIND, tree.tree));
        } else {
            List<TokenTree> children = new ArrayList<>(tree.children.size());
            TreeToken leftToken = null;
            TreeToken rightToken = null;
            List<TreeImploder.SubTree<IStrategoTerm>> subTrees = tree.children;
            for(TreeImploder.SubTree<IStrategoTerm> imploderSubTree : subTrees) {
                TokenTree subTree = tokenizeInternal(tokens, imploderSubTree, pivotPosition);
                children.add(subTree);

                // If tree ast == null, that means it's layout or literal lexical;
                // that means it needs to be bound to the current tree
                if(subTree.tree == null) {
                    if(subTree.token != null)
                        subTree.token.setAstNode(tree.tree);
                }

                // The left-most token of this tree is the first non-null leftToken of a subTree
                if(leftToken == null)
                    leftToken = subTree.leftToken;

                // The right-most token of this tree is the last non-null rightToken of a subTree
                if(subTree.rightToken != null)
                    rightToken = subTree.rightToken;

                // In the case when we're dealing with an ambiguous tree node, position is not advanced
                if(!tree.isAmbiguous) {
                    pivotPosition = TreeTokens.addPosition(pivotPosition, subTree.positionRange);
                }
            }

            // If there is no token, this means that this AST has no characters in the input.
            // In this case, create an empty token to associate with this AST node.
            if(leftToken == null) {
                assert rightToken == null;
                return new TokenTree(tree, new TreeToken(tokens, EMPTY_RANGE, TK_NO_TOKEN_KIND, tree.tree));
            }

            Position positionRange = tree.isAmbiguous ? children.get(0).positionRange
                : children.stream().map(child -> child.positionRange).reduce(EMPTY_RANGE, TreeTokens::addPosition);
            TokenTree res = new TokenTree(tree, children, leftToken, rightToken, positionRange);
            for(TokenTree child : children) {
                child.parent = res;
            }
            return res;
        }
    }

}
