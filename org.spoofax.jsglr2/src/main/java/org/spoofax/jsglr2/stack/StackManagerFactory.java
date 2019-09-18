package org.spoofax.jsglr2.stack;

import org.spoofax.jsglr2.parseforest.IParseForest;
import org.spoofax.jsglr2.parser.AbstractParseState;
import org.spoofax.jsglr2.parser.observing.ParserObserving;

public interface StackManagerFactory
//@formatter:off
   <ParseForest  extends IParseForest,
    StackNode    extends IStackNode,
    ParseState   extends AbstractParseState<ParseForest, StackNode>,
    StackManager extends AbstractStackManager<ParseForest, StackNode, ParseState>>
//@formatter:on
{

    StackManager get(ParserObserving<ParseForest, StackNode, ParseState> observing);

}
