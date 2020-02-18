package org.spoofax.jsglr2.imploder;

import java.util.ArrayList;
import java.util.Collection;

import org.metaborg.parsetable.productions.IProduction;
import org.metaborg.parsetable.symbols.ISymbol;
import org.spoofax.jsglr.client.imploder.IToken;
import org.spoofax.jsglr2.messages.Message;
import org.spoofax.jsglr2.parser.Position;
import org.spoofax.jsglr2.recovery.RecoveryMessages;
import org.spoofax.jsglr2.tokens.Tokens;

public abstract class TreeTokenizer<Tree> implements ITokenizer<TreeImploder.SubTree<Tree>> {
    class SubTree {
        Tree tree;
        IToken leftToken;
        IToken rightToken;
        Position endPosition;
        public final Collection<Message> messages;

        SubTree(TreeImploder.SubTree<Tree> tree, IToken leftToken, IToken rightToken, Position endPosition,
            Collection<Message> messages) {
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
            this.messages = messages;
        }

    }

    @Override public TokenizeResult tokenize(String input, String fileName,
        TreeImploder.SubTree<Tree> implodeIntermediateResult) {
        Tokens tokens = new Tokens(input, fileName);

        SubTree result = tokenize(tokens, implodeIntermediateResult);

        return new TokenizeResult(tokens, result.messages);
    }

    protected SubTree tokenize(Tokens tokens, TreeImploder.SubTree<Tree> tree) {
        tokens.makeStartToken();
        tokenTreeBinding(tokens.startToken(), tree.tree);

        SubTree res = tokenizeInternal(tokens, tree, new Position(0, 1, 1));

        tokens.makeEndToken(new Position(res.endPosition.offset, res.endPosition.line, res.endPosition.column));
        tokenTreeBinding(tokens.endToken(), res.tree);

        return res;
    }

    private SubTree tokenizeInternal(Tokens tokens, TreeImploder.SubTree<Tree> tree, Position startPosition) {
        if(tree.children.size() == 0) {
            if(tree.width > 0) {
                Position endPosition = startPosition.step(tokens.getInput(), tree.width);
                IToken token = tokens.makeToken(startPosition, endPosition, tree.production);
                tokenTreeBinding(token, tree.tree);

                Collection<Message> messages = recoveryMessages(tree.production, startPosition, endPosition);

                return new SubTree(tree, token, token, endPosition, messages);
            } else {
                Collection<Message> messages =
                    recoveryMessages(tree.production, startPosition, startPosition.step(tokens.getInput(), 1));

                return new SubTree(tree, null, null, startPosition, messages);
            }
        } else {
            IToken leftToken = null;
            IToken rightToken = null;
            Position pivotPosition = startPosition;
            Collection<Message> messages = null;
            for(TreeImploder.SubTree<Tree> child : tree.children) {
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

                // If tree production == null, that means it's an "amb" node; in that case, position is not advanced
                if(tree.production != null)
                    pivotPosition = subTree.endPosition;

                if(subTree.messages != null) {
                    if(messages == null)
                        messages = new ArrayList<>();

                    messages.addAll(subTree.messages);
                }
            }

            messages = recoveryMessages(tree.production, startPosition, pivotPosition, messages);

            return new SubTree(tree, leftToken, rightToken, pivotPosition, messages);
        }
    }

    protected Collection<Message> recoveryMessages(IProduction production, Position startPosition,
        Position endPosition) {
        return recoveryMessages(production, startPosition, endPosition, null);
    }

    protected Collection<Message> recoveryMessages(IProduction production, Position startPosition, Position endPosition,
        Collection<Message> messages) {
        if(production != null && production.isRecovery()) {
            if(messages == null)
                messages = new ArrayList<>();

            messages.add(RecoveryMessages.get(production, startPosition, endPosition));
        }

        return messages;
    }

    protected abstract void configure(Tree term, String sort, IToken leftToken, IToken rightToken);

    protected abstract void configureInjection(ISymbol lhs, Tree term, boolean bracket);

    protected abstract void tokenTreeBinding(IToken token, Tree term);

}
