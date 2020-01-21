package org.spoofax.jsglr2.imploder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import javax.annotation.Nullable;

import org.apache.commons.vfs2.FileObject;
import org.spoofax.jsglr.client.imploder.IToken;
import org.spoofax.jsglr2.messages.Message;
import org.spoofax.jsglr2.messages.SourceRegion;
import org.spoofax.jsglr2.parser.Position;
import org.spoofax.jsglr2.tokens.Tokens;

public abstract class TreeTokenizer<Tree> implements ITokenizer<ImplodeResult<TreeImploder.SubTree<Tree>, Tree>> {
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
            if(!tree.isInjection && tree.tree != null && leftToken != null && rightToken != null) {
                String sort = tree.production == null ? null : tree.production.sort();
                configure(tree.tree, sort, leftToken, rightToken);
            }
            this.messages = messages;
        }

    }

    @Override public TokenizeResult tokenize(String input, @Nullable FileObject resource,
        ImplodeResult<TreeImploder.SubTree<Tree>, Tree> implodeResult) {
        Tokens tokens = new Tokens(input, resource != null ? resource.getName().getURI() : "");

        SubTree result = tokenize(resource, tokens, implodeResult.intermediateResult);

        return new TokenizeResult(tokens, result.messages);
    }

    protected SubTree tokenize(FileObject resource, Tokens tokens, TreeImploder.SubTree<Tree> tree) {
        tokens.makeStartToken();
        tokenTreeBinding(tokens.startToken(), tree.tree);

        SubTree res = tokenizeInternal(resource, tokens, tree, new Position(0, 1, 1));

        tokens.makeEndToken(new Position(res.endPosition.offset, res.endPosition.line, res.endPosition.column));
        tokenTreeBinding(tokens.endToken(), res.tree);

        return res;
    }

    private SubTree tokenizeInternal(FileObject resource, Tokens tokens, TreeImploder.SubTree<Tree> tree,
        Position startPosition) {
        if(tree.children.size() == 0) {
            if(tree.width > 0) {
                Position endPosition = startPosition.step(tokens.getInput(), tree.width);
                IToken token = tokens.makeToken(startPosition, endPosition, tree.production);
                tokenTreeBinding(token, tree.tree);
                return new SubTree(tree, token, token, endPosition, Collections.emptyList());
            }
            return new SubTree(tree, null, null, startPosition, Collections.emptyList());
        } else {
            IToken leftToken = null;
            IToken rightToken = null;
            Position pivotPosition = startPosition;
            Collection<Message> messages = null;
            for(TreeImploder.SubTree<Tree> child : tree.children) {
                SubTree subTree = tokenizeInternal(resource, tokens, child, pivotPosition);

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

            if(tree.production != null && tree.production.isRecovery()) {
                if(messages == null)
                    messages = new ArrayList<>();

                messages.add(parseErrorMessage(resource, startPosition, pivotPosition));
            }

            return new SubTree(tree, leftToken, rightToken, pivotPosition, messages);
        }
    }

    protected Message parseErrorMessage(FileObject resource, Position start, Position end) {
        SourceRegion region =
            new SourceRegion(start.offset, start.line, start.column, end.offset, end.line, end.column);

        return Message.error("Invalid syntax", region);
    }

    protected abstract void configure(Tree term, String sort, IToken leftToken, IToken rightToken);

    protected abstract void tokenTreeBinding(IToken token, Tree term);

}
