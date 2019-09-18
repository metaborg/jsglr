package org.spoofax.jsglr2.parseforest;

import org.spoofax.jsglr2.parser.AbstractParseState;
import org.spoofax.jsglr2.parser.observing.ParserObserving;
import org.spoofax.jsglr2.stack.IStackNode;

public interface ParseForestManagerFactory
//@formatter:off
   <ParseForest extends IParseForest,
    ParseNode   extends ParseForest,
    Derivation  extends IDerivation<ParseForest>,
    StackNode   extends IStackNode,
    ParseState  extends AbstractParseState<ParseForest, StackNode>>
//@formatter:on
{

    ParseForestManager<ParseForest, ParseNode, Derivation, StackNode, ParseState>
        get(ParserObserving<ParseForest, StackNode, ParseState> observing);

}
