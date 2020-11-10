package org.spoofax.jsglr2.tokens.treeshaped;

import static org.spoofax.jsglr.client.imploder.IToken.Kind.TK_EOF;
import static org.spoofax.jsglr.client.imploder.IToken.Kind.TK_RESERVED;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Stack;

import org.spoofax.jsglr.client.imploder.IToken;
import org.spoofax.jsglr.client.imploder.ITokens;
import org.spoofax.jsglr2.JSGLR2Request;
import org.spoofax.jsglr2.parser.Position;

public class TreeTokens implements ITokens {

    private static final long serialVersionUID = 2054391299757162697L;

    private final String filename;
    private final String input;

    final TreeToken startToken, endToken;
    TokenTree tree;

    /**
     * <b>Note:</b> this operation is not commutative.
     *
     * @param pos
     *            The position to add to.
     * @param add
     *            The range to add to this position.
     * @return A new position, representing the addition of `add` to `pos`.
     */
    public static Position addPosition(Position pos, Position add) {
        return new Position(pos.offset + add.offset, pos.line + add.line - 1,
            add.line == 1 ? pos.column + add.column : add.column);
    }

    public static final Position EMPTY_RANGE = new Position(0, 1, 0);

    public TreeTokens(JSGLR2Request input) {
        this.input = input.input;
        this.filename = input.fileName;
        this.startToken = new TreeToken(this, EMPTY_RANGE, TK_RESERVED, null);
        this.endToken = new TreeToken(this, EMPTY_RANGE, TK_EOF, null);
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

    @Override public Iterator<IToken> iterator() {
        return new TokenIterator(true, false, false);
    }

    @Override public Iterable<IToken> allTokens() {
        return () -> new TokenIterator(true, true, true);
    }

    class TokenIterator implements Iterator<IToken> {
        private final Stack<TokenTree> stack = new Stack<>();
        private final boolean includeAmbiguous;
        private final boolean includeEmpty;

        TokenIterator(boolean includeStartEnd, boolean includeAmbiguous, boolean includeEmpty) {
            if(includeStartEnd)
                stack.push(tree);
            else
                stack.push(tree.children.get(1));
            this.includeAmbiguous = includeAmbiguous;
            this.includeEmpty = includeEmpty;
        }

        @Override public boolean hasNext() {
            while(!stack.isEmpty()) {
                boolean updated = false;
                while(!stack.isEmpty() && !stack.peek().children.isEmpty()) {
                    TokenTree pop = stack.pop();
                    if(pop.isAmbiguous && !includeAmbiguous)
                        stack.push(pop.children.get(0));
                    else
                        for(int i = pop.children.size() - 1; i >= 0; i--) {
                            stack.push(pop.children.get(i));
                        }
                    updated = true;
                }
                while(!stack.isEmpty() && stack.peek().children.isEmpty() && shouldSkipToken(stack.peek().token)) {
                    stack.pop();
                    updated = true;
                }
                if(!updated)
                    return true;
            }
            return false;
        }

        private boolean shouldSkipToken(TreeToken token) {
            return token == null || !includeEmpty && token.getKind() != TK_EOF && token.getKind() != TK_RESERVED
                && token.positionRange.offset == 0;
        }

        @Override public IToken next() {
            if(!hasNext())
                throw new NoSuchElementException();
            return stack.pop().token;
        }
    }

}
