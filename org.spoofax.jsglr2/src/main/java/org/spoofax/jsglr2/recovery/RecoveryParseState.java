package org.spoofax.jsglr2.recovery;

import org.metaborg.parsetable.characterclasses.CharacterClassFactory;
import org.spoofax.jsglr2.parseforest.IParseForest;
import org.spoofax.jsglr2.parser.AbstractParseState;
import org.spoofax.jsglr2.parser.ParseStateFactory;
import org.spoofax.jsglr2.parser.Position;
import org.spoofax.jsglr2.stack.IStackNode;
import org.spoofax.jsglr2.stack.collections.IActiveStacks;
import org.spoofax.jsglr2.stack.collections.IForActorStacks;

import java.util.Optional;

public class RecoveryParseState
//@formatter:off
   <ParseForest extends IParseForest,
    StackNode   extends IStackNode>
//@formatter:on
    extends AbstractParseState<ParseForest, StackNode> implements IRecoveryParseState<ParseForest, StackNode> {

    public static
//@formatter:off
   <ParseForest_ extends IParseForest,
    StackNode_   extends IStackNode,
    ParseState_  extends AbstractParseState<ParseForest_, StackNode_> & IRecoveryParseState<ParseForest_, StackNode_>>
//@formatter:on
    ParseStateFactory<ParseForest_, StackNode_, ParseState_> factory() {
        return (inputString, filename, activeStacks, forActorStacks) -> (ParseState_) new RecoveryParseState<>(
            inputString, filename, activeStacks, forActorStacks);
    }

    private BacktrackChoicePoint[] backtrackChoicePoints;
    private Optional<RecoveryPoint> recoveryPointOpt = Optional.empty();

    RecoveryParseState(String inputString, String filename, IActiveStacks<StackNode> activeStacks,
        IForActorStacks<StackNode> forActorStacks) {
        super(inputString, filename, activeStacks, forActorStacks);
    }

    @Override public void initializeBacktrackChoicePoints(String input) {
        backtrackChoicePoints = new BacktrackChoicePoint[inputLineCount(input)];
    }

    private int inputLineCount(String input) {
        int lineCount = 1;

        for(char c : input.toCharArray()) {
            if(CharacterClassFactory.isNewLine(c))
                lineCount++;
        }

        return lineCount;
    }

    @Override public void saveBacktrackChoicePoint(Position position, Iterable<StackNode> activeStacks) {
        backtrackChoicePoints[position.line - 1] = new BacktrackChoicePoint<>(position, activeStacks);
    }

    @Override public BacktrackChoicePoint<ParseForest, StackNode> getBacktrackChoicePoint(int index) {
        return (BacktrackChoicePoint<ParseForest, StackNode>) backtrackChoicePoints[index];
    }

    @Override public void startRecovery(Position position) {
        recoveryPointOpt = Optional.of(new RecoveryPoint(position));

        // TODO: start using recovery productions
    }

    @Override public Optional<RecoveryPoint> recoveryPointOpt() {
        return recoveryPointOpt;
    }

    @Override public boolean nextRecoveryIteration() {
        if(recoveryPoint().hasNextIteration()) {
            int iteration = recoveryPoint().nextIteration();

            BacktrackChoicePoint backtrackChoicePoint = backtrackChoicePointForIteration(iteration);

            // TODO: reset parser state

            return true;
        } else
            return false;
    }

}
