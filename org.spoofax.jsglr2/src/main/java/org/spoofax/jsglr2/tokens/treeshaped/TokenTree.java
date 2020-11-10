package org.spoofax.jsglr2.tokens.treeshaped;

import static org.spoofax.jsglr2.imploder.StrategoTermTokenizer.configureStatic;
import static org.spoofax.jsglr2.imploder.treefactory.TokenizedTermTreeFactory.configureInjection;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.jsglr.client.imploder.IToken;
import org.spoofax.jsglr2.imploder.TreeImploder;
import org.spoofax.jsglr2.parser.Position;

public class TokenTree {

    final IStrategoTerm tree;
    final TreeToken token; // null for internal nodes
    TokenTree parent;
    final List<TokenTree> children;
    final List<TokenTree> nonNullChildren;
    final Position positionRange;
    /** The number of tokens in this tree */
    final int size;
    final TreeToken leftToken;
    final TreeToken rightToken;
    final Collection<IToken> leftTokens;
    final Collection<IToken> rightTokens;
    final boolean isAmbiguous;

    // The parameter `treeToken` can be null only when the width of the token is 0
    protected TokenTree(TreeImploder.SubTree<IStrategoTerm> tree, TreeToken treeToken) {
        this.tree = tree == null ? null : tree.tree;
        this.leftToken = this.rightToken = this.token = treeToken;
        if(treeToken == null)
            this.leftTokens = this.rightTokens = null;
        else
            this.leftTokens = this.rightTokens = Collections.singleton(treeToken);
        this.children = this.nonNullChildren = Collections.emptyList();
        this.positionRange = treeToken == null ? TreeTokens.EMPTY_RANGE : treeToken.positionRange;
        this.size = treeToken == null ? 0 : 1;
        this.isAmbiguous = false;

        if(treeToken != null)
            treeToken.tree = this;

        configure(tree);
    }

    protected TokenTree(TreeImploder.SubTree<IStrategoTerm> tree, List<TokenTree> children, TreeToken leftToken,
        TreeToken rightToken, Position positionRange) {
        this.tree = tree == null ? null : tree.tree;
        this.token = null;
        this.children = children;
        this.nonNullChildren = children.stream().filter(c -> c.leftToken != null).collect(Collectors.toList());
        this.leftToken = leftToken;
        this.rightToken = rightToken;
        this.positionRange = positionRange;
        this.isAmbiguous = tree != null && tree.isAmbiguous;

        int size = 0;
        for(TokenTree child : children) {
            size += child.size;
        }
        this.size = size;

        if(this.isAmbiguous) {
            this.leftTokens = nonNullChildren.stream().flatMap(c -> c.leftTokens.stream()).collect(Collectors.toList());
            this.rightTokens =
                nonNullChildren.stream().flatMap(c -> c.rightTokens.stream()).collect(Collectors.toList());
        } else {
            Collection<IToken> leftTokens = null;
            Collection<IToken> rightTokens = null;
            for(TokenTree child : children) {
                // The left-most token of this tree is the first non-null leftToken of a subTree
                if(leftTokens == null)
                    leftTokens = child.leftTokens;

                // The right-most token of this tree is the last non-null rightToken of a subTree
                if(child.rightTokens != null)
                    rightTokens = child.rightTokens;
            }
            this.leftTokens = leftTokens;
            this.rightTokens = rightTokens;
        }

        configure(tree);
    }

    private void configure(TreeImploder.SubTree<IStrategoTerm> tree) {
        assert leftToken != null ^ rightToken == null : "Both tokens should be either null, or not null";
        if(tree != null && tree.tree != null) {
            assert leftToken != null && rightToken != null : "All AST nodes should have tokens, even if it's empty";
            if(tree.isInjection) {
                configureInjection(tree.production.lhs(), tree.tree, tree.production.isBracket());
            } else {
                String sort = tree.production == null ? null : tree.production.sort();
                configureStatic(tree.tree, sort, leftToken, rightToken);
            }
        }
    }

}
