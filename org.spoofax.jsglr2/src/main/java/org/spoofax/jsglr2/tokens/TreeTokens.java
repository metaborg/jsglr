package org.spoofax.jsglr2.tokens;

import static org.spoofax.jsglr.client.imploder.IToken.Kind.*;
import static org.spoofax.jsglr2.imploder.StrategoTermTokenizer.configureStatic;
import static org.spoofax.jsglr2.imploder.treefactory.TokenizedTermTreeFactory.configureInjection;

import java.util.*;
import java.util.stream.Collectors;

import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.jsglr.client.imploder.IToken;
import org.spoofax.jsglr.client.imploder.ITokens;
import org.spoofax.jsglr2.JSGLR2Request;
import org.spoofax.jsglr2.imploder.ITokenizer;
import org.spoofax.jsglr2.imploder.TokenizeResult;
import org.spoofax.jsglr2.imploder.TreeImploder;
import org.spoofax.jsglr2.parser.Position;

public class TreeTokens implements ITokens {

    private static final long serialVersionUID = 2054391299757162697L;

    private final String filename;
    private final String input;

    private final TreeToken startToken, endToken;
    TokenTree tree;

    public static class TokenTree {
        private static final TokenTree[] EMPTY_CHILDREN = new TokenTree[0];

        final IStrategoTerm tree;
        final TreeToken token; // null for internal nodes
        TokenTree parent;
        final TokenTree[] children;
        final TokenTree[] nonNullChildren;
        final Position positionRange;
        final int size;
        final TreeToken leftToken;
        final TreeToken rightToken;
        final Collection<IToken> leftTokens;
        final Collection<IToken> rightTokens;
        final boolean isAmbiguous;
        IStrategoTerm enclosingAst;

        protected TokenTree(TreeImploder.SubTree<IStrategoTerm> tree, TreeToken token, Position positionRange) {
            this.tree = tree == null ? null : tree.tree;
            this.leftToken = this.rightToken = this.token = token;
            if(token == null)
                this.leftTokens = this.rightTokens = null;
            else
                this.leftTokens = this.rightTokens = Collections.singleton(token);
            this.children = this.nonNullChildren = EMPTY_CHILDREN;
            this.positionRange = positionRange;
            this.size = token == null ? 0 : 1;
            this.isAmbiguous = false;

            configure(tree);
        }

        protected TokenTree(TreeImploder.SubTree<IStrategoTerm> tree, TokenTree[] children, TreeToken leftToken,
            TreeToken rightToken, Position positionRange) {
            this.tree = tree == null ? null : tree.tree;
            this.token = null;
            this.children = children;
            this.nonNullChildren = Arrays.stream(children).filter(c -> c.leftToken != null).toArray(TokenTree[]::new);
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
                this.leftTokens =
                    Arrays.stream(nonNullChildren).flatMap(c -> c.leftTokens.stream()).collect(Collectors.toList());
                this.rightTokens =
                    Arrays.stream(nonNullChildren).flatMap(c -> c.rightTokens.stream()).collect(Collectors.toList());
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

        protected void setEnclosingAst(IStrategoTerm tree) {
            this.enclosingAst = tree;
        }
    }

    /** <b>Note:</b> this operation is not associative. */
    public static Position addPosition(Position pos, Position add) {
        return new Position(pos.offset + add.offset, pos.line + add.line - 1,
            add.line == 1 ? pos.column + add.column : add.column);
    }

    private static Position positionRange(Position beginPosition, Position endPosition) {
        return new Position(endPosition.offset - beginPosition.offset, endPosition.line - beginPosition.line + 1,
            beginPosition.line == endPosition.line ? endPosition.column - beginPosition.column : endPosition.column);
    }

    public static final Position EMPTY_RANGE = new Position(0, 1, 0);

    public TreeTokens(JSGLR2Request input) {
        this.input = input.input;
        this.filename = input.fileName;
        this.startToken = new TreeToken(this, EMPTY_RANGE, TK_RESERVED, null, null);
        this.endToken = new TreeToken(this, EMPTY_RANGE, TK_EOF, null, null);
    }

    public static class Tokenizer extends AbstractTokenizer<TreeTokens> {
        @Override public TokenizeResult<TreeTokens> tokenize(JSGLR2Request input,
            TreeImploder.SubTree<IStrategoTerm> tree, TreeTokens previousResult) {

            TreeTokens tokens = new TreeTokens(input);

            TokenTree tokenTree = tokenizeInternal(tokens, tree, Position.START_POSITION);
            finalize(tree, tokens, tokenTree);

            return new TokenizeResult<>(tokens);
        }
    }

    public static abstract class AbstractTokenizer<TokensResult extends TreeTokens>
        implements ITokenizer<TreeImploder.SubTree<IStrategoTerm>, TokensResult> {

        protected final void finalize(TreeImploder.SubTree<IStrategoTerm> tree, TreeTokens tokens,
            TokenTree tokenTree) {
            TokenTree res = new TokenTree(null,
                new TokenTree[] { new TokenTree(null, tokens.startToken, EMPTY_RANGE), tokenTree,
                    new TokenTree(null, tokens.endToken, EMPTY_RANGE) },
                tokens.startToken, tokens.endToken, tokenTree.positionRange);
            for(TokenTree child : res.children) {
                child.parent = res;
            }
            tokens.startToken.setAstNode(tree.tree);
            tokens.endToken.setAstNode(tree.tree);

            tokens.startToken.tree = res.children[0];
            tokens.endToken.tree = res.children[2];
            tokens.tree = res;
        }

        public TreeTokens.TokenTree tokenizeInternal(TreeTokens tokens, TreeImploder.SubTree<IStrategoTerm> tree,
            Position pivotPosition) {
            if(tree.production != null && !tree.production.isContextFree() || tree.isCharacterTerminal) {
                if(tree.width > 0) {
                    Position endPosition = pivotPosition.step(tokens.getInput(), tree.width);
                    Position positionRange = positionRange(pivotPosition, endPosition);
                    return singleTokenTree(tokens, tree, positionRange, IToken.getTokenKind(tree.production),
                        tree.tree);
                } else
                    return new TokenTree(tree, null, EMPTY_RANGE);
            } else {
                TreeTokens.TokenTree[] children = new TreeTokens.TokenTree[tree.children.size()];
                TreeToken leftToken = null;
                TreeToken rightToken = null;
                List<TreeImploder.SubTree<IStrategoTerm>> subTrees = tree.children;
                for(int i = 0; i < subTrees.size(); i++) {
                    TreeTokens.TokenTree subTree = tokenizeInternal(tokens, subTrees.get(i), pivotPosition);
                    children[i] = subTree;

                    // If tree ast == null, that means it's layout or literal lexical;
                    // that means it needs to be bound to the current tree
                    if(subTree.tree == null) {
                        subTree.setEnclosingAst(tree.tree);
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
                        pivotPosition = addPosition(pivotPosition, subTree.positionRange);
                    }
                }

                // If there is no token, this means that this AST has no characters in the input.
                // In this case, create an empty token to associate with this AST node.
                if(leftToken == null) {
                    assert rightToken == null;
                    return singleTokenTree(tokens, tree, EMPTY_RANGE, TK_NO_TOKEN_KIND, tree.tree);
                }

                Position positionRange = tree.isAmbiguous ? children[0].positionRange : Arrays.stream(children)
                    .map(child -> child.positionRange).reduce(EMPTY_RANGE, TreeTokens::addPosition);
                TokenTree res = new TokenTree(tree, children, leftToken, rightToken, positionRange);
                for(TokenTree child : children) {
                    child.parent = res;
                }
                return res;
            }
        }
    }

    private static TokenTree singleTokenTree(TreeTokens tokens, TreeImploder.SubTree<IStrategoTerm> tree,
        Position positionRange, IToken.Kind kind, IStrategoTerm astTree) {
        TreeToken token = new TreeToken(tokens, positionRange, kind, astTree, null);
        TokenTree tokenTree = new TokenTree(tree, token, positionRange);
        token.tree = tokenTree;
        return tokenTree;
    }

    class TokenIterator implements Iterator<IToken> {
        private final Stack<TokenTree> stack = new Stack<>();
        private final boolean includeAmbiguous;

        TokenIterator(boolean includeStartEnd, boolean includeAmbiguous) {
            if(includeStartEnd)
                stack.push(new TokenTree(null, endToken, EMPTY_RANGE));
            stack.push(tree.children[1]);
            if(includeStartEnd)
                stack.push(new TokenTree(null, startToken, EMPTY_RANGE));
            this.includeAmbiguous = includeAmbiguous;
        }

        @Override public boolean hasNext() {
            while(!stack.isEmpty()) {
                boolean updated = false;
                while(!stack.isEmpty() && stack.peek().children.length > 0) {
                    TokenTree pop = stack.pop();
                    if(pop.isAmbiguous && !includeAmbiguous)
                        stack.push(pop.children[0]);
                    else
                        for(int i = pop.children.length - 1; i >= 0; i--) {
                            stack.push(pop.children[i]);
                        }
                    updated = true;
                }
                while(!stack.isEmpty() && stack.peek().children.length == 0 && stack.peek().token == null) {
                    stack.pop();
                    updated = true;
                }
                if(!updated)
                    return true;
            }
            return false;
        }

        @Override public IToken next() {
            if(!hasNext())
                throw new NoSuchElementException();
            return stack.pop().token;
        }
    }

    @Override public Iterator<IToken> iterator() {
        return new TokenIterator(true, false);
    }

    @Override public Iterable<IToken> allTokens() {
        return () -> new TokenIterator(true, true);
    }

    @Override public String getInput() {
        return input;
    }

    @Override public int getTokenCount() {
        return tree.size;
    }

    @Override public IToken getTokenAtOffset(int offset) {
        int currentOffset = 0;
        TokenTree currentTree = this.tree;
        while(true) {
            if(currentTree.token != null)
                return currentTree.token;
            for(TokenTree child : currentTree.children) {
                if(child.leftToken == null)
                    continue;
                int width = child.positionRange.offset;
                if(currentOffset <= offset && offset < currentOffset + (width == 0 ? 1 : width)) {
                    currentTree = child;
                    break;
                }
                currentOffset = currentOffset + width;
            }
        }
    }

    @Override public String getFilename() {
        return filename;
    }

    @Override public String toString(IToken left, IToken right) {
        int startOffset = left.getStartOffset();
        int endOffset = right.getEndOffset();

        if(startOffset >= 0 && endOffset >= 0)
            return toString(startOffset, endOffset + 1);
        else
            return "";
    }

    @Override public String toString(int startOffset, int endOffset) {
        return input.substring(startOffset, endOffset);
    }

    @Override public String toString() {
        return "TreeTokens{filename='" + filename + "', input='" + input + "'}";
    }

}
