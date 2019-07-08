package org.spoofax.jsglr2.incremental.lookaheadstack;

import java.util.Stack;

import org.spoofax.jsglr2.incremental.parseforest.IncrementalCharacterNode;
import org.spoofax.jsglr2.incremental.parseforest.IncrementalParseForest;
import org.spoofax.jsglr2.incremental.parseforest.IncrementalParseNode;

public class EagerLookaheadStack extends AbstractLookaheadStack {
    /**
     * The stack contains all subtrees that are yet to be popped. The top of the stack also contains the subtree that
     * has been returned last time. The stack initially only contains EOF and the root.
     */
    protected final Stack<IncrementalParseForest> stack = new Stack<>();

    /**
     * @param inputString
     *            should be equal to the yield of the root.
     */
    public EagerLookaheadStack(IncrementalParseForest root, String inputString) {
        super(inputString);
        stack.push(IncrementalCharacterNode.EOF_NODE);
        stack.push(root);
    }

    public EagerLookaheadStack(IncrementalParseForest root) {
        this(root, root.getYield());
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
        position += stack.pop().width();
    }

    @Override public IncrementalParseForest get() {
        return stack.empty() ? null : stack.peek();
    }
}
