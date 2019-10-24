package org.spoofax.jsglr2.recovery;

import org.spoofax.jsglr2.parseforest.IParseForest;
import org.spoofax.jsglr2.parser.AbstractParseState;

import org.spoofax.jsglr2.parser.failure.IParseFailureHandler;
import org.spoofax.jsglr2.parser.result.ParseFailureType;
import org.spoofax.jsglr2.stack.IStackNode;

public class RecoveryParseFailureHandler
//@formatter:off
   <ParseForest extends IParseForest,
    StackNode   extends IStackNode,
    ParseState  extends AbstractParseState<ParseForest, StackNode> & IRecoveryParseState<ParseForest, StackNode>>
//@formatter:on
    implements IParseFailureHandler<ParseForest, StackNode, ParseState> {

    @Override public boolean onFailure(ParseState parseState) {
        if(!parseState.isRecovering())
            parseState.startRecovery(parseState.currentOffset);

        return parseState.nextRecoveryIteration();
    }

    @Override public ParseFailureType failureType(ParseState parseState) {
        return ParseFailureType.Unknown;
    }

}
