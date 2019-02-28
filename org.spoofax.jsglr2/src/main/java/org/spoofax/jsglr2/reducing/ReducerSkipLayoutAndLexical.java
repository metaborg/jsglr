package org.spoofax.jsglr2.reducing;

import org.metaborg.parsetable.IState;
import org.metaborg.parsetable.actions.IReduce;
import org.spoofax.jsglr2.parseforest.AbstractParseForest;
import org.spoofax.jsglr2.parseforest.IDerivation;
import org.spoofax.jsglr2.parseforest.ParseForestManager;
import org.spoofax.jsglr2.parser.AbstractParse;
import org.spoofax.jsglr2.stack.AbstractStackManager;
import org.spoofax.jsglr2.stack.AbstractStackNode;
import org.spoofax.jsglr2.stack.StackLink;

public class ReducerSkipLayoutAndLexical
//@formatter:off
   <StackNode   extends AbstractStackNode<ParseForest>,
    ParseForest extends AbstractParseForest,
    ParseNode   extends ParseForest,
    Derivation  extends IDerivation<ParseForest>>
//@formatter:on
    extends Reducer<ParseForest, ParseNode, Derivation, StackNode> {

    public ReducerSkipLayoutAndLexical(AbstractStackManager<ParseForest, StackNode> stackManager,
        ParseForestManager<ParseForest, ParseNode, Derivation> parseForestManager) {
        super(stackManager, parseForestManager);
    }

    @Override public void reducerExistingStackWithDirectLink(AbstractParse<ParseForest, StackNode> parse,
        IReduce reduce, StackLink<ParseForest, StackNode> existingDirectLinkToActiveStateWithGoto,
        ParseForest[] parseForests) {
        @SuppressWarnings("unchecked") ParseNode parseNode =
            (ParseNode) existingDirectLinkToActiveStateWithGoto.parseForest;

        if(parseNode != null) {
            Derivation derivation =
                parseForestManager.createDerivation(parse, existingDirectLinkToActiveStateWithGoto.to.position,
                    reduce.production(), reduce.productionType(), parseForests);
            parseForestManager.addDerivation(parse, parseNode, derivation);
        }

        if(reduce.isRejectProduction())
            stackManager.rejectStackLink(parse, existingDirectLinkToActiveStateWithGoto);
    }

    @Override public StackLink<ParseForest, StackNode> reducerExistingStackWithoutDirectLink(
        AbstractParse<ParseForest, StackNode> parse, IReduce reduce, StackNode existingActiveStackWithGotoState,
        StackNode stack, ParseForest[] parseForests) {
        ParseNode parseNode;

        if(reduce.production().isSkippableInParseForest())
            parseNode = null;
        else {
            Derivation derivation = parseForestManager.createDerivation(parse, stack.position, reduce.production(),
                reduce.productionType(), parseForests);
            parseNode = parseForestManager.createParseNode(parse, stack.position, reduce.production(), derivation);
        }

        StackLink<ParseForest, StackNode> newDirectLinkToActiveStateWithGoto =
            stackManager.createStackLink(parse, existingActiveStackWithGotoState, stack, parseNode);

        if(reduce.isRejectProduction())
            stackManager.rejectStackLink(parse, newDirectLinkToActiveStateWithGoto);

        return newDirectLinkToActiveStateWithGoto;
    }

    @Override public StackNode reducerNoExistingStack(AbstractParse<ParseForest, StackNode> parse, IReduce reduce,
        StackNode stack, IState gotoState, ParseForest[] parseForests) {
        ParseNode parseNode;

        if(reduce.production().isSkippableInParseForest())
            parseNode = null;
        else {
            Derivation derivation = parseForestManager.createDerivation(parse, stack.position, reduce.production(),
                reduce.productionType(), parseForests);
            parseNode = parseForestManager.createParseNode(parse, stack.position, reduce.production(), derivation);
        }

        StackNode newStackWithGotoState = stackManager.createStackNode(parse, gotoState);
        StackLink<ParseForest, StackNode> link =
            stackManager.createStackLink(parse, newStackWithGotoState, stack, parseNode);

        if(reduce.isRejectProduction())
            stackManager.rejectStackLink(parse, link);

        return newStackWithGotoState;
    }

}
