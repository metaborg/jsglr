package org.spoofax.jsglr2.parser.failure;

import org.spoofax.jsglr2.parseforest.IDerivation;
import org.spoofax.jsglr2.parseforest.IParseForest;
import org.spoofax.jsglr2.parseforest.IParseNode;
import org.spoofax.jsglr2.parser.AbstractParseState;
import org.spoofax.jsglr2.parser.observing.ParserObserving;
import org.spoofax.jsglr2.parser.result.ParseFailureType;
import org.spoofax.jsglr2.stack.IStackNode;

public class DefaultParseFailureHandler
//@formatter:off
   <ParseForest extends IParseForest,
    Derivation  extends IDerivation<ParseForest>,
    ParseNode   extends IParseNode<ParseForest, Derivation>,
    StackNode   extends IStackNode,
    ParseState  extends AbstractParseState<StackNode>>
//@formatter:on
    implements IParseFailureHandler<ParseForest, StackNode, ParseState> {

    public static
//@formatter:off
   <ParseForest_ extends IParseForest,
    Derivation_  extends IDerivation<ParseForest_>,
    ParseNode_   extends IParseNode<ParseForest_, Derivation_>,
    StackNode_   extends IStackNode,
    ParseState_  extends AbstractParseState<StackNode_>>
//@formatter:on
    ParseFailureHandlerFactory<ParseForest_, Derivation_, ParseNode_, StackNode_, ParseState_>
    factory() {
        return DefaultParseFailureHandler::new;
    }

    ParserObserving<ParseForest, Derivation, ParseNode, StackNode, ParseState> observing;

    DefaultParseFailureHandler(ParserObserving<ParseForest, Derivation, ParseNode, StackNode, ParseState> observing) {
        this.observing = observing;
    }

    public boolean onFailure(ParseState parseState) {
        return false;
    }

    public ParseFailureType failureType(ParseState parseState) {
        return ParseFailureType.Unknown;
    }

}
