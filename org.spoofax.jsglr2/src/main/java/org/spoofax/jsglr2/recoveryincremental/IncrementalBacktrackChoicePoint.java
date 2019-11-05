package org.spoofax.jsglr2.recoveryincremental;

import org.spoofax.jsglr2.incremental.lookaheadstack.ILookaheadStack;
import org.spoofax.jsglr2.recovery.BacktrackChoicePoint;
import org.spoofax.jsglr2.stack.IStackNode;

public class IncrementalBacktrackChoicePoint<StackNode extends IStackNode> extends BacktrackChoicePoint<StackNode> {

    public final ILookaheadStack lookahead;

    public IncrementalBacktrackChoicePoint(int index, int offset, Iterable<StackNode> activeStacks, ILookaheadStack lookahead) {
        super(index, offset, activeStacks);

        this.lookahead = lookahead;
    }

}
