package org.spoofax.jsglr2.recovery;

import java.util.Stack;

import org.metaborg.parsetable.characterclasses.CharacterClassFactory;
import org.spoofax.jsglr2.JSGLR2Request;
import org.spoofax.jsglr2.inputstack.IInputStack;
import org.spoofax.jsglr2.parser.AbstractParseState;
import org.spoofax.jsglr2.parser.observing.ParserObserving;
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
    private RecoveryJob<StackNode> recoveryJob = null;

    public AbstractRecoveryParseState(JSGLR2Request request, InputStack inputStack,
        IActiveStacks<StackNode> activeStacks, IForActorStacks<StackNode> forActorStacks) {
        super(request, inputStack, activeStacks, forActorStacks);
    }

    @Override public void nextParseRound(ParserObserving observing) {
        super.nextParseRound(observing);
        int currentOffset = inputStack.offset();

        // Record backtrack choice points per line.
        // If in recovery mode, only record new choice points when parsing after the point that initiated recovery.
        if((currentOffset == 0 || CharacterClassFactory.isNewLine(inputStack.getChar(currentOffset - 1)))
            && (!isRecovering() || lastBacktrackChoicePoint().offset() < currentOffset)) {
            IBacktrackChoicePoint<?, StackNode> choicePoint = saveBacktrackChoicePoint();

            observing.notify(
                observer -> observer.recoveryBacktrackChoicePoint(backtrackChoicePoints().size() - 1, choicePoint));
        }

        if(successfulRecovery(request, currentOffset)) {
            endRecovery();

            observing.notify(observer -> observer.endRecovery(this));
        }
    }

    @Override public Stack<BacktrackChoicePoint> backtrackChoicePoints() {
        return backtrackChoicePoints;
    }

    @Override public void startRecovery(JSGLR2Request request, int offset) {
        recoveryJob = new RecoveryJob<>(offset, request.recoveryIterationsQuota);
    }

    @Override public void endRecovery() {
        recoveryJob = null;
    }

    @Override public RecoveryJob<StackNode> recoveryJob() {
        return recoveryJob;
    }

    @Override public boolean nextRecoveryIteration() {
        if(recoveryJob().hasNextIteration()) {
            int iteration = recoveryJob().nextIteration();

            for(int i = iteration; i > 0 && backtrackChoicePoints.size() > 1; i--)
                backtrackChoicePoints.pop();

            resetToBacktrackChoicePoint(backtrackChoicePoints.peek());

            recoveryJob.initQuota(activeStacks);

            return true;
        } else
            return false;
    }

    @SuppressWarnings("unchecked") protected void
        resetToBacktrackChoicePoint(BacktrackChoicePoint backtrackChoicePoint) {
        // This cast is ugly, but there's no way around it.
        // The subclasses of `IInputStack` specialize the return type of `clone` to be their own class,
        // but this information cannot be stored in the type parameter `InputStack` of this class.
        // As programmers, we assume that the backtrack choice points contain an input stack of the same type each time.
        this.inputStack = (InputStack) backtrackChoicePoint.inputStack().clone();

        this.activeStacks.clear();

        for(StackNode activeStack : backtrackChoicePoint.activeStacks())
            this.activeStacks.add(activeStack);
    }

}
