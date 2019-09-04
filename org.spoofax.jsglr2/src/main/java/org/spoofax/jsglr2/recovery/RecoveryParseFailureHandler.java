package org.spoofax.jsglr2.recovery;

import org.spoofax.jsglr2.parseforest.IParseForest;
import org.spoofax.jsglr2.parser.AbstractParse;
import org.spoofax.jsglr2.parser.IParseState;
import org.spoofax.jsglr2.parser.failure.IParseFailureHandler;
import org.spoofax.jsglr2.parser.result.ParseFailureType;
import org.spoofax.jsglr2.stack.IStackNode;

public class RecoveryParseFailureHandler
//@formatter:off
   <ParseForest extends IParseForest,
    StackNode   extends IStackNode,
    ParseState  extends IParseState<ParseForest, StackNode> & IRecoveryState<ParseForest, StackNode>,
    Parse       extends AbstractParse<ParseForest, StackNode, ParseState>>
//@formatter:on
    implements IParseFailureHandler<ParseForest, StackNode, ParseState, Parse> {

    @Override public void onFailure(Parse parse) {

    }

    @Override public ParseFailureType failureType(AbstractParse parse) {
        return ParseFailureType.Unknown;
    }

}
