package org.spoofax.jsglr2.incremental.lookaheadstack;

import static org.metaborg.characterclasses.CharacterClassFactory.EOF_INT;

import java.util.Stack;

import org.spoofax.jsglr2.incremental.parseforest.IncrementalCharacterNode;
import org.spoofax.jsglr2.incremental.parseforest.IncrementalParseForest;
import org.spoofax.jsglr2.incremental.parseforest.IncrementalParseNode;

public class EagerLookaheadStack implements ILookaheadStack {
    /**
     * The stack contains all subtrees that are yet to be popped. The top of the stack also contains the subtree that
     * has been returned last time. The stack initially only contains EOF and the root.
     */
    private final Stack<IncrementalParseForest> stack = new Stack<>();
    private final String inputString;
    private final int inputLength;
    private int position = 0;

    /**
     * @param inputString
     *            should be equal to the yield of the root.
     */
    public EagerLookaheadStack(IncrementalParseForest root, String inputString) {
        stack.push(IncrementalCharacterNode.EOF_NODE);
        if(root.width() > 0)
            stack.push(root);

        this.inputString = inputString;
        this.inputLength = inputString.length();
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

    @Override public int actionQueryCharacter() {
        if(position < inputLength)
            return inputString.charAt(position);
        if(position == inputLength)
            return EOF_INT;
        else
            return -1;
    }

    @Override public String actionQueryLookahead(int length) {
        return inputString.substring(position + 1, Math.min(position + 1 + length, inputLength))
            + (position + 1 + length > inputLength ? (char) EOF_INT : "");
    }

    @Override public IncrementalParseForest get() {
        return stack.empty() ? null : stack.peek();
    }
}
