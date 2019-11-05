package org.spoofax.jsglr2.recovery;

import org.metaborg.parsetable.characterclasses.CharacterClassFactory;
import org.spoofax.jsglr2.parseforest.IParseForest;
import org.spoofax.jsglr2.parser.AbstractParseState;
import org.spoofax.jsglr2.stack.IStackNode;
import org.spoofax.jsglr2.stack.collections.IActiveStacks;
import org.spoofax.jsglr2.stack.collections.IForActorStacks;

import java.util.Optional;

public abstract class AbstractRecoveryParseState
//@formatter:off
   <ParseForest          extends IParseForest,
    StackNode            extends IStackNode,
    BacktrackChoicePoint extends IBacktrackChoicePoint<StackNode>>
//@formatter:on
    extends AbstractParseState<ParseForest, StackNode> implements IRecoveryParseState<StackNode, BacktrackChoicePoint> {

    protected BacktrackChoicePoint[] backtrackChoicePoints;
    protected int lastBacktrackChoicePointIndex = -1;
    private Optional<RecoveryJob> recoveryPointOpt = Optional.empty();

    public AbstractRecoveryParseState(String inputString, String filename, IActiveStacks<StackNode> activeStacks,
        IForActorStacks<StackNode> forActorStacks) {
        super(inputString, filename, activeStacks, forActorStacks);
    }

    abstract public void initializeBacktrackChoicePoints(String input);

    protected int inputLineCount(String input) {
        int lineCount = 1;

        for(char c : input.toCharArray()) {
            if(CharacterClassFactory.isNewLine(c))
                lineCount++;
        }

        return lineCount;
    }

    @Override public BacktrackChoicePoint saveBacktrackChoicePoint(int offset, Iterable<StackNode> activeStacks) {
        BacktrackChoicePoint backtrackChoicePoint = createBacktrackChoicePoint(offset, activeStacks);

        backtrackChoicePoints[backtrackChoicePoint.index()] = backtrackChoicePoint;

        return backtrackChoicePoint;
    }

    abstract protected BacktrackChoicePoint createBacktrackChoicePoint(int offset, Iterable<StackNode> activeStacks);

    @Override public BacktrackChoicePoint getBacktrackChoicePoint(int index) {
        return backtrackChoicePoints[index];
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
            recoveryJob().nextIteration();

            BacktrackChoicePoint backtrackChoicePoint =
                getBacktrackChoicePoint(recoveryJob().iterationBacktrackChoicePointIndex());

            resetToBacktrackChoicePoint(backtrackChoicePoint);

            return true;
        } else
            return false;
    }

    protected void resetToBacktrackChoicePoint(BacktrackChoicePoint backtrackChoicePoint) {
        this.lastBacktrackChoicePointIndex = backtrackChoicePoint.index();
        this.currentOffset = backtrackChoicePoint.offset();

        this.currentChar = getChar(currentOffset);

        this.activeStacks.clear();

        for(StackNode activeStack : backtrackChoicePoint.activeStacks())
            this.activeStacks.add(activeStack);
    }

}
