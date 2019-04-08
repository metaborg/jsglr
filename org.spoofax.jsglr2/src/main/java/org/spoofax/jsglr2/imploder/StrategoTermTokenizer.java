package org.spoofax.jsglr2.imploder;

import static org.spoofax.jsglr.client.imploder.ImploderAttachment.putImploderAttachment;

import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.jsglr.client.imploder.IToken;
import org.spoofax.jsglr2.tokens.Tokens;

public class StrategoTermTokenizer {
    static class SubTree {
        IStrategoTerm tree;
        IToken leftToken;
        IToken rightToken;

        SubTree(StrategoTermImploder.SubTree tree, Tokens tokens) {
            this.tree = tree.tree;
            if(!tree.startPosition.equals(tree.endPosition)) {
                IToken token = tokens.makeToken(tree.startPosition, tree.endPosition, tree.production);
                this.leftToken = token;
                this.rightToken = token;
                if(this.tree != null) {
                    tokenTreeBinding(token, tree.tree);
                    putImploderAttachment(this.tree, false, tree.production.sort(), leftToken, rightToken, false, false,
                        false, false);
                }
            }
        }

        SubTree(StrategoTermImploder.SubTree tree, IToken leftToken, IToken rightToken) {
            this.tree = tree.tree;
            this.leftToken = leftToken;
            this.rightToken = rightToken;
        }

    }

    public IStrategoTerm tokenize(Tokens tokens, StrategoTermImploder.SubTree tree) {
        tokens.makeStartToken();
        tokenTreeBinding(tokens.startToken(), tree.tree);

        IStrategoTerm res = tokenizeInternal(tokens, tree).tree;

        tokens.makeEndToken(tree.endPosition);
        tokenTreeBinding(tokens.endToken(), tree.tree);

        return res;
    }

    private SubTree tokenizeInternal(Tokens tokens, StrategoTermImploder.SubTree tree) {
        if(tree.children.size() == 0) {
            return new SubTree(tree, tokens);
        } else {
            IToken leftToken = null;
            IToken rightToken = null;
            for(StrategoTermImploder.SubTree child : tree.children) {
                SubTree subTree = tokenizeInternal(tokens, child);
                // Collect tokens that are not bound to a tree such that they can later be bound to the resulting
                // parent tree
                if(child.tree == null) {
                    if(subTree.leftToken != null)
                        tokenTreeBinding(subTree.leftToken, tree.tree);

                    if(subTree.rightToken != null)
                        tokenTreeBinding(subTree.rightToken, tree.tree);
                } else {
                    // The left-most token of this tree is the first non-null leftToken of a subTree
                    if(leftToken == null)
                        leftToken = subTree.leftToken;

                    // The right-most token of this tree is the last non-null rightToken of a subTree
                    if(subTree.rightToken != null)
                        rightToken = subTree.rightToken;
                }
            }
            return new SubTree(tree, leftToken, rightToken);
        }
    }

    protected static void tokenTreeBinding(IToken token, IStrategoTerm term) {
        token.setAstNode(term);
    }

}
