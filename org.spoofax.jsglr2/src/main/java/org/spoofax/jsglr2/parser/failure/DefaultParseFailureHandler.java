package org.spoofax.jsglr2.parser.failure;

import org.spoofax.jsglr2.parseforest.IParseForest;
import org.spoofax.jsglr2.parser.AbstractParse;
import org.spoofax.jsglr2.parser.IParseState;
import org.spoofax.jsglr2.parser.result.ParseFailureType;
import org.spoofax.jsglr2.stack.IStackNode;

public class DefaultParseFailureHandler
//@formatter:off
   <ParseForest extends IParseForest,
    StackNode   extends IStackNode,
    ParseState  extends IParseState<ParseForest, StackNode>,
    Parse       extends AbstractParse<ParseForest, StackNode, ParseState>>
//@formatter:on
    implements IParseFailureHandler<ParseForest, StackNode, ParseState, Parse> {

    public void onFailure(Parse parse) {

    }

    public ParseFailureType failureType(Parse parse) {
        return ParseFailureType.Unknown;
    }

}
