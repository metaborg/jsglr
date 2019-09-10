package org.spoofax.jsglr2.reducing;

import org.metaborg.parsetable.actions.IReduce;
import org.spoofax.jsglr2.parseforest.IParseForest;
import org.spoofax.jsglr2.parser.AbstractParseState;
import org.spoofax.jsglr2.parser.Parse;
import org.spoofax.jsglr2.stack.IStackNode;

public interface ReduceFilter
//@formatter:off
   <ParseForest extends IParseForest,
    StackNode   extends IStackNode,
    ParseState  extends AbstractParseState<ParseForest, StackNode>>
//@formatter:on
{

    boolean ignoreReduce(Parse<ParseForest, StackNode, ParseState> parse, IReduce reduce);

    static
//@formatter:off
   <ParseForest_ extends IParseForest,
    StackNode_   extends IStackNode,
    ParseState_  extends AbstractParseState<ParseForest_, StackNode_>>
//@formatter:on
    ReduceFilter<ParseForest_, StackNode_, ParseState_> ignoreCompletionAndRecovery() {
        return (parse, reduce) -> reduce.production().isCompletionOrRecovery();
    }

}
