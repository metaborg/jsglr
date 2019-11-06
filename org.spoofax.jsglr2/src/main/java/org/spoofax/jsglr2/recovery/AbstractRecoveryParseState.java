package org.spoofax.jsglr2.recovery;

import org.spoofax.jsglr2.parser.AbstractParseState;
import org.spoofax.jsglr2.stack.IStackNode;
import org.spoofax.jsglr2.stack.collections.IActiveStacks;
import org.spoofax.jsglr2.stack.collections.IForActorStacks;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class AbstractRecoveryParseState
//@formatter:off
   <StackNode            extends IStackNode,
    BacktrackChoicePoint extends IBacktrackChoicePoint<StackNode>>
//@formatter:on
    extends AbstractParseState<StackNode> implements IRecoveryParseState<StackNode, BacktrackChoicePoint> {

    protected List<BacktrackChoicePoint> backtrackChoicePoints = new ArrayList<>();
    protected int lastBacktrackChoicePointIndex = -1;
    private Optional<RecoveryJob> recoveryPointOpt = Optional.empty();

    public AbstractRecoveryParseState(String inputString, String filename, IActiveStacks<StackNode> activeStacks,
        IForActorStacks<StackNode> forActorStacks) {
        super(inputString, filename, activeStacks, forActorStacks);
    }

    @Override public int lastBacktrackChoicePointIndex() {
        return lastBacktrackChoicePointIndex;
    }

    @Override public BacktrackChoicePoint saveBacktrackChoicePoint() {
        BacktrackChoicePoint backtrackChoicePoint = createBacktrackChoicePoint();

        backtrackChoicePoints.add(++lastBacktrackChoicePointIndex, backtrackChoicePoint);

        return backtrackChoicePoint;
    }

    abstract protected BacktrackChoicePoint createBacktrackChoicePoint();

    @Override public BacktrackChoicePoint getBacktrackChoicePoint(int index) {
        return backtrackChoicePoints.get(index);
    }

    @Override public void startRecovery(int offset) {
        recoveryPointOpt = Optional
            .of(new RecoveryJob(lastBacktrackChoicePointIndex, offset, RecoveryConfig.RECOVERY_ITERATIONS_QUOTA));
    }

    @Override public void endRecovery() {
        recoveryPointOpt = Optional.empty();
    }

    @Override public Optional<RecoveryJob> recoveryJobOpt() {
        return recoveryPointOpt;
    }

    @Override public boolean nextRecoveryIteration() {
        if(recoveryJob().hasNextIteration()) {
            int backtrackChoicePointIndex = recoveryJob().nextIteration().iterationBacktrackChoicePointIndex();

            BacktrackChoicePoint backtrackChoicePoint = getBacktrackChoicePoint(backtrackChoicePointIndex);

            resetToBacktrackChoicePoint(backtrackChoicePoint, backtrackChoicePointIndex);

            return true;
        } else
            return false;
    }

    protected void resetToBacktrackChoicePoint(BacktrackChoicePoint backtrackChoicePoint,
        int backtrackChoicePointIndex) {
        this.lastBacktrackChoicePointIndex = backtrackChoicePointIndex;
        this.currentOffset = backtrackChoicePoint.offset();

        this.currentChar = getChar(currentOffset);

        this.activeStacks.clear();

        for(StackNode activeStack : backtrackChoicePoint.activeStacks())
            this.activeStacks.add(activeStack);
    }

}
