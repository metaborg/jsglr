package org.spoofax.jsglr2.parser;

import org.spoofax.jsglr2.JSGLR2Request;
import org.spoofax.jsglr2.inputstack.IInputStack;
import org.spoofax.jsglr2.parseforest.IDerivation;
import org.spoofax.jsglr2.parseforest.IParseForest;
import org.spoofax.jsglr2.parseforest.IParseNode;
import org.spoofax.jsglr2.parser.observing.ParserObserving;
import org.spoofax.jsglr2.stack.IStackNode;

public interface ParseStateFactory
//@formatter:off
   <ParseForest extends IParseForest,
    Derivation  extends IDerivation<ParseForest>,
    ParseNode   extends IParseNode<ParseForest, Derivation>,
    InputStack  extends IInputStack,
    StackNode   extends IStackNode,
    ParseState  extends AbstractParseState<InputStack, StackNode>>
//@formatter:on
{

    ParseState get(JSGLR2Request request, InputStack inputStack,
        ParserObserving<ParseForest, Derivation, ParseNode, StackNode, ParseState> observing);

}
