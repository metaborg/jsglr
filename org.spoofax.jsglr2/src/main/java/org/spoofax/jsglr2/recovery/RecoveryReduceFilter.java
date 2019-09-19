package org.spoofax.jsglr2.recovery;

import org.metaborg.parsetable.actions.IReduce;
import org.spoofax.jsglr2.parseforest.IParseForest;
import org.spoofax.jsglr2.parser.AbstractParseState;

import org.spoofax.jsglr2.reducing.ReduceFilter;
import org.spoofax.jsglr2.stack.IStackNode;

public class RecoveryReduceFilter
//@formatter:off
   <ParseForest extends IParseForest,
    StackNode   extends IStackNode,
    ParseState  extends AbstractParseState<ParseForest, StackNode> & IRecoveryParseState<ParseForest, StackNode>>
//@formatter:on
    implements ReduceFilter<ParseForest, StackNode, ParseState> {

    @Override public boolean ignoreReduce(ParseState parseState, IReduce reduce) {
        if(reduce.production().isCompletion())
            return true;

        if(reduce.production().isRecovery()) {
            if(parseState.isRecovering()) {
                if(parseState.recoveryJob().quota > 0) {
                    parseState.recoveryJob().quota--;

                    return false;
                } else
                    return true;
            } else
                return true;
        } else
            return false;
    }

}
