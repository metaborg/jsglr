package org.spoofax.jsglr2.imploder;

import org.metaborg.parsetable.symbols.ISymbol;
import org.spoofax.jsglr.client.imploder.IToken;
import org.spoofax.jsglr2.JSGLR2Request;
import org.spoofax.jsglr2.parser.Position;
import org.spoofax.jsglr2.tokens.Tokens;

public abstract class TreeTokenizer<Tree> implements ITokenizer<TreeImploder.SubTree<Tree>> {
    class SubTree {
        Tree tree;
        IToken leftToken;
        IToken rightToken;
        Position endPosition;

        SubTree(TreeImploder.SubTree<Tree> tree, IToken leftToken, IToken rightToken, Position endPosition) {
            this.tree = tree.tree;
            this.leftToken = leftToken;
            this.rightToken = rightToken;
            this.endPosition = endPosition;
            if(tree.tree != null && leftToken != null && rightToken != null) {
                if(tree.isInjection) {
                    configureInjection(tree.production.lhs(), tree.tree, tree.production.isBracket());
                } else {
                    String sort = tree.production == null ? null : tree.production.sort();
                    configure(tree.tree, sort, leftToken, rightToken);
                }
            }
        }

    }

    @Override public TokenizeResult tokenize(JSGLR2Request request,
        TreeImploder.SubTree<Tree> implodeIntermediateResult) {
        Tokens tokens = new Tokens(request.input, request.fileName);

        tokenize(tokens, implodeIntermediateResult);

        return new TokenizeResult(tokens);
    }

    protected void tokenize(Tokens tokens, TreeImploder.SubTree<Tree> tree) {
        tokens.makeStartToken();
        tokenTreeBinding(tokens.startToken(), tree.tree);

        SubTree res = tokenizeInternal(tokens, tree, Position.START_POSITION, tokens.startToken());

        tokens.makeEndToken(res.endPosition);
        tokenTreeBinding(tokens.endToken(), res.tree);
    }

    private SubTree tokenizeInternal(Tokens tokens, TreeImploder.SubTree<Tree> tree, Position startPosition,
        IToken parentLeftToken) {
        if(tree.production != null && !tree.production.isContextFree() || tree.isCharacterTerminal) {
            if(tree.width > 0) {
                Position endPosition = startPosition.step(tokens.getInput(), tree.width);
                IToken token = tokens.makeToken(startPosition, endPosition, tree.production);
                tokenTreeBinding(token, tree.tree);

                return new SubTree(tree, token, token, endPosition);
            } else
                return new SubTree(tree, null, null, startPosition);
        } else {
            IToken leftToken = null;
            IToken pivotToken = parentLeftToken;
            Position pivotPosition = startPosition;
            for(TreeImploder.SubTree<Tree> child : tree.children) {
                // In the case when we're dealing with an ambiguous tree node, position is not advanced
                if(tree.isAmbiguous)
                    pivotPosition = startPosition;

                SubTree subTree = tokenizeInternal(tokens, child, pivotPosition, pivotToken);

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
                    pivotToken = subTree.rightToken;

                pivotPosition = subTree.endPosition;
            }

            if(leftToken == null)
                leftToken = parentLeftToken;

            return new SubTree(tree, leftToken, pivotToken, pivotPosition);
        }
    }

    protected abstract void configure(Tree term, String sort, IToken leftToken, IToken rightToken);

    protected abstract void configureInjection(ISymbol lhs, Tree term, boolean bracket);

    protected abstract void tokenTreeBinding(IToken token, Tree term);

}
