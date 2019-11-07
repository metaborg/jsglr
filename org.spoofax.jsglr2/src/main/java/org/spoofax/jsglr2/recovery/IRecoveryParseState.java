package org.spoofax.jsglr2.recovery;

import org.spoofax.jsglr2.stack.IStackNode;

import java.util.Optional;
import java.util.Stack;

public interface IRecoveryParseState
//@formatter:off
   <StackNode            extends IStackNode,
    BacktrackChoicePoint extends IBacktrackChoicePoint<StackNode>>
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

    void startRecovery(int offset);

    void endRecovery();

    Optional<RecoveryJob> recoveryJobOpt();

    default boolean isRecovering() {
        return recoveryJobOpt().isPresent();
    }

    default boolean successfulRecovery(int currentOffset) {
        return isRecovering() && currentOffset >= recoveryJob().offset + RecoveryConfig.SUCCEEDING_RECOVERY_OFFSET;
    }

    default RecoveryJob recoveryJob() {
        return recoveryJobOpt().get();
    }

    boolean nextRecoveryIteration();

}
