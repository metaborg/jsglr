package org.spoofax.jsglr2.recovery;

import org.spoofax.jsglr2.parseforest.IParseForest;
import org.spoofax.jsglr2.stack.IStackNode;

import java.util.ArrayList;
import java.util.List;

public class BacktrackChoicePoint
//@formatter:off
   <ParseForest extends IParseForest,
    StackNode   extends IStackNode>
//@formatter:on
{

    public final int offset;
    public final List<StackNode> activeStacks;

    public BacktrackChoicePoint(int offset, Iterable<StackNode> activeStacks) {
        this.offset = offset;
        this.activeStacks = new ArrayList<>();

        for(StackNode activeStack : activeStacks)
            this.activeStacks.add(activeStack);
    }

}
