package org.spoofax.jsglr2.inputstack.incremental;

import static org.spoofax.jsglr2.incremental.parseforest.IncrementalCharacterNode.EOF_NODE;
import static org.spoofax.jsglr2.inputstack.incremental.AbstractPreprocessingIncrementalInputStack.preProcessParseForest;

import java.util.Stack;

import org.spoofax.jsglr2.incremental.IIncrementalParseState;
import org.spoofax.jsglr2.incremental.diff.IStringDiff;
import org.spoofax.jsglr2.incremental.diff.ProcessUpdates;
import org.spoofax.jsglr2.incremental.parseforest.*;
import org.spoofax.jsglr2.parser.AbstractParseState;
import org.spoofax.jsglr2.parser.observing.IParserObserver.BreakdownReason;
import org.spoofax.jsglr2.stack.IStackNode;

public class LazyPreprocessingIncrementalInputStack extends AbstractInputStack implements IIncrementalInputStack {
    /**
     * The stack contains the parent and child index of the node that has been returned last time. When the stack is
     * initialized, a mock root is created and pushed to the stack.
     */
    private final Stack<StackTuple> stack = new Stack<>();
    private IncrementalParseForest last;

    /**
     * @param inputString
     *            should be equal to the yield of the root.
     */
    public LazyPreprocessingIncrementalInputStack(IncrementalParseForest root, String inputString) {
        super(inputString);
        IncrementalParseNode ultraRoot = new IncrementalParseNode(root, IncrementalCharacterNode.EOF_NODE);
        stack.push(new StackTuple(ultraRoot, 0));

        this.last = root;
    }

    public static <StackNode extends IStackNode, ParseState extends AbstractParseState<IIncrementalInputStack, StackNode> & IIncrementalParseState>
        IncrementalInputStackFactory<IIncrementalInputStack>
        factory(IStringDiff diff, ProcessUpdates<StackNode, ParseState> processUpdates) {
        return (inputString, previousInput, previousResult, observing) -> new LazyPreprocessingIncrementalInputStack(
            preProcessParseForest(processUpdates, diff, inputString, previousInput, previousResult), inputString);
    }

    @Override public LazyPreprocessingIncrementalInputStack clone() {
        LazyPreprocessingIncrementalInputStack clone =
            new LazyPreprocessingIncrementalInputStack(EOF_NODE, inputString);
        clone.stack.clear();
        for(StackTuple stackTuple : stack) {
            clone.stack.push(stackTuple);
        }
        clone.last = last;
        clone.currentOffset = currentOffset;
        return clone;
    }

    @Override public IncrementalParseForest getNode() {
        return last;
    }

    @Override public void breakDown(BreakdownReason breakdownReason) {
        if(stack.isEmpty())
            last = null;
        if(last == null || last.isTerminal())
            return;

        if(last instanceof IncrementalSkippedNode) {
            // Replace skipped node by one that has all skipped characters explicitly instantiated
            last = new IncrementalParseNode(inputString.substring(currentOffset, currentOffset + last.width())
                .codePoints().mapToObj(IncrementalCharacterNode::new).toArray(IncrementalParseForest[]::new));
        }

        IncrementalParseForest[] children = ((IncrementalParseNode) last).getFirstDerivation().parseForests();
        if(children.length > 0) {
            stack.push(new StackTuple(((IncrementalParseNode) last), 0));
            last = children[0];
        } else
            next();
    }

    @Override public void next() {
        currentOffset += last.width();
        if(stack.isEmpty()) {
            last = null;
            return;
        }
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

    /** See comment at {@link AbstractPreprocessingIncrementalInputStack#lookaheadIsUnchanged()} */
    @Override public boolean lookaheadIsUnchanged() {
        return false;
        // for(int i = stack.size() - 1; i >= 0; i--) {
        // IncrementalParseForest node = rightSibling(stack.get(i));
        // if(node != null) {
        // if(node.isTerminal()) return true;
        // return ((IncrementalParseNode) node).production() != null;
        // }
        // }
        // return true; // EOF is always unchanged
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

    private static final class StackTuple {
        private final IncrementalParseNode parseForest;
        private final int childIndex;

        StackTuple(IncrementalParseNode parseForest, int childIndex) {
            this.parseForest = parseForest;
            this.childIndex = childIndex;
        }
    }
}
