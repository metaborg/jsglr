package org.spoofax.jsglr2.recovery;

import org.spoofax.jsglr2.parseforest.IParseForest;
import org.spoofax.jsglr2.parser.Position;
import org.spoofax.jsglr2.stack.IStackNode;

import java.util.ArrayList;
import java.util.List;

public class BacktrackChoicePoint
//@formatter:off
   <ParseForest extends IParseForest,
    StackNode   extends IStackNode>
//@formatter:on
{

    public final Position position;
    public final List<StackNode> activeStacks;

    public BacktrackChoicePoint(Position position, Iterable<StackNode> activeStacks) {
        this.position = position;
        this.activeStacks = new ArrayList<>();

        for(StackNode activeStack : activeStacks)
            this.activeStacks.add(activeStack);
    }

}
