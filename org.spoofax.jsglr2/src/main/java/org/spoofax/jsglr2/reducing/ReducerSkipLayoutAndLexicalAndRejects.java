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

public class ReducerSkipLayoutAndLexicalAndRejects
//@formatter:off
   <ParseForest extends IParseForest,
    ParseNode   extends ParseForest,
    Derivation  extends IDerivation<ParseForest>,
    StackNode   extends IStackNode,
    ParseState  extends AbstractParseState<ParseForest, StackNode>>
//@formatter:on
    extends Reducer<ParseForest, ParseNode, Derivation, StackNode, ParseState> {

    public ReducerSkipLayoutAndLexicalAndRejects(AbstractStackManager<ParseForest, StackNode, ParseState> stackManager,
        ParseForestManager<ParseForest, ParseNode, Derivation, StackNode, ParseState> parseForestManager) {
        super(stackManager, parseForestManager);
    }

    @Override public void reducerExistingStackWithDirectLink(
        ParserObserving<ParseForest, StackNode, ParseState> observing, ParseState parseState, IReduce reduce,
        StackLink<ParseForest, StackNode> existingDirectLinkToActiveStateWithGoto, ParseForest[] parseForests) {
        @SuppressWarnings("unchecked") ParseNode parseNode =
            (ParseNode) existingDirectLinkToActiveStateWithGoto.parseForest;

        if(reduce.isRejectProduction())
            stackManager.rejectStackLink(observing, existingDirectLinkToActiveStateWithGoto);
        else if(!existingDirectLinkToActiveStateWithGoto.isRejected() && parseNode != null) {
            Derivation derivation = parseForestManager.createDerivation(observing, parseState,
                existingDirectLinkToActiveStateWithGoto.to, reduce.production(), reduce.productionType(), parseForests);
            parseForestManager.addDerivation(observing, parseState, parseNode, derivation);
        }
    }

    @Override public StackLink<ParseForest, StackNode> reducerExistingStackWithoutDirectLink(
        ParserObserving<ParseForest, StackNode, ParseState> observing, ParseState parseState, IReduce reduce,
        StackNode existingActiveStackWithGotoState, StackNode stack, ParseForest[] parseForests) {
        StackLink<ParseForest, StackNode> newDirectLinkToActiveStateWithGoto;

        if(reduce.isRejectProduction()) {
            newDirectLinkToActiveStateWithGoto =
                stackManager.createStackLink(observing, parseState, existingActiveStackWithGotoState, stack, null);

            stackManager.rejectStackLink(observing, newDirectLinkToActiveStateWithGoto);
        } else {
            ParseNode parseNode;

            if(reduce.production().isSkippableInParseForest())
                parseNode = null;
            else {
                Derivation derivation = parseForestManager.createDerivation(observing, parseState, stack,
                    reduce.production(), reduce.productionType(), parseForests);
                parseNode =
                    parseForestManager.createParseNode(observing, parseState, stack, reduce.production(), derivation);
            }

            newDirectLinkToActiveStateWithGoto =
                stackManager.createStackLink(observing, parseState, existingActiveStackWithGotoState, stack, parseNode);
        }

        return newDirectLinkToActiveStateWithGoto;
    }

    @Override public StackNode reducerNoExistingStack(ParserObserving<ParseForest, StackNode, ParseState> observing,
        ParseState parseState, IReduce reduce, StackNode stack, IState gotoState, ParseForest[] parseForests) {
        StackNode newStackWithGotoState =
            stackManager.createStackNode(observing, gotoState);

        StackLink<ParseForest, StackNode> link;

        if(reduce.isRejectProduction()) {
            link = stackManager.createStackLink(observing, parseState, newStackWithGotoState, stack, null);

            stackManager.rejectStackLink(observing, link);
        } else {
            ParseNode parseNode;

            if(reduce.production().isSkippableInParseForest())
                parseNode = null;
            else {
                Derivation derivation = parseForestManager.createDerivation(observing, parseState, stack,
                    reduce.production(), reduce.productionType(), parseForests);
                parseNode =
                    parseForestManager.createParseNode(observing, parseState, stack, reduce.production(), derivation);
            }

            link = stackManager.createStackLink(observing, parseState, newStackWithGotoState, stack, parseNode);
        }

        return newStackWithGotoState;
    }

}
