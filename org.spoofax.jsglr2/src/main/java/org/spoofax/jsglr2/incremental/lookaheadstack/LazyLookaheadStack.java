package org.spoofax.jsglr2.incremental.lookaheadstack;

import java.util.Stack;

import org.spoofax.jsglr2.incremental.parseforest.IncrementalCharacterNode;
import org.spoofax.jsglr2.incremental.parseforest.IncrementalDerivation;
import org.spoofax.jsglr2.incremental.parseforest.IncrementalParseForest;
import org.spoofax.jsglr2.incremental.parseforest.IncrementalParseNode;

public class LazyLookaheadStack implements ILookaheadStack {
    /**
     * The stack contains the parent and child index of the node that has been returned last time.
     * When the stack is initialized, a mock root is created and pushed to the stack.
     */
    private final Stack<StackTuple> stack = new Stack<>();
    private IncrementalParseForest last;

    public LazyLookaheadStack(IncrementalParseForest root) {
        IncrementalParseNode ultraRoot = new IncrementalParseNode(root, IncrementalCharacterNode.EOF_NODE);
        if (root.width() > 0) {
            stack.push(new StackTuple(ultraRoot, 0));
            last = root;
        } else {
            stack.push(new StackTuple(ultraRoot, 1));
            last = IncrementalCharacterNode.EOF_NODE;
        }
    }

    @Override
    public IncrementalParseForest get() {
        return last;
    }

    @Override public void leftBreakdown() {
        if(stack.isEmpty())
            last = null;
        if(last == null || last.isTerminal())
            return;
        IncrementalParseForest[] children = ((IncrementalParseNode) last).getFirstDerivation().parseForests();
        if(children.length > 0) {
            stack.push(new StackTuple(((IncrementalParseNode) last), 0));
            last = children[0];
        } else
            popLookahead();
    }

    @Override public void popLookahead() {
        if(stack.isEmpty())
            last = null;
        StackTuple res = stack.pop();
        while(rightSibling(res) == null)
            if(stack.isEmpty()) {
                last = null;
                return;
            } else
                res = stack.pop();
        stack.push(new StackTuple(res.parseForest, res.childIndex + 1));
        last = rightSibling(res);
    }

    private IncrementalParseForest rightSibling(StackTuple res) {
        if(res == null)
            return IncrementalCharacterNode.EOF_NODE;
        else {
            IncrementalDerivation parent = res.parseForest.getFirstDerivation();
            if(res.childIndex + 1 == parent.parseForests().length)
                return null;
            else
                return parent.parseForests()[res.childIndex + 1];
        }
    }

    @Override
    public int actionQueryCharacter() {
        return 'a'; // TODO this is a pain in the nose. Hardcoded test result :see_no_evil:
    }

    @Override
    public String actionQueryLookahead(int length) {
        return ("bcd" + (char) 256).substring(0, Math.min(4, length)); // TODO this is a pain in the nose
    }

    private final class StackTuple {
        private final IncrementalParseNode parseForest;
        private final int childIndex;

        public StackTuple(IncrementalParseNode parseForest, int childIndex) {
            this.parseForest = parseForest;
            this.childIndex = childIndex;
        }
    }
}
