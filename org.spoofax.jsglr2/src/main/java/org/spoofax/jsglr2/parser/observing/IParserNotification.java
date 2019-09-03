package org.spoofax.jsglr2.parser.observing;

import org.spoofax.jsglr2.parseforest.IParseForest;
import org.spoofax.jsglr2.parser.IParseState;
import org.spoofax.jsglr2.stack.IStackNode;

public interface IParserNotification
//@formatter:off
   <ParseForest extends IParseForest,
    StackNode   extends IStackNode,
    ParseState  extends IParseState<ParseForest, StackNode>>
//@formatter:on
{

    void notify(IParserObserver<ParseForest, StackNode, ParseState> observer);

}
