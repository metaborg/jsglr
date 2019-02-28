package org.spoofax.jsglr2.parser;

import org.spoofax.jsglr2.parseforest.AbstractParseForest;
import org.spoofax.jsglr2.parser.observing.ParserObserving;
import org.spoofax.jsglr2.stack.AbstractStackNode;
import org.spoofax.jsglr2.stack.collections.IActiveStacks;
import org.spoofax.jsglr2.stack.collections.IForActorStacks;

public class Parse
//@formatter:off
   <ParseForest extends AbstractParseForest,
    StackNode   extends AbstractStackNode<ParseForest>>
//@formatter:on
    extends AbstractParse<ParseForest, StackNode> {

    public static <ParseForest_ extends AbstractParseForest, StackNode_ extends AbstractStackNode<ParseForest_>>
        ParseFactory<ParseForest_, StackNode_, Parse<ParseForest_, StackNode_>> factory() {
        return (inputString, filename, activeStacks, forActorStacks, observing) -> new Parse<>(inputString, filename,
            activeStacks, forActorStacks, observing);
    }

    public Parse(String inputString, String filename, IActiveStacks<StackNode> activeStacks,
        IForActorStacks<StackNode> forActorStacks, ParserObserving<ParseForest, StackNode> observing) {
        super(inputString, filename, activeStacks, forActorStacks, observing);
    }
}
