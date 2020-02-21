package org.spoofax.jsglr2.recovery;

import org.metaborg.parsetable.actions.IReduce;
import org.spoofax.jsglr2.parseforest.IDerivation;
import org.spoofax.jsglr2.parseforest.IParseForest;
import org.spoofax.jsglr2.parseforest.IParseNode;
import org.spoofax.jsglr2.parser.AbstractParseState;
import org.spoofax.jsglr2.parser.observing.IParserObserver;
import org.spoofax.jsglr2.reducing.ReduceActionFilter;
import org.spoofax.jsglr2.stack.IStackNode;

public class RecoveryObserver
//@formatter:off
   <ParseForest extends IParseForest,
    Derivation  extends IDerivation<ParseForest>,
    ParseNode   extends IParseNode<ParseForest, Derivation>,
    StackNode            extends IStackNode,
    BacktrackChoicePoint extends IBacktrackChoicePoint<?, StackNode>,
    ParseState           extends AbstractParseState<?, StackNode> & IRecoveryParseState<?, StackNode, BacktrackChoicePoint>>
//@formatter:on
    implements IParserObserver<ParseForest, Derivation, ParseNode, StackNode, ParseState> {

    @Override public void reducer(ParseState parseState, StackNode activeStack, StackNode originStack, IReduce reduce,
        ParseForest[] parseNodes, StackNode gotoStack) {
        if(reduce.production().isRecovery()) {
            int quota = parseState.recoveryJob().getQuota(activeStack);

            parseState.recoveryJob().updateQuota(gotoStack, quota - 1);
        }
    }

}
