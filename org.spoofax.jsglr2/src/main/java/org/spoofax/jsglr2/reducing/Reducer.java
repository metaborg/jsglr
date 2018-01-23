package org.spoofax.jsglr2.reducing;

import org.metaborg.parsetable.IState;
import org.metaborg.parsetable.actions.IReduce;
import org.spoofax.jsglr2.parseforest.AbstractParseForest;
import org.spoofax.jsglr2.parseforest.ParseForestManager;
import org.spoofax.jsglr2.parser.Parse;
import org.spoofax.jsglr2.stack.AbstractStackNode;
import org.spoofax.jsglr2.stack.StackLink;
import org.spoofax.jsglr2.stack.StackManager;

public class Reducer<ParseForest extends AbstractParseForest, ParseNode extends ParseForest, Derivation, StackNode extends AbstractStackNode<ParseForest>> {

    protected final StackManager<ParseForest, StackNode> stackManager;
    protected final ParseForestManager<ParseForest, ParseNode, Derivation> parseForestManager;

    public Reducer(StackManager<ParseForest, StackNode> stackManager,
        ParseForestManager<ParseForest, ParseNode, Derivation> parseForestManager) {
        this.stackManager = stackManager;
        this.parseForestManager = parseForestManager;
    }

    /**
     * Performs a reduction when an existing active stack is found with the required goto state and when there is a
     * direct link found between this active stack and the stack from where the reduction started. This means the
     * currently reduced derivation will be added as an alternative to the parse node on the link. This means the parse
     * node is ambiguous.
     */
    public void reducerExistingStackWithDirectLink(Parse<ParseForest, StackNode> parse, IReduce reduce,
        StackLink<ParseForest, StackNode> existingDirectLinkToActiveStateWithGoto, ParseForest[] parseForests) {
        Derivation derivation =
            parseForestManager.createDerivation(parse, existingDirectLinkToActiveStateWithGoto.to.position,
                reduce.production(), reduce.productionType(), parseForests);

        @SuppressWarnings("unchecked") ParseNode parseNode =
            (ParseNode) existingDirectLinkToActiveStateWithGoto.parseForest;

        parseForestManager.addDerivation(parse, parseNode, derivation);

        if(reduce.isRejectProduction())
            stackManager.rejectStackLink(parse, existingDirectLinkToActiveStateWithGoto);
    }

    /**
     * Performs a reduction when an existing active stack is found with the required goto state but when there is not
     * already a direct link present between this active stack and the stack from where the reduction started. The link
     * between these stacks is created and the currently reduced derivation is added as the first derivation for the
     * parse node on the link.
     */
    public StackLink<ParseForest, StackNode> reducerExistingStackWithoutDirectLink(Parse<ParseForest, StackNode> parse,
        IReduce reduce, StackNode existingActiveStackWithGotoState, StackNode stack, ParseForest[] parseForests) {
        Derivation derivation = parseForestManager.createDerivation(parse, stack.position, reduce.production(),
            reduce.productionType(), parseForests);
        ParseForest parseNode =
            parseForestManager.createParseNode(parse, stack.position, reduce.production(), derivation);

        StackLink<ParseForest, StackNode> newDirectLinkToActiveStateWithGoto =
            stackManager.createStackLink(parse, existingActiveStackWithGotoState, stack, parseNode);

        if(reduce.isRejectProduction())
            stackManager.rejectStackLink(parse, newDirectLinkToActiveStateWithGoto);

        return newDirectLinkToActiveStateWithGoto;
    }

    /**
     * Performs a reduction when no active stack is found with the required goto state. A new stack with the required
     * goto state is created and a link between this stack and the stack from where the reduction started is created.
     * The currently reduced derivation is added as the first derivation for the parse node on the link.
     */
    public StackNode reducerNoExistingStack(Parse<ParseForest, StackNode> parse, IReduce reduce, StackNode stack,
        IState gotoState, ParseForest[] parseForests) {
        Derivation derivation = parseForestManager.createDerivation(parse, stack.position, reduce.production(),
            reduce.productionType(), parseForests);
        ParseForest parseNode =
            parseForestManager.createParseNode(parse, stack.position, reduce.production(), derivation);

        StackNode newStackWithGotoState = stackManager.createStackNode(parse, gotoState);
        StackLink<ParseForest, StackNode> link =
            stackManager.createStackLink(parse, newStackWithGotoState, stack, parseNode);

        if(reduce.isRejectProduction())
            stackManager.rejectStackLink(parse, link);

        return newStackWithGotoState;
    }

}
