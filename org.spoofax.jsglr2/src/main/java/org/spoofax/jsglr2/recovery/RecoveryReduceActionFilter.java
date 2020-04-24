package org.spoofax.jsglr2.recovery;

import org.metaborg.parsetable.actions.IReduce;
import org.spoofax.jsglr2.parseforest.IParseForest;
import org.spoofax.jsglr2.parser.AbstractParseState;
import org.spoofax.jsglr2.reducing.ReduceActionFilter;
import org.spoofax.jsglr2.stack.IStackNode;

public class RecoveryReduceActionFilter
//@formatter:off
   <ParseForest          extends IParseForest,
    StackNode            extends IStackNode,
    BacktrackChoicePoint extends IBacktrackChoicePoint<?, StackNode>,
    ParseState           extends AbstractParseState<?, StackNode> & IRecoveryParseState<?, StackNode, BacktrackChoicePoint>>
//@formatter:on
    implements ReduceActionFilter<ParseForest, StackNode, ParseState> {

    @Override public boolean ignoreReduce(ParseState parseState, StackNode stack, IReduce reduce) {
        if(reduce.production().isCompletion())
            return true;

        if(!reduce.production().isRecovery())
            return false; // Regular productions can always be used
        if(!parseState.isRecovering())
            return true; // Ignore recovery productions outside recovery mode
        if(parseState.recoveryJob().getQuota(stack) <= 0)
            return true; // Ignore recovery productions after quota exceeded

        return false;
    }

}
