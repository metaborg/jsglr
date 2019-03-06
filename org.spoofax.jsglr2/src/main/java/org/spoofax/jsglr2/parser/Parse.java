package org.spoofax.jsglr2.parser;

import org.spoofax.jsglr2.parseforest.IParseForest;
import org.spoofax.jsglr2.parser.observing.ParserObserving;
import org.spoofax.jsglr2.stack.IStackNode;
import org.spoofax.jsglr2.stack.collections.IActiveStacks;
import org.spoofax.jsglr2.stack.collections.IForActorStacks;

public class Parse
//@formatter:off
   <ParseForest extends IParseForest,
    StackNode   extends IStackNode>
//@formatter:on
    extends AbstractParse<ParseForest, StackNode> {

    public static <ParseForest_ extends IParseForest, StackNode_ extends IStackNode>
        ParseFactory<ParseForest_, StackNode_, AbstractParse<ParseForest_, StackNode_>> factory() {
        return Parse::new;
    }

    public Parse(String inputString, String filename, IActiveStacks<StackNode> activeStacks,
        IForActorStacks<StackNode> forActorStacks, ParserObserving<ParseForest, StackNode> observing) {
        super(inputString, filename, activeStacks, forActorStacks, observing);
    }
}
