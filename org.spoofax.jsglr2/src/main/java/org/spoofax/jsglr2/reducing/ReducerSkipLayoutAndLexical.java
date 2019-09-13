package org.spoofax.jsglr2.reducing;

import org.metaborg.parsetable.actions.IReduce;
import org.metaborg.parsetable.states.IState;
import org.spoofax.jsglr2.parseforest.IDerivation;
import org.spoofax.jsglr2.parseforest.IParseForest;
import org.spoofax.jsglr2.parseforest.ParseForestManager;
import org.spoofax.jsglr2.parser.AbstractParseState;

import org.spoofax.jsglr2.parser.observing.ParserObserving;
import org.spoofax.jsglr2.stack.AbstractStackManager;
import org.spoofax.jsglr2.stack.IStackNode;
import org.spoofax.jsglr2.stack.StackLink;

public class ReducerSkipLayoutAndLexical
//@formatter:off
   <StackNode   extends IStackNode,
    ParseForest extends IParseForest,
    ParseNode   extends ParseForest,
    Derivation  extends IDerivation<ParseForest>,
    ParseState  extends AbstractParseState<ParseForest, StackNode>>
//@formatter:on
    extends Reducer<ParseForest, ParseNode, Derivation, StackNode, ParseState> {

    public ReducerSkipLayoutAndLexical(AbstractStackManager<ParseForest, StackNode, ParseState> stackManager,
        ParseForestManager<ParseForest, ParseNode, Derivation, StackNode, ParseState> parseForestManager) {
        super(stackManager, parseForestManager);
    }

    @Override public void reducerExistingStackWithDirectLink(
        ParserObserving<ParseForest, StackNode, ParseState> observing, ParseState parseState, IReduce reduce,
        StackLink<ParseForest, StackNode> existingDirectLinkToActiveStateWithGoto, ParseForest[] parseForests) {
        @SuppressWarnings("unchecked") ParseNode parseNode =
            (ParseNode) existingDirectLinkToActiveStateWithGoto.parseForest;

        if(parseNode != null) {
            Derivation derivation = parseForestManager.createDerivation(observing, parseState,
                existingDirectLinkToActiveStateWithGoto.to, reduce.production(), reduce.productionType(), parseForests);
            parseForestManager.addDerivation(observing, parseState, parseNode, derivation);
        }

        if(reduce.isRejectProduction())
            stackManager.rejectStackLink(observing, existingDirectLinkToActiveStateWithGoto);
    }

    @Override public StackLink<ParseForest, StackNode> reducerExistingStackWithoutDirectLink(
        ParserObserving<ParseForest, StackNode, ParseState> observing, ParseState parseState, IReduce reduce,
        StackNode existingActiveStackWithGotoState, StackNode stack, ParseForest[] parseForests) {
        ParseNode parseNode;

        if(reduce.production().isSkippableInParseForest())
            parseNode = null;
        else {
            Derivation derivation = parseForestManager.createDerivation(observing, parseState, stack,
                reduce.production(), reduce.productionType(), parseForests);
            parseNode =
                parseForestManager.createParseNode(observing, parseState, stack, reduce.production(), derivation);
        }

        StackLink<ParseForest, StackNode> newDirectLinkToActiveStateWithGoto =
            stackManager.createStackLink(observing, parseState, existingActiveStackWithGotoState, stack, parseNode);

        if(reduce.isRejectProduction())
            stackManager.rejectStackLink(observing, newDirectLinkToActiveStateWithGoto);

        return newDirectLinkToActiveStateWithGoto;
    }

    @Override public StackNode reducerNoExistingStack(ParserObserving<ParseForest, StackNode, ParseState> observing,
        ParseState parseState, IReduce reduce, StackNode stack, IState gotoState, ParseForest[] parseForests) {
        ParseNode parseNode;

        if(reduce.production().isSkippableInParseForest())
            parseNode = null;
        else {
            Derivation derivation = parseForestManager.createDerivation(observing, parseState, stack,
                reduce.production(), reduce.productionType(), parseForests);
            parseNode =
                parseForestManager.createParseNode(observing, parseState, stack, reduce.production(), derivation);
        }

        StackNode newStackWithGotoState =
            stackManager.createStackNode(observing, gotoState);
        StackLink<ParseForest, StackNode> link =
            stackManager.createStackLink(observing, parseState, newStackWithGotoState, stack, parseNode);

        if(reduce.isRejectProduction())
            stackManager.rejectStackLink(observing, link);

        return newStackWithGotoState;
    }

}
