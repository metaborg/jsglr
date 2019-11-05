package org.spoofax.jsglr2.recoveryincremental;

import org.spoofax.jsglr2.recovery.BacktrackChoicePoint;
import org.spoofax.jsglr2.stack.IStackNode;

public class IncrementalBacktrackChoicePoint<StackNode extends IStackNode> extends BacktrackChoicePoint<StackNode> {

    public IncrementalBacktrackChoicePoint(int index, int offset, Iterable<StackNode> activeStacks) {
        super(index, offset, activeStacks);
    }

}
