package org.spoofax.jsglr2.parser;

import org.spoofax.jsglr2.parseforest.AbstractParseForest;
import org.spoofax.jsglr2.parser.observing.ParserObserving;
import org.spoofax.jsglr2.stack.AbstractStackNode;
import org.spoofax.jsglr2.stack.collections.IActiveStacks;
import org.spoofax.jsglr2.stack.collections.IForActorStacks;

public interface ParseFactory
//@formatter:off
   <ParseForest extends AbstractParseForest,
    StackNode   extends AbstractStackNode<ParseForest>,
    Parse       extends AbstractParse<ParseForest, StackNode>>
//@formatter:on
{

    Parse get(String inputString, String filename, IActiveStacks<StackNode> activeStacks,
        IForActorStacks<StackNode> forActorStacks, ParserObserving<ParseForest, StackNode> observing);

}