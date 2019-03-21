package org.spoofax.jsglr2.incremental.lookaheadstack;

import java.util.Stack;

import org.spoofax.jsglr2.incremental.parseforest.IncrementalCharacterNode;
import org.spoofax.jsglr2.incremental.parseforest.IncrementalParseForest;
import org.spoofax.jsglr2.incremental.parseforest.IncrementalParseNode;

public class EagerLookaheadStack implements ILookaheadStack {
    /**
     * The stack contains all subtrees that are yet to be popped. The top of the stack also contains the subtree that
     * has been returned last time. The stack initially only contains EOF and the root (
     */
    private final Stack<IncrementalParseForest> stack = new Stack<>();

    public EagerLookaheadStack(IncrementalParseForest root) {
        stack.push(IncrementalCharacterNode.EOF_NODE);
        if (root.width() > 0)
            stack.push(root);
    }

    @Override public void leftBreakdown() {
        if(stack.isEmpty())
            return;
        IncrementalParseForest current = stack.peek();
        if(current.isTerminal()) {
            return;
        }
        stack.pop(); // always pop last lookahead, whether it has children or not
        IncrementalParseForest[] children = ((IncrementalParseNode) current).getFirstDerivation().parseForests();
        // Push all children to stack in reverse order
        for(int i = children.length - 1; i >= 0; i--) {
            stack.push(children[i]);
        }
    }

    @Override public void popLookahead() {
        stack.pop();
    }

    @Override
    public int actionQueryCharacter() {
        for (int i = stack.size() - 1; i >= 0; i--) {
            if (stack.get(i).width() <= 0)
                continue;
            return stack.get(i).getYield(1).charAt(0);
        }
        return -1; // Should only happen when stack is empty, as EOF is always on the stack before that
    }

    @Override public String actionQueryLookahead(int length) {
        int width = length + 1;
        StringBuilder sb = new StringBuilder(width);
        int stackIndex = stack.size() - 1;
        while(width > 0 && stackIndex >= 0) {
            IncrementalParseForest parseForest = stack.get(stackIndex);
            sb.append(parseForest.width() <= width ? parseForest.getYield() : parseForest.getYield(width));
            width -= parseForest.width();
            stackIndex--;
        }
        return sb.substring(1);
    }

    @Override public IncrementalParseForest get() {
        return stack.empty() ? null : stack.peek();
    }
}
