package org.spoofax.jsglr2.parser.failure;

import org.spoofax.jsglr2.parseforest.IParseForest;
import org.spoofax.jsglr2.parser.AbstractParseState;
import org.spoofax.jsglr2.parser.result.ParseFailureType;
import org.spoofax.jsglr2.stack.IStackNode;

public interface IParseFailureHandler
//@formatter:off
   <ParseForest extends IParseForest,
    StackNode   extends IStackNode,
    ParseState  extends AbstractParseState<?, StackNode>>
//@formatter:on
{

    boolean onFailure(ParseState parseState);

    default ParseFailureType failureType(ParseState parseState) {
        if(parseState.inputStack.offset() == parseState.inputStack.length() - 1)
            return ParseFailureType.UnexpectedEOF;
        else
            return ParseFailureType.UnexpectedInput;
    }

}
