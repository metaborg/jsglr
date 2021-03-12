package org.spoofax.jsglr2.recovery;

import org.metaborg.parsetable.actions.IReduce;
import org.spoofax.jsglr2.parseforest.IDerivation;
import org.spoofax.jsglr2.parseforest.IParseForest;
import org.spoofax.jsglr2.parseforest.IParseNode;
import org.spoofax.jsglr2.parser.AbstractParseState;
import org.spoofax.jsglr2.parser.observing.IParserObserver;
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
        if(parseState.isRecovering()) {
            int quota = parseState.recoveryJob().getQuota(activeStack);

            if(reduce.production().isRecovery()) {
                quota--;

                parseState.recoveryJob().updateLastRecoveredOffset(gotoStack, parseState.inputStack.offset());
            } else {
                parseState.recoveryJob().updateLastRecoveredOffset(gotoStack,
                    parseState.recoveryJob().lastRecoveredOffset(activeStack));
            }

            parseState.recoveryJob().updateQuota(gotoStack, quota);
        }
    }

    @Override public void shift(ParseState parseState, StackNode originStack, StackNode gotoStack) {
        if(parseState.isRecovering()) {
            int quota = parseState.recoveryJob().getQuota(originStack);
            int lastRecoveredOffset = parseState.recoveryJob().lastRecoveredOffset(originStack);

            parseState.recoveryJob().updateQuota(gotoStack, quota);
            parseState.recoveryJob().updateLastRecoveredOffset(gotoStack, lastRecoveredOffset);
        }
    }

}
