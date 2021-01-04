package org.spoofax.jsglr2.recovery;

import java.util.Stack;

import org.spoofax.jsglr2.JSGLR2Request;
import org.spoofax.jsglr2.inputstack.IInputStack;
import org.spoofax.jsglr2.stack.IStackNode;

public interface IRecoveryParseState
//@formatter:off
   <InputStack           extends IInputStack,
    StackNode            extends IStackNode,
    BacktrackChoicePoint extends IBacktrackChoicePoint<InputStack, StackNode>>
//@formatter:on
{

    Stack<BacktrackChoicePoint> backtrackChoicePoints();

    BacktrackChoicePoint createBacktrackChoicePoint();

    default BacktrackChoicePoint saveBacktrackChoicePoint() {
        return backtrackChoicePoints().push(createBacktrackChoicePoint());
    }

    default BacktrackChoicePoint lastBacktrackChoicePoint() {
        return backtrackChoicePoints().peek();
    }

    void startRecovery(JSGLR2Request request, int offset);

    void endRecovery();

    RecoveryJob<StackNode> recoveryJob();

    default boolean isRecovering() {
        return recoveryJob() != null;
    }

    boolean nextRecoveryIteration();

    default boolean successfulRecovery(JSGLR2Request request, int currentOffset) {
        return isRecovering() && currentOffset >= recoveryJob().offset + request.succeedingRecoveryOffset;
    }

    boolean appliedRecovery();

    void setAppliedRecovery();

}
