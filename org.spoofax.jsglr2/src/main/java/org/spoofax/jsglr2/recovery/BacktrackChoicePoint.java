package org.spoofax.jsglr2.recovery;

import org.spoofax.jsglr2.stack.IStackNode;

import java.util.ArrayList;
import java.util.List;

public class BacktrackChoicePoint<StackNode extends IStackNode> implements IBacktrackChoicePoint<StackNode> {

    public final int offset;
    public final List<StackNode> activeStacks;

    public BacktrackChoicePoint(int offset, Iterable<StackNode> activeStacks) {
        this.offset = offset;
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
}
