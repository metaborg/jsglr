package org.spoofax.jsglr2.reducing;

import org.metaborg.parsetable.states.IState;
import org.metaborg.parsetable.actions.IReduce;
import org.spoofax.jsglr2.parseforest.IDerivation;
import org.spoofax.jsglr2.parseforest.IParseForest;
import org.spoofax.jsglr2.parseforest.ParseForestManager;
import org.spoofax.jsglr2.parser.AbstractParse;
import org.spoofax.jsglr2.stack.AbstractStackManager;
import org.spoofax.jsglr2.stack.IStackNode;
import org.spoofax.jsglr2.stack.StackLink;

public class ReducerSkipRejects
//@formatter:off
   <ParseForest extends IParseForest,
    ParseNode   extends ParseForest,
    Derivation  extends IDerivation<ParseForest>,
    StackNode   extends IStackNode,
    Parse       extends AbstractParse<ParseForest, StackNode>>
//@formatter:on
    extends Reducer<ParseForest, ParseNode, Derivation, StackNode, Parse> {

    public ReducerSkipRejects(AbstractStackManager<ParseForest, StackNode, Parse> stackManager,
        ParseForestManager<ParseForest, ParseNode, Derivation, Parse> parseForestManager) {
        super(stackManager, parseForestManager);
    }

    @Override public void reducerExistingStackWithDirectLink(Parse parse, IReduce reduce,
        StackLink<ParseForest, StackNode> existingDirectLinkToActiveStateWithGoto, ParseForest[] parseForests) {
        @SuppressWarnings("unchecked") ParseNode parseNode =
            (ParseNode) existingDirectLinkToActiveStateWithGoto.parseForest;

        if(reduce.isRejectProduction())
            stackManager.rejectStackLink(parse, existingDirectLinkToActiveStateWithGoto);
        else if(!existingDirectLinkToActiveStateWithGoto.isRejected()) {
            Derivation derivation = parseForestManager.createDerivation(parse,
                existingDirectLinkToActiveStateWithGoto.to, reduce.production(), reduce.productionType(), parseForests);

            parseForestManager.addDerivation(parse, parseNode, derivation);
        }
    }

    @Override public StackLink<ParseForest, StackNode> reducerExistingStackWithoutDirectLink(Parse parse,
        IReduce reduce, StackNode existingActiveStackWithGotoState, StackNode stack, ParseForest[] parseForests) {
        StackLink<ParseForest, StackNode> newDirectLinkToActiveStateWithGoto;

        if(reduce.isRejectProduction()) {
            newDirectLinkToActiveStateWithGoto =
                stackManager.createStackLink(parse, existingActiveStackWithGotoState, stack, null);

            stackManager.rejectStackLink(parse, newDirectLinkToActiveStateWithGoto);
        } else {
            Derivation derivation = parseForestManager.createDerivation(parse, stack, reduce.production(),
                reduce.productionType(), parseForests);
            ParseForest parseNode = parseForestManager.createParseNode(parse, stack, reduce.production(), derivation);

            newDirectLinkToActiveStateWithGoto =
                stackManager.createStackLink(parse, existingActiveStackWithGotoState, stack, parseNode);
        }

        return newDirectLinkToActiveStateWithGoto;
    }

    @Override public StackNode reducerNoExistingStack(Parse parse, IReduce reduce, StackNode stack, IState gotoState,
        ParseForest[] parseForests) {
        StackNode newStackWithGotoState = stackManager.createStackNode(parse, gotoState);

        StackLink<ParseForest, StackNode> link;

        if(reduce.isRejectProduction()) {
            link = stackManager.createStackLink(parse, newStackWithGotoState, stack, null);

            stackManager.rejectStackLink(parse, link);
        } else {
            Derivation derivation = parseForestManager.createDerivation(parse, stack, reduce.production(),
                reduce.productionType(), parseForests);
            ParseForest parseNode = parseForestManager.createParseNode(parse, stack, reduce.production(), derivation);

            link = stackManager.createStackLink(parse, newStackWithGotoState, stack, parseNode);
        }

        return newStackWithGotoState;
    }

}
