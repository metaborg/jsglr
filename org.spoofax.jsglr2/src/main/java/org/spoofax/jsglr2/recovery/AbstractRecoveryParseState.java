package org.spoofax.jsglr2.recovery;

import java.util.Stack;

import org.spoofax.jsglr2.inputstack.IInputStack;
import org.spoofax.jsglr2.parser.AbstractParseState;
import org.spoofax.jsglr2.stack.IStackNode;
import org.spoofax.jsglr2.stack.collections.IActiveStacks;
import org.spoofax.jsglr2.stack.collections.IForActorStacks;

public abstract class AbstractRecoveryParseState
//@formatter:off
   <InputStack           extends IInputStack,
    StackNode            extends IStackNode,
    BacktrackChoicePoint extends IBacktrackChoicePoint<InputStack, StackNode>>
//@formatter:on
    extends AbstractParseState<InputStack, StackNode>
    implements IRecoveryParseState<InputStack, StackNode, BacktrackChoicePoint> {

    Stack<BacktrackChoicePoint> backtrackChoicePoints = new Stack<>();
    private RecoveryJob recoveryJob = null;

    public AbstractRecoveryParseState(InputStack inputStack, IActiveStacks<StackNode> activeStacks,
        IForActorStacks<StackNode> forActorStacks) {
        super(inputStack, activeStacks, forActorStacks);
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
        // TODO this cast is ugly, but there's no way around it
        this.inputStack = (InputStack) backtrackChoicePoint.inputStack().clone();

        this.activeStacks.clear();

        for(StackNode activeStack : backtrackChoicePoint.activeStacks())
            this.activeStacks.add(activeStack);
    }

}
