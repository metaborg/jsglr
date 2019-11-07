package org.spoofax.jsglr2.recovery;

import org.spoofax.jsglr2.parser.AbstractParseState;
import org.spoofax.jsglr2.stack.IStackNode;
import org.spoofax.jsglr2.stack.collections.IActiveStacks;
import org.spoofax.jsglr2.stack.collections.IForActorStacks;

import java.util.Stack;

public abstract class AbstractRecoveryParseState
//@formatter:off
   <StackNode            extends IStackNode,
    BacktrackChoicePoint extends IBacktrackChoicePoint<StackNode>>
//@formatter:on
    extends AbstractParseState<StackNode> implements IRecoveryParseState<StackNode, BacktrackChoicePoint> {

    Stack<BacktrackChoicePoint> backtrackChoicePoints = new Stack<>();
    private RecoveryJob recoveryJob = null;

    public AbstractRecoveryParseState(String inputString, String filename, IActiveStacks<StackNode> activeStacks,
        IForActorStacks<StackNode> forActorStacks) {
        super(inputString, filename, activeStacks, forActorStacks);
    }

    @Override public Stack<BacktrackChoicePoint> backtrackChoicePoints() {
        return backtrackChoicePoints;
    }

    @Override public void startRecovery(int offset) {
        recoveryJob = new RecoveryJob(offset, RecoveryConfig.RECOVERY_ITERATIONS_QUOTA);
    }

    @Override public void endRecovery() {
        recoveryJob = null;
    }

    @Override public RecoveryJob recoveryJob() {
        return recoveryJob;
    }

    @Override public boolean nextRecoveryIteration() {
        if(recoveryJob().hasNextIteration()) {
            int iteration = recoveryJob().nextIteration();

            for(int i = iteration; i > 0 && backtrackChoicePoints.size() > 1; i--)
                backtrackChoicePoints.pop();

            resetToBacktrackChoicePoint(backtrackChoicePoints.peek());

            return true;
        } else
            return false;
    }

    protected void resetToBacktrackChoicePoint(BacktrackChoicePoint backtrackChoicePoint) {
        this.currentOffset = backtrackChoicePoint.offset();
        this.currentChar = getChar(currentOffset);

        this.activeStacks.clear();

        for(StackNode activeStack : backtrackChoicePoint.activeStacks())
            this.activeStacks.add(activeStack);
    }

}
