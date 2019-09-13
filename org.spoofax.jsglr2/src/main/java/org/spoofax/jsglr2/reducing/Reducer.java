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

public class Reducer
//@formatter:off
   <ParseForest extends IParseForest,
    ParseNode   extends ParseForest,
    Derivation  extends IDerivation<ParseForest>,
    StackNode   extends IStackNode,
    ParseState  extends AbstractParseState<ParseForest, StackNode>>
//@formatter:on
{

    protected final AbstractStackManager<ParseForest, StackNode, ParseState> stackManager;
    protected final ParseForestManager<ParseForest, ParseNode, Derivation, StackNode, ParseState> parseForestManager;

    public Reducer(AbstractStackManager<ParseForest, StackNode, ParseState> stackManager,
        ParseForestManager<ParseForest, ParseNode, Derivation, StackNode, ParseState> parseForestManager) {
        this.stackManager = stackManager;
        this.parseForestManager = parseForestManager;
    }

    /**
     * Performs a reduction when an existing active stack is found with the required goto state and when there is a
     * direct link found between this active stack and the stack from where the reduction started. This means the
     * currently reduced derivation will be added as an alternative to the parse node on the link. This means the parse
     * node is ambiguous.
     */
    public void reducerExistingStackWithDirectLink(ParserObserving<ParseForest, StackNode, ParseState> observing,
        ParseState parseState, IReduce reduce,
        StackLink<ParseForest, StackNode> existingDirectLinkToActiveStateWithGoto, ParseForest[] parseForests) {
        Derivation derivation = parseForestManager.createDerivation(observing, parseState,
            existingDirectLinkToActiveStateWithGoto.to, reduce.production(), reduce.productionType(), parseForests);

        @SuppressWarnings("unchecked") ParseNode parseNode =
            (ParseNode) existingDirectLinkToActiveStateWithGoto.parseForest;

        parseForestManager.addDerivation(observing, parseState, parseNode, derivation);

        if(reduce.isRejectProduction())
            stackManager.rejectStackLink(observing, existingDirectLinkToActiveStateWithGoto);
    }

    /**
     * Performs a reduction when an existing active stack is found with the required goto state but when there is not
     * already a direct link present between this active stack and the stack from where the reduction started. The link
     * between these stacks is created and the currently reduced derivation is added as the first derivation for the
     * parse node on the link.
     */
    public StackLink<ParseForest, StackNode> reducerExistingStackWithoutDirectLink(
        ParserObserving<ParseForest, StackNode, ParseState> observing, ParseState parseState, IReduce reduce,
        StackNode existingActiveStackWithGotoState, StackNode stack, ParseForest[] parseForests) {
        Derivation derivation = parseForestManager.createDerivation(observing, parseState, stack, reduce.production(),
            reduce.productionType(), parseForests);
        ParseForest parseNode =
            parseForestManager.createParseNode(observing, parseState, stack, reduce.production(), derivation);

        StackLink<ParseForest, StackNode> newDirectLinkToActiveStateWithGoto =
            stackManager.createStackLink(observing, parseState, existingActiveStackWithGotoState, stack, parseNode);

        if(reduce.isRejectProduction())
            stackManager.rejectStackLink(observing, newDirectLinkToActiveStateWithGoto);

        return newDirectLinkToActiveStateWithGoto;
    }

    /**
     * Performs a reduction when no active stack is found with the required goto state. A new stack with the required
     * goto state is created and a link between this stack and the stack from where the reduction started is created.
     * The currently reduced derivation is added as the first derivation for the parse node on the link.
     */
    public StackNode reducerNoExistingStack(ParserObserving<ParseForest, StackNode, ParseState> observing,
        ParseState parseState, IReduce reduce, StackNode stack, IState gotoState, ParseForest[] parseForests) {
        Derivation derivation = parseForestManager.createDerivation(observing, parseState, stack, reduce.production(),
            reduce.productionType(), parseForests);
        ParseForest parseNode =
            parseForestManager.createParseNode(observing, parseState, stack, reduce.production(), derivation);

        StackNode newStackWithGotoState =
            stackManager.createStackNode(observing, gotoState);
        StackLink<ParseForest, StackNode> link =
            stackManager.createStackLink(observing, parseState, newStackWithGotoState, stack, parseNode);

        if(reduce.isRejectProduction())
            stackManager.rejectStackLink(observing, link);

        return newStackWithGotoState;
    }

}
