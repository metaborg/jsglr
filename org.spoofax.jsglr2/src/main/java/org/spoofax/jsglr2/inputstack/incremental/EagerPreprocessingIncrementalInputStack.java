package org.spoofax.jsglr2.inputstack.incremental;

import org.spoofax.jsglr2.incremental.IIncrementalParseState;
import org.spoofax.jsglr2.incremental.diff.IStringDiff;
import org.spoofax.jsglr2.incremental.diff.ProcessUpdates;
import org.spoofax.jsglr2.incremental.parseforest.IncrementalParseForest;
import org.spoofax.jsglr2.incremental.parseforest.IncrementalParseNode;
import org.spoofax.jsglr2.parser.AbstractParseState;
import org.spoofax.jsglr2.stack.IStackNode;

public class EagerPreprocessingIncrementalInputStack extends AbstractPreprocessingIncrementalInputStack {

    EagerPreprocessingIncrementalInputStack(EagerPreprocessingIncrementalInputStack original) {
        super(original);
    }

    public EagerPreprocessingIncrementalInputStack(IncrementalParseForest root, String inputString) {
        super(root, inputString);
    }

    public static <StackNode extends IStackNode, ParseState extends AbstractParseState<IIncrementalInputStack, StackNode> & IIncrementalParseState>
        IncrementalInputStackFactory<IIncrementalInputStack>
        factory(IStringDiff diff, ProcessUpdates<StackNode, ParseState> processUpdates) {
        return (inputString, previousInput, previousResult) -> new EagerPreprocessingIncrementalInputStack(
            preProcessParseForest(processUpdates, diff, inputString, previousInput, previousResult), inputString);
    }

    @Override protected CloneableStack<IncrementalParseForest> createStack() {
        return new CloneableStack<>();
    }

    @Override public EagerPreprocessingIncrementalInputStack clone() {
        return new EagerPreprocessingIncrementalInputStack(this);
    }

    @Override public boolean lookaheadIsUnchanged() {
        CloneableStack<IncrementalParseForest> stack = (CloneableStack<IncrementalParseForest>) this.stack;
        if(stack.size() < 2)
            return true; // EOF is always unchanged
        IncrementalParseForest node = stack.get(stack.size() - 2);
        if(node.isTerminal())
            return true;
        return ((IncrementalParseNode) node).production() != null;
    }
}
