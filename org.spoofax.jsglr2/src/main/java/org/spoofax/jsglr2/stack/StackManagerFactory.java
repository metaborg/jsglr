package org.spoofax.jsglr2.stack;

import org.spoofax.jsglr2.parseforest.IDerivation;
import org.spoofax.jsglr2.parseforest.IParseForest;
import org.spoofax.jsglr2.parseforest.IParseNode;
import org.spoofax.jsglr2.parser.AbstractParseState;
import org.spoofax.jsglr2.parser.observing.ParserObserving;

public interface StackManagerFactory
//@formatter:off
   <ParseForest  extends IParseForest,
    Derivation   extends IDerivation<ParseForest>,
    ParseNode    extends IParseNode<ParseForest, Derivation>,
    StackNode    extends IStackNode,
    ParseState   extends AbstractParseState<ParseForest, StackNode>,
    StackManager extends AbstractStackManager<ParseForest, Derivation, ParseNode, StackNode, ParseState>>
//@formatter:on
{

    StackManager get(ParserObserving<ParseForest, Derivation, ParseNode, StackNode, ParseState> observing);

}
