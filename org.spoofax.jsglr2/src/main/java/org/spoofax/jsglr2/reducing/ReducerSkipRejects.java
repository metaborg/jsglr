package org.spoofax.jsglr2.reducing;

import org.metaborg.parsetable.IState;
import org.metaborg.parsetable.actions.IReduce;
import org.spoofax.jsglr2.parseforest.AbstractParseForest;
import org.spoofax.jsglr2.parseforest.ParseForestManager;
import org.spoofax.jsglr2.parser.Parse;
import org.spoofax.jsglr2.stack.AbstractStackNode;
import org.spoofax.jsglr2.stack.StackLink;
import org.spoofax.jsglr2.stack.StackManager;

public class ReducerSkipRejects<StackNode extends AbstractStackNode<ParseForest>, ParseForest extends AbstractParseForest, ParseNode extends ParseForest, Derivation>
    extends Reducer<ParseForest, ParseNode, Derivation, StackNode> {

    public ReducerSkipRejects(StackManager<ParseForest, StackNode> stackManager,
        ParseForestManager<ParseForest, ParseNode, Derivation> parseForestManager) {
        super(stackManager, parseForestManager);
    }

    @Override
    public void reducerExistingStackWithDirectLink(Parse<ParseForest, StackNode> parse, IReduce reduce,
        StackLink<ParseForest, StackNode> existingDirectLinkToActiveStateWithGoto, ParseForest[] parseForests) {
        @SuppressWarnings("unchecked") ParseNode parseNode =
            (ParseNode) existingDirectLinkToActiveStateWithGoto.parseForest;

        if(reduce.isRejectProduction())
            stackManager.rejectStackLink(parse, existingDirectLinkToActiveStateWithGoto);
        else if(!existingDirectLinkToActiveStateWithGoto.isRejected()) {
            Derivation derivation =
                parseForestManager.createDerivation(parse, existingDirectLinkToActiveStateWithGoto.to.position,
                    reduce.production(), reduce.productionType(), parseForests);

            parseForestManager.addDerivation(parse, parseNode, derivation);
        }
    }

    @Override
    public StackLink<ParseForest, StackNode> reducerExistingStackWithoutDirectLink(Parse<ParseForest, StackNode> parse,
        IReduce reduce, StackNode existingActiveStackWithGotoState, StackNode stack, ParseForest[] parseForests) {
        StackLink<ParseForest, StackNode> newDirectLinkToActiveStateWithGoto;

        if(reduce.isRejectProduction()) {
            newDirectLinkToActiveStateWithGoto =
                stackManager.createStackLink(parse, existingActiveStackWithGotoState, stack, null);

            stackManager.rejectStackLink(parse, newDirectLinkToActiveStateWithGoto);
        } else {
            Derivation derivation = parseForestManager.createDerivation(parse, stack.position, reduce.production(),
                reduce.productionType(), parseForests);
            ParseForest parseNode =
                parseForestManager.createParseNode(parse, stack.position, reduce.production(), derivation);

            newDirectLinkToActiveStateWithGoto =
                stackManager.createStackLink(parse, existingActiveStackWithGotoState, stack, parseNode);
        }

        return newDirectLinkToActiveStateWithGoto;
    }

    @Override
    public StackNode reducerNoExistingStack(Parse<ParseForest, StackNode> parse, IReduce reduce, StackNode stack,
        IState gotoState, ParseForest[] parseForests) {
        StackNode newStackWithGotoState = stackManager.createStackNode(parse, gotoState);

        StackLink<ParseForest, StackNode> link;

        if(reduce.isRejectProduction()) {
            link = stackManager.createStackLink(parse, newStackWithGotoState, stack, null);

            stackManager.rejectStackLink(parse, link);
        } else {
            Derivation derivation = parseForestManager.createDerivation(parse, stack.position, reduce.production(),
                reduce.productionType(), parseForests);
            ParseForest parseNode =
                parseForestManager.createParseNode(parse, stack.position, reduce.production(), derivation);

            link = stackManager.createStackLink(parse, newStackWithGotoState, stack, parseNode);
        }

        return newStackWithGotoState;
    }

}
