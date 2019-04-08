package org.spoofax.jsglr2.imploder;

import static org.spoofax.jsglr.client.imploder.ImploderAttachment.putImploderAttachment;

import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.jsglr.client.imploder.IToken;
import org.spoofax.jsglr2.parser.Position;
import org.spoofax.jsglr2.tokens.Tokens;

public class StrategoTermTokenizer {
    static class SubTree {
        IStrategoTerm tree;
        IToken leftToken;
        IToken rightToken;
        Position endPosition;

        SubTree(StrategoTermImploder.SubTree tree, IToken leftToken, IToken rightToken, Position endPosition) {
            this.tree = tree.tree;
            this.leftToken = leftToken;
            this.rightToken = rightToken;
            this.endPosition = endPosition;
            if(this.tree != null && leftToken != null && rightToken != null) {
                String sort = tree.production == null ? null : tree.production.sort();
                putImploderAttachment(this.tree, false, sort, leftToken, rightToken, false, false,
                    false, false);
            }
        }

    }

    public IStrategoTerm tokenize(Tokens tokens, StrategoTermImploder.SubTree tree) {
        tokens.makeStartToken();
        tokenTreeBinding(tokens.startToken(), tree.tree);

        SubTree res = tokenizeInternal(tokens, tree, new Position(0, 1, 1));

        tokens.makeEndToken(new Position(res.endPosition.offset, res.endPosition.line, res.endPosition.column));
        tokenTreeBinding(tokens.endToken(), res.tree);

        return res.tree;
    }

    private SubTree tokenizeInternal(Tokens tokens, StrategoTermImploder.SubTree tree, Position startPosition) {
        if(tree.children.size() == 0) {
            if(tree.width > 0) {
                Position endPosition = startPosition.step(tokens.getInput(), tree.width);
                IToken token = tokens.makeToken(startPosition, endPosition, tree.production);
                tokenTreeBinding(token, tree.tree);
                return new SubTree(tree, token, token, endPosition);
            }
            return new SubTree(tree, null, null, startPosition);
        } else {
            IToken leftToken = null;
            IToken rightToken = null;
            Position pivotPosition = startPosition;
            for(StrategoTermImploder.SubTree child : tree.children) {
                SubTree subTree = tokenizeInternal(tokens, child, pivotPosition);

                // If child tree had tokens that were not yet bound, bind them
                if(child.tree == null) {
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

                // If tree production == null, that means it's an "amb" node; in that case, position is not advanced
                if(tree.production != null)
                    pivotPosition = subTree.endPosition;
            }
            return new SubTree(tree, leftToken, rightToken, pivotPosition);
        }
    }

    protected static void tokenTreeBinding(IToken token, IStrategoTerm term) {
        token.setAstNode(term);
    }

}
