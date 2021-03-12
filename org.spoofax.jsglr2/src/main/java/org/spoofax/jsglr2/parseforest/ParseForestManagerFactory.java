package org.spoofax.jsglr2.parseforest;

import org.spoofax.jsglr2.parser.AbstractParseState;
import org.spoofax.jsglr2.parser.observing.ParserObserving;
import org.spoofax.jsglr2.stack.IStackNode;

public interface ParseForestManagerFactory
//@formatter:off
   <ParseForest extends IParseForest,
    Derivation  extends IDerivation<ParseForest>,
    ParseNode   extends IParseNode<ParseForest, Derivation>,
    StackNode   extends IStackNode,
    ParseState  extends AbstractParseState<?, StackNode>>
//@formatter:on
{

    ParseForestManager<ParseForest, Derivation, ParseNode, StackNode, ParseState> get(
        ParserObserving<ParseForest, Derivation, ParseNode, StackNode, ParseState> observing,
        Disambiguator<ParseForest, Derivation, ParseNode, StackNode, ParseState> disambiguator);

}
