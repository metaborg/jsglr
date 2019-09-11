package org.spoofax.jsglr2.recovery;

import org.spoofax.jsglr2.parseforest.IParseForest;
import org.spoofax.jsglr2.parser.Position;
import org.spoofax.jsglr2.stack.IStackNode;

import java.util.Optional;

public interface IRecoveryParseState
//@formatter:off
   <ParseForest extends IParseForest,
    StackNode   extends IStackNode>
//@formatter:on
{

    void initializeBacktrackChoicePoints(String input);

    void saveBacktrackChoicePoint(Position position, Iterable<StackNode> activeStacks);

    BacktrackChoicePoint<ParseForest, StackNode> getBacktrackChoicePoint(int line);

    void setRecovery(Position position);

    Optional<RecoveryJob> recoveryJobOpt();

    default boolean isRecovering() {
        return recoveryJobOpt().isPresent();
    }

    default RecoveryJob recoveryJob() {
        return recoveryJobOpt().get();
    }

    default BacktrackChoicePoint<ParseForest, StackNode> backtrackChoicePointForIteration(int i) {
        int recoveryLine = recoveryJob().position.line;

        return getBacktrackChoicePoint(Math.max(recoveryLine - i, 0));
    }

    boolean nextRecoveryIteration();

}
