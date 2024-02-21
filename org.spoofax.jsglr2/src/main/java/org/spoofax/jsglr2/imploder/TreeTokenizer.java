package org.spoofax.jsglr2.imploder;

import org.metaborg.parsetable.symbols.ISymbol;
import org.spoofax.jsglr2.JSGLR2Request;
import org.spoofax.jsglr2.parser.Position;
import org.spoofax.jsglr2.tokens.Tokens;

import jsglr.shared.IToken;
import jsglr.shared.ITokens;

public abstract class TreeTokenizer<Tree> implements ITokenizer<TreeImploder.SubTree<Tree>, ITokens> {
    class SubTree {
        public final Tree tree;
        public final IToken leftToken;
        public final IToken rightToken;
        public final Position endPosition;

        SubTree(TreeImploder.SubTree<Tree> tree, IToken leftToken, IToken rightToken, Position endPosition) {
            this.tree = tree.tree;
            this.leftToken = leftToken;
            this.rightToken = rightToken;
            this.endPosition = endPosition;
            assert leftToken != null ^ rightToken == null : "Both tokens should be either null, or not null";
            if(tree.tree != null) {
                assert leftToken != null && rightToken != null : "All AST nodes should have tokens, even if it's empty";
                if(tree.isInjection) {
                    configureInjection(tree.production.lhs(), tree.tree, tree.production.isBracket());
                } else {
                    String sort = tree.production == null ? null : tree.production.sort();
                    configure(tree.tree, sort, leftToken, rightToken);
                }
            }
        }
    }

    @Override public TokenizeResult<ITokens> tokenize(JSGLR2Request request,
        TreeImploder.SubTree<Tree> implodeIntermediateResult, ITokens previousResult) {
        Tokens tokens = new Tokens(request.input, request.fileName);

        tokenize(tokens, implodeIntermediateResult);

        return new TokenizeResult<>(tokens);
    }

    protected void tokenize(Tokens tokens, TreeImploder.SubTree<Tree> tree) {
        tokens.makeStartToken();
        tokenTreeBinding(tokens.startToken(), tree.tree);

        SubTree res = tokenizeInternal(tokens, tree, Position.START_POSITION);

        tokens.makeEndToken(res.endPosition);
        tokenTreeBinding(tokens.endToken(), res.tree);
    }

    private SubTree tokenizeInternal(Tokens tokens, TreeImploder.SubTree<Tree> tree, Position startPosition) {
        if(tree.production != null && !tree.production.isContextFree() || tree.isCharacterTerminal) {
            if(tree.width > 0 || tree.tree != null) {
                Position endPosition = startPosition.step(tokens.getInput(), tree.width);
                IToken token = tokens.makeToken(startPosition, endPosition, tree.production);
                tokenTreeBinding(token, tree.tree);

                return new SubTree(tree, token, token, endPosition);
            } else
                return new SubTree(tree, null, null, startPosition);
        } else {
            IToken leftToken = null;
            IToken rightToken = null;
            Position pivotPosition = startPosition;
            for(TreeImploder.SubTree<Tree> child : tree.children) {
                // In the case when we're dealing with an ambiguous tree node, position is not advanced
                if(tree.isAmbiguous)
                    pivotPosition = startPosition;

                SubTree subTree = tokenizeInternal(tokens, child, pivotPosition);

                // If child tree had tokens that were not yet bound, bind them
                if(subTree.tree == null) {
                    if(subTree.leftToken != null)
                        tokenTreeBinding(subTree.leftToken, tree.tree);

                    if(subTree.rightToken != null)
                        tokenTreeBinding(subTree.rightToken, tree.tree);
                }

                // The left-most token of this tree is the first non-null leftToken of a subTree
                if(leftToken == null)
                    leftToken = subTree.leftToken;

                // The right-most token of this tree is the last non-null rightToken of a subTree
                if(subTree.rightToken != null)
                    rightToken = subTree.rightToken;

                pivotPosition = subTree.endPosition;
            }

            // If there is no token, this means that this AST has no characters in the input.
            // In this case, create an empty token to associate with this AST node.
            if(leftToken == null) {
                assert rightToken == null;
                leftToken = rightToken = tokens.makeToken(startPosition, pivotPosition, tree.production);
                tokenTreeBinding(leftToken, tree.tree);
            }

            return new SubTree(tree, leftToken, rightToken, pivotPosition);
        }
    }

    protected abstract void configure(Tree term, String sort, IToken leftToken, IToken rightToken);

    protected abstract void configureInjection(ISymbol lhs, Tree term, boolean bracket);

    protected abstract void tokenTreeBinding(IToken token, Tree term);

}
