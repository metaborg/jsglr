package org.spoofax.jsglr2.parser.observing;

import org.spoofax.jsglr2.parseforest.AbstractParseForest;
import org.spoofax.jsglr2.stack.AbstractStackNode;

public interface IParserNotification
//@formatter:off
   <ParseForest extends AbstractParseForest,
    StackNode   extends AbstractStackNode<ParseForest>>
//@formatter:on
{

    void notify(IParserObserver<ParseForest, StackNode> observer);

}
