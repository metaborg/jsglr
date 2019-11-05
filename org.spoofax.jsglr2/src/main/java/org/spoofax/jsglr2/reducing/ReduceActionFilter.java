package org.spoofax.jsglr2.reducing;

import org.metaborg.parsetable.actions.IReduce;
import org.spoofax.jsglr2.parseforest.IParseForest;
import org.spoofax.jsglr2.parser.AbstractParseState;
import org.spoofax.jsglr2.stack.IStackNode;

public interface ReduceActionFilter
//@formatter:off
   <ParseForest extends IParseForest,
    StackNode   extends IStackNode,
    ParseState  extends AbstractParseState<ParseForest, StackNode>>
//@formatter:on
{

    boolean ignoreReduce(ParseState parseState, IReduce reduce);

    static
//@formatter:off
   <ParseForest_ extends IParseForest,
    StackNode_   extends IStackNode,
    ParseState_  extends AbstractParseState<ParseForest_, StackNode_>>
//@formatter:on
    ReduceActionFilter<ParseForest_, StackNode_, ParseState_> ignoreRecoveryAndCompletion() {
        return (parseState, reduce) -> reduce.production().isRecovery() || reduce.production().isCompletion();
    }

}
