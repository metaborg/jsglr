package org.spoofax.jsglr2.reducing;

import org.metaborg.parsetable.actions.IReduce;
import org.metaborg.parsetable.states.IState;
import org.spoofax.jsglr2.parseforest.IDerivation;
import org.spoofax.jsglr2.parseforest.IParseForest;
import org.spoofax.jsglr2.parseforest.IParseNode;
import org.spoofax.jsglr2.parseforest.ParseForestManager;
import org.spoofax.jsglr2.parser.AbstractParseState;
import org.spoofax.jsglr2.parser.observing.ParserObserving;
import org.spoofax.jsglr2.stack.AbstractStackManager;
import org.spoofax.jsglr2.stack.IStackNode;
import org.spoofax.jsglr2.stack.StackLink;

public class ReducerSkipLayoutAndLexicalAndRejects
//@formatter:off
   <ParseForest extends IParseForest,
    Derivation  extends IDerivation<ParseForest>,
    ParseNode   extends IParseNode<ParseForest, Derivation>,
    StackNode   extends IStackNode,
    ParseState  extends AbstractParseState<StackNode>>
//@formatter:on
    extends Reducer<ParseForest, Derivation, ParseNode, StackNode, ParseState> {

    public ReducerSkipLayoutAndLexicalAndRejects(
        AbstractStackManager<ParseForest, Derivation, ParseNode, StackNode, ParseState> stackManager,
        ParseForestManager<ParseForest, Derivation, ParseNode, StackNode, ParseState> parseForestManager) {
        super(stackManager, parseForestManager);
    }

    @Override public void reducerExistingStackWithDirectLink(
        ParserObserving<ParseForest, Derivation, ParseNode, StackNode, ParseState> observing, ParseState parseState,
        IReduce reduce, StackLink<ParseForest, StackNode> existingDirectLinkToActiveStateWithGoto,
        ParseForest[] parseForests) {
        @SuppressWarnings("unchecked") ParseNode parseNode =
            (ParseNode) existingDirectLinkToActiveStateWithGoto.parseForest;

        if(reduce.isRejectProduction())
            stackManager.rejectStackLink(existingDirectLinkToActiveStateWithGoto);
        else if(!existingDirectLinkToActiveStateWithGoto.isRejected() && parseNode != null) {
            Derivation derivation = parseForestManager.createDerivation(parseState,
                existingDirectLinkToActiveStateWithGoto.to, reduce.production(), reduce.productionType(), parseForests);
            parseForestManager.addDerivation(parseState, parseNode, derivation);
        }
    }

    @Override public StackLink<ParseForest, StackNode> reducerExistingStackWithoutDirectLink(
        ParserObserving<ParseForest, Derivation, ParseNode, StackNode, ParseState> observing, ParseState parseState,
        IReduce reduce, StackNode existingActiveStackWithGotoState, StackNode stack, ParseForest[] parseForests) {
        StackLink<ParseForest, StackNode> newDirectLinkToActiveStateWithGoto;

        if(reduce.isRejectProduction()) {
            newDirectLinkToActiveStateWithGoto =
                stackManager.createStackLink(parseState, existingActiveStackWithGotoState, stack, null);

            stackManager.rejectStackLink(newDirectLinkToActiveStateWithGoto);
        } else {
            ParseNode parseNode;

            if(reduce.production().isSkippableInParseForest())
                parseNode = null;
            else {
                Derivation derivation = parseForestManager.createDerivation(parseState, stack, reduce.production(),
                    reduce.productionType(), parseForests);
                parseNode = parseForestManager.createParseNode(parseState, stack, reduce.production(), derivation);
            }

            newDirectLinkToActiveStateWithGoto = stackManager.createStackLink(parseState,
                existingActiveStackWithGotoState, stack, (ParseForest) parseNode);
        }

        return newDirectLinkToActiveStateWithGoto;
    }

    @Override public StackNode reducerNoExistingStack(
        ParserObserving<ParseForest, Derivation, ParseNode, StackNode, ParseState> observing, ParseState parseState,
        IReduce reduce, StackNode stack, IState gotoState, ParseForest[] parseForests) {
        StackNode newStackWithGotoState = stackManager.createStackNode(gotoState);

        StackLink<ParseForest, StackNode> link;

        if(reduce.isRejectProduction()) {
            link = stackManager.createStackLink(parseState, newStackWithGotoState, stack, null);

            stackManager.rejectStackLink(link);
        } else {
            ParseNode parseNode;

            if(reduce.production().isSkippableInParseForest())
                parseNode = null;
            else {
                Derivation derivation = parseForestManager.createDerivation(parseState, stack, reduce.production(),
                    reduce.productionType(), parseForests);
                parseNode = parseForestManager.createParseNode(parseState, stack, reduce.production(), derivation);
            }

            link = stackManager.createStackLink(parseState, newStackWithGotoState, stack, (ParseForest) parseNode);
        }

        return newStackWithGotoState;
    }

}
