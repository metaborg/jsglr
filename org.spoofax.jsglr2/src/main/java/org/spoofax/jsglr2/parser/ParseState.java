package org.spoofax.jsglr2.parser;

import org.spoofax.jsglr2.parseforest.IParseForest;
import org.spoofax.jsglr2.stack.IStackNode;

public class ParseState
//@formatter:off
   <ParseForest extends IParseForest,
    StackNode   extends IStackNode>
//@formatter:on
    implements IParseState<ParseForest, StackNode> {

    public static
//@formatter:off
   <ParseForest_ extends IParseForest,
    StackNode_   extends IStackNode,
    ParseState_  extends IParseState<ParseForest_, StackNode_>>
//@formatter:on
   ParseStateFactory<ParseForest_, StackNode_, ParseState_> factory() {
        return () -> (ParseState_) new ParseState<>();
    }

}
