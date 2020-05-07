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

public class Reducer
//@formatter:off
   <ParseForest extends IParseForest,
    Derivation  extends IDerivation<ParseForest>,
    ParseNode   extends IParseNode<ParseForest, Derivation>,
    StackNode   extends IStackNode,
    ParseState  extends AbstractParseState<?, StackNode>>
//@formatter:on
{

    protected final AbstractStackManager<ParseForest, Derivation, ParseNode, StackNode, ParseState> stackManager;
    protected final ParseForestManager<ParseForest, Derivation, ParseNode, StackNode, ParseState> parseForestManager;

    public Reducer(AbstractStackManager<ParseForest, Derivation, ParseNode, StackNode, ParseState> stackManager,
        ParseForestManager<ParseForest, Derivation, ParseNode, StackNode, ParseState> parseForestManager) {
        this.stackManager = stackManager;
        this.parseForestManager = parseForestManager;
    }

    public static
    //@formatter:off
       <ParseForest_ extends IParseForest,
        Derivation_  extends IDerivation<ParseForest_>,
        ParseNode_   extends IParseNode<ParseForest_, Derivation_>,
        StackNode_   extends IStackNode,
        ParseState_  extends AbstractParseState<?, StackNode_>>
    //@formatter:on
    ReducerFactory<ParseForest_, Derivation_, ParseNode_, StackNode_, ParseState_> factory() {
        return Reducer::new;
    }

    /**
     * Performs a reduction when an existing active stack is found with the required goto state and when there is a
     * direct link found between this active stack and the stack from where the reduction started. This means the
     * currently reduced derivation will be added as an alternative to the parse node on the link. This means the parse
     * node is ambiguous.
     */
    public void reducerExistingStackWithDirectLink(
        ParserObserving<ParseForest, Derivation, ParseNode, StackNode, ParseState> observing, ParseState parseState,
        IReduce reduce, StackLink<ParseForest, StackNode> existingDirectLinkToActiveStateWithGoto,
        ParseForest[] parseForests) {
        Derivation derivation = parseForestManager.createDerivation(parseState,
            existingDirectLinkToActiveStateWithGoto.to, reduce.production(), reduce.productionType(), parseForests);

        @SuppressWarnings("unchecked") ParseNode parseNode =
            (ParseNode) existingDirectLinkToActiveStateWithGoto.parseForest;

        parseForestManager.addDerivation(parseState, parseNode, derivation);

        if(reduce.isRejectProduction())
            stackManager.rejectStackLink(existingDirectLinkToActiveStateWithGoto);
    }

    /**
     * Performs a reduction when an existing active stack is found with the required goto state but when there is not
     * already a direct link present between this active stack and the stack from where the reduction started. The link
     * between these stacks is created and the currently reduced derivation is added as the first derivation for the
     * parse node on the link.
     */
    public StackLink<ParseForest, StackNode> reducerExistingStackWithoutDirectLink(
        ParserObserving<ParseForest, Derivation, ParseNode, StackNode, ParseState> observing, ParseState parseState,
        IReduce reduce, StackNode existingActiveStackWithGotoState, StackNode stack, ParseForest[] parseForests) {
        Derivation derivation = parseForestManager.createDerivation(parseState, stack, reduce.production(),
            reduce.productionType(), parseForests);
        ParseNode parseNode = parseForestManager.createParseNode(parseState, stack, reduce.production(), derivation);

        StackLink<ParseForest, StackNode> newDirectLinkToActiveStateWithGoto =
            stackManager.createStackLink(parseState, existingActiveStackWithGotoState, stack, (ParseForest) parseNode);

        if(reduce.isRejectProduction())
            stackManager.rejectStackLink(newDirectLinkToActiveStateWithGoto);

        return newDirectLinkToActiveStateWithGoto;
    }

    /**
     * Performs a reduction when no active stack is found with the required goto state. A new stack with the required
     * goto state is created and a link between this stack and the stack from where the reduction started is created.
     * The currently reduced derivation is added as the first derivation for the parse node on the link.
     */
    public StackNode reducerNoExistingStack(
        ParserObserving<ParseForest, Derivation, ParseNode, StackNode, ParseState> observing, ParseState parseState,
        IReduce reduce, StackNode stack, IState gotoState, ParseForest[] parseForests) {
        Derivation derivation = parseForestManager.createDerivation(parseState, stack, reduce.production(),
            reduce.productionType(), parseForests);
        ParseNode parseNode = parseForestManager.createParseNode(parseState, stack, reduce.production(), derivation);

        StackNode newStackWithGotoState = stackManager.createStackNode(gotoState);
        StackLink<ParseForest, StackNode> link =
            stackManager.createStackLink(parseState, newStackWithGotoState, stack, (ParseForest) parseNode);

        if(reduce.isRejectProduction())
            stackManager.rejectStackLink(link);

        return newStackWithGotoState;
    }

}
