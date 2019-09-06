package org.spoofax.jsglr2.recovery;

import java.util.ArrayList;
import java.util.List;

import org.metaborg.parsetable.characterclasses.CharacterClassFactory;
import org.spoofax.jsglr2.parseforest.IParseForest;
import org.spoofax.jsglr2.parser.IParseState;
import org.spoofax.jsglr2.parser.ParseStateFactory;
import org.spoofax.jsglr2.parser.Position;
import org.spoofax.jsglr2.stack.IStackNode;

public class RecoveryParseState
//@formatter:off
   <ParseForest extends IParseForest,
    StackNode   extends IStackNode>
//@formatter:on
    implements IParseState<ParseForest, StackNode>, IRecoveryState<ParseForest, StackNode> {

    public static
//@formatter:off
   <ParseForest_ extends IParseForest,
    StackNode_   extends IStackNode,
    ParseState_  extends IParseState<ParseForest_, StackNode_> & IRecoveryState<ParseForest_, StackNode_>>
//@formatter:on
    ParseStateFactory<ParseForest_, StackNode_, ParseState_> factory() {
        return () -> (ParseState_) new RecoveryParseState<>();
    }

    private BacktrackChoicePoint[] backtrackChoicePoints;

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

}
