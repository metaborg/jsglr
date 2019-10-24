package org.spoofax.jsglr2.recovery;

import org.metaborg.parsetable.characterclasses.CharacterClassFactory;
import org.spoofax.jsglr2.parseforest.IDerivation;
import org.spoofax.jsglr2.parseforest.IParseForest;
import org.spoofax.jsglr2.parseforest.IParseNode;
import org.spoofax.jsglr2.parser.AbstractParseState;
import org.spoofax.jsglr2.parser.ParseStateFactory;
import org.spoofax.jsglr2.parser.ParserVariant;
import org.spoofax.jsglr2.stack.IStackNode;
import org.spoofax.jsglr2.stack.collections.ActiveStacksFactory;
import org.spoofax.jsglr2.stack.collections.ForActorStacksFactory;
import org.spoofax.jsglr2.stack.collections.IActiveStacks;
import org.spoofax.jsglr2.stack.collections.IForActorStacks;

import java.util.Optional;

public class RecoveryParseState
//@formatter:off
   <ParseForest extends IParseForest,
    StackNode   extends IStackNode>
//@formatter:on
    extends AbstractParseState<ParseForest, StackNode> implements IRecoveryParseState<ParseForest, StackNode> {

    private BacktrackChoicePoint[] backtrackChoicePoints;
    private int backtrackChoicePointCount = 0;
    private Optional<RecoveryJob> recoveryPointOpt = Optional.empty();

    RecoveryParseState(String inputString, String filename, IActiveStacks<StackNode> activeStacks,
        IForActorStacks<StackNode> forActorStacks) {
        super(inputString, filename, activeStacks, forActorStacks);
    }

    @Override public void initializeBacktrackChoicePoints(String input) {
        backtrackChoicePoints = new BacktrackChoicePoint[inputLineCount(input)];
    }

    public static
//@formatter:off
   <ParseForest_ extends IParseForest,
    Derivation_  extends IDerivation<ParseForest_>,
    ParseNode_   extends IParseNode<ParseForest_, Derivation_>,
    StackNode_   extends IStackNode,
    ParseState_  extends AbstractParseState<ParseForest_, StackNode_> & IRecoveryParseState<ParseForest_, StackNode_>>
//@formatter:on
    ParseStateFactory<ParseForest_, Derivation_, ParseNode_, StackNode_, ParseState_> factory(ParserVariant variant) {
        return (inputString, filename, observing) -> {
            IActiveStacks<StackNode_> activeStacks =
                new ActiveStacksFactory(variant.activeStacksRepresentation).get(observing);
            IForActorStacks<StackNode_> forActorStacks =
                new ForActorStacksFactory(variant.forActorStacksRepresentation).get(observing);

            return (ParseState_) new RecoveryParseState<>(inputString, filename, activeStacks, forActorStacks);
        };
    }

    private int inputLineCount(String input) {
        int lineCount = 1;

        for(char c : input.toCharArray()) {
            if(CharacterClassFactory.isNewLine(c))
                lineCount++;
        }

        return lineCount;
    }

    @Override public void saveBacktrackChoicePoint(int offset, Iterable<StackNode> activeStacks) {
        backtrackChoicePoints[backtrackChoicePointCount++] = new BacktrackChoicePoint<>(offset, activeStacks);
    }

    @Override public BacktrackChoicePoint<ParseForest, StackNode> getBacktrackChoicePoint(int index) {
        return (BacktrackChoicePoint<ParseForest, StackNode>) backtrackChoicePoints[index];
    }

    @Override public void startRecovery(int offset) {
        recoveryPointOpt = Optional.of(new RecoveryJob(backtrackChoicePointCount - 1, offset));
    }

    @Override public void endRecovery() {
        recoveryPointOpt = Optional.empty();
    }

    @Override public Optional<RecoveryJob> recoveryJobOpt() {
        return recoveryPointOpt;
    }

    @Override public boolean nextRecoveryIteration() {
        if(recoveryJob().hasNextIteration()) {
            int iteration = recoveryJob().nextIteration();

            BacktrackChoicePoint<ParseForest, StackNode> backtrackChoicePoint =
                getBacktrackChoicePoint(recoveryJob().backtrackChoicePointIndex);

            this.backtrackChoicePointCount = recoveryJob().backtrackChoicePointIndex + 1;

            this.currentOffset = backtrackChoicePoint.offset;

            // TODO: lines below required for layout-sensitive
            // this.currentLine = backtrackChoicePoint.position.line;
            // this.currentColumn = backtrackChoicePoint.position.column;

            this.currentChar = getChar(currentOffset);

            this.activeStacks.clear();

            for(StackNode activeStack : backtrackChoicePoint.activeStacks)
                this.activeStacks.add(activeStack);

            return true;
        } else
            return false;
    }

}
