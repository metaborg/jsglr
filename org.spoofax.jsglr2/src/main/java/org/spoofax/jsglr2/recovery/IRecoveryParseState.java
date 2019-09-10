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

    void startRecovery(Position position);

    Optional<RecoveryPoint> recoveryPointOpt();

    default boolean isRecovering() {
        return recoveryPointOpt().isPresent();
    }

    default RecoveryPoint recoveryPoint() {
        return recoveryPointOpt().get();
    }

    default BacktrackChoicePoint<ParseForest, StackNode> backtrackChoicePointForIteration(int i) {
        int recoveryLine = recoveryPoint().position.line;

        return getBacktrackChoicePoint(Math.max(recoveryLine - i, 0));
    }

    boolean nextRecoveryIteration();

}
