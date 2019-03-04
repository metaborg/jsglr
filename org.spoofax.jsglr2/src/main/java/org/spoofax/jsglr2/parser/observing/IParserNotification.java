package org.spoofax.jsglr2.parser.observing;

import org.spoofax.jsglr2.parseforest.IParseForest;
import org.spoofax.jsglr2.stack.AbstractStackNode;

public interface IParserNotification
//@formatter:off
   <ParseForest extends IParseForest,
    StackNode   extends AbstractStackNode<ParseForest>>
//@formatter:on
{

    void notify(IParserObserver<ParseForest, StackNode> observer);

}
