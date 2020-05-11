package org.spoofax.jsglr2.parser;

import java.util.Collections;

import org.spoofax.jsglr2.inputstack.IInputStack;
import org.spoofax.jsglr2.parseforest.IDerivation;
import org.spoofax.jsglr2.parseforest.IParseForest;
import org.spoofax.jsglr2.parseforest.IParseNode;
import org.spoofax.jsglr2.stack.IStackNode;

public class EmptyParseReporter {

    public static
//@formatter:off
   <ParseForest_ extends IParseForest,
    Derivation_  extends IDerivation<ParseForest_>,
    ParseNode_   extends IParseNode<ParseForest_, Derivation_>,
    StackNode_   extends IStackNode,
    InputStack_  extends IInputStack,
    ParseState_  extends AbstractParseState<InputStack_, StackNode_>>
//@formatter:on
    ParseReporterFactory<ParseForest_, Derivation_, ParseNode_, StackNode_, InputStack_, ParseState_> factory() {
        return parseForestManager -> (parseState, parseForest) -> Collections.emptyList();
    }

}
