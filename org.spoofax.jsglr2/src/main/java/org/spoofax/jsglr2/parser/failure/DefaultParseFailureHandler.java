package org.spoofax.jsglr2.parser.failure;

import org.spoofax.jsglr2.parseforest.IParseForest;
import org.spoofax.jsglr2.parser.AbstractParseState;
import org.spoofax.jsglr2.parser.Parse;
import org.spoofax.jsglr2.parser.result.ParseFailureType;
import org.spoofax.jsglr2.stack.IStackNode;

public class DefaultParseFailureHandler
//@formatter:off
   <ParseForest extends IParseForest,
    StackNode   extends IStackNode,
    ParseState  extends AbstractParseState<ParseForest, StackNode>>
//@formatter:on
    implements IParseFailureHandler<ParseForest, StackNode, ParseState> {

    public boolean onFailure(Parse<ParseForest, StackNode, ParseState> parse) {
        return false;
    }

    public ParseFailureType failureType(Parse parse) {
        return ParseFailureType.Unknown;
    }

}
