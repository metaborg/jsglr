package org.spoofax.jsglr2.recovery;

import java.util.ArrayList;
import java.util.List;

import org.spoofax.jsglr2.inputstack.IInputStack;
import org.spoofax.jsglr2.stack.IStackNode;

public class BacktrackChoicePoint<InputStack extends IInputStack, StackNode extends IStackNode>
    implements IBacktrackChoicePoint<InputStack, StackNode> {

    public final int offset;
    public final InputStack inputStack;
    public final List<StackNode> activeStacks;

    public BacktrackChoicePoint(InputStack inputStack, Iterable<StackNode> activeStacks) {
        this.offset = inputStack.offset();
        this.inputStack = inputStack;
        this.activeStacks = new ArrayList<>();

        for(StackNode activeStack : activeStacks)
            this.activeStacks.add(activeStack);
    }

    @Override public int offset() {
        return offset;
    }

    @Override public List<StackNode> activeStacks() {
        return activeStacks;
    }

    @Override public InputStack inputStack() {
        return inputStack;
    }
}
