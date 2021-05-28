package org.spoofax.jsglr2.inputstack.incremental;

import org.spoofax.jsglr2.incremental.IIncrementalParseState;
import org.spoofax.jsglr2.incremental.diff.IStringDiff;
import org.spoofax.jsglr2.incremental.diff.ProcessUpdates;
import org.spoofax.jsglr2.incremental.parseforest.IncrementalParseForest;
import org.spoofax.jsglr2.incremental.parseforest.IncrementalParseNode;
import org.spoofax.jsglr2.parser.AbstractParseState;
import org.spoofax.jsglr2.stack.IStackNode;

public class LinkedPreprocessingIncrementalInputStack extends AbstractPreprocessingIncrementalInputStack {

    LinkedPreprocessingIncrementalInputStack(LinkedPreprocessingIncrementalInputStack original) {
        super(original);
    }

    /**
     * @param inputString
     *            should be equal to the yield of the root.
     */
    public LinkedPreprocessingIncrementalInputStack(IncrementalParseForest root, String inputString) {
        super(root, inputString);
    }

    public static <StackNode extends IStackNode, ParseState extends AbstractParseState<IIncrementalInputStack, StackNode> & IIncrementalParseState>
        IncrementalInputStackFactory<IIncrementalInputStack>
        factory(IStringDiff diff, ProcessUpdates<StackNode, ParseState> processUpdates) {
        return (inputString, previousInput, previousResult) -> new LinkedPreprocessingIncrementalInputStack(
            preProcessParseForest(processUpdates, diff, inputString, previousInput, previousResult), inputString);
    }

    @Override protected IStack<IncrementalParseForest> createStack() {
        return new LinkedStack<>();
    }

    @Override public LinkedPreprocessingIncrementalInputStack clone() {
        return new LinkedPreprocessingIncrementalInputStack(this);
    }

    @Override public boolean lookaheadIsUnchanged() {
        LinkedStack<IncrementalParseForest> stack = (LinkedStack<IncrementalParseForest>) this.stack;
        if(stack.head.next == null)
            return true;
        IncrementalParseForest node = stack.head.next.node;
        if(node.isTerminal())
            return true;
        return ((IncrementalParseNode) node).production() != null;
    }

}

