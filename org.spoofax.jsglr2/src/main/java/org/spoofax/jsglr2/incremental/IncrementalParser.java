package org.spoofax.jsglr2.incremental;

import static org.spoofax.jsglr2.parser.observing.IParserObserver.BreakdownReason.*;

import java.util.*;

import org.metaborg.parsetable.IParseTable;
import org.metaborg.parsetable.actions.*;
import org.metaborg.parsetable.states.IState;
import org.spoofax.jsglr2.JSGLR2Request;
import org.spoofax.jsglr2.incremental.actions.GotoShift;
import org.spoofax.jsglr2.incremental.parseforest.IncrementalDerivation;
import org.spoofax.jsglr2.incremental.parseforest.IncrementalParseForest;
import org.spoofax.jsglr2.incremental.parseforest.IncrementalParseNode;
import org.spoofax.jsglr2.incremental.parseforest.IncrementalSkippedNode;
import org.spoofax.jsglr2.inputstack.incremental.IIncrementalInputStack;
import org.spoofax.jsglr2.inputstack.incremental.IncrementalInputStackFactory;
import org.spoofax.jsglr2.parseforest.Disambiguator;
import org.spoofax.jsglr2.parseforest.IParseNode;
import org.spoofax.jsglr2.parseforest.ParseForestManagerFactory;
import org.spoofax.jsglr2.parser.*;
import org.spoofax.jsglr2.parser.failure.ParseFailureHandlerFactory;
import org.spoofax.jsglr2.reducing.ReduceManagerFactory;
import org.spoofax.jsglr2.stack.AbstractStackManager;
import org.spoofax.jsglr2.stack.IStackNode;
import org.spoofax.jsglr2.stack.StackManagerFactory;

import com.google.common.collect.Iterables;

public class IncrementalParser
// @formatter:off
   <StackNode     extends IStackNode,
    ParseState    extends AbstractParseState<IIncrementalInputStack, StackNode> & IIncrementalParseState,
    StackManager  extends AbstractStackManager<IncrementalParseForest, IncrementalDerivation, IncrementalParseNode, StackNode, ParseState>,
    ReduceManager extends org.spoofax.jsglr2.reducing.ReduceManager<IncrementalParseForest, IncrementalDerivation, IncrementalParseNode, StackNode, IIncrementalInputStack, ParseState>>
// @formatter:on
    extends
    Parser<IncrementalParseForest, IncrementalDerivation, IncrementalParseNode, StackNode, IIncrementalInputStack, ParseState, StackManager, ReduceManager> {

    private final IncrementalInputStackFactory<IIncrementalInputStack> incrementalInputStackFactory;

    public IncrementalParser(IncrementalInputStackFactory<IIncrementalInputStack> incrementalInputStackFactory,
        ParseStateFactory<IncrementalParseForest, IncrementalDerivation, IncrementalParseNode, IIncrementalInputStack, StackNode, ParseState> parseStateFactory,
        IParseTable parseTable,
        StackManagerFactory<IncrementalParseForest, IncrementalDerivation, IncrementalParseNode, StackNode, ParseState, StackManager> stackManagerFactory,
        ParseForestManagerFactory<IncrementalParseForest, IncrementalDerivation, IncrementalParseNode, StackNode, ParseState> parseForestManagerFactory,
        Disambiguator<IncrementalParseForest, IncrementalDerivation, IncrementalParseNode, StackNode, ParseState> disambiguator,
        ReduceManagerFactory<IncrementalParseForest, IncrementalDerivation, IncrementalParseNode, StackNode, IIncrementalInputStack, ParseState, StackManager, ReduceManager> reduceManagerFactory,
        ParseFailureHandlerFactory<IncrementalParseForest, IncrementalDerivation, IncrementalParseNode, StackNode, ParseState> failureHandlerFactory,
        ParseReporterFactory<IncrementalParseForest, IncrementalDerivation, IncrementalParseNode, StackNode, IIncrementalInputStack, ParseState> reporterFactory) {

        super(null, parseStateFactory, parseTable, stackManagerFactory, parseForestManagerFactory, disambiguator,
            reduceManagerFactory, failureHandlerFactory, reporterFactory);

        this.incrementalInputStackFactory = incrementalInputStackFactory;
    }

    @Override protected ParseState getParseState(JSGLR2Request request, String previousInput,
        IncrementalParseForest previousResult) {
        return parseStateFactory.get(request,
            incrementalInputStackFactory.get(request.input, previousInput, previousResult), observing);
    }

    @Override protected void parseLoop(ParseState parseState) throws ParseException {
        if(!attemptToFullyReuse(parseState))
            super.parseLoop(parseState);
    }

    // Optimization: if the first node on the lookahead stack has no changes and can be completely reused, do so
    private boolean attemptToFullyReuse(ParseState parseState) {
        // We cannot do this optimization if...

        // ...the parse is not at the start anymore (parseLoop may be called multiple times due to recovery)
        if(parseState.inputStack.offset() != 0
            || parseState.activeStacks.getSingle().state().id() != parseTable.getStartState().id())
            return false;

        IncrementalParseForest node = parseState.inputStack.getNode();

        // ...the parse node at the top of the stack has already been broken down all the way down to a terminal
        if(node.isTerminal())
            return false;

        IncrementalParseNode rootNode = (IncrementalParseNode) node;

        // ...the root node is a temporary node
        if(rootNode.production() == null)
            return false;

        // ...the parse node at the top of the stack has already been broken down
        if(!rootNode.production().lhs().descriptor().equals("<START>"))
            return false;

        StackNode stack = parseState.activeStacks.getSingle();

        // Shift the entire tree
        addForShifter(parseState, stack, parseTable.getState(stack.state().getGotoId(rootNode.production().id())));
        shifter(parseState);
        parseState.inputStack.next();

        // Accept
        actor(parseState.activeStacks.getSingle(), parseState, Accept.SINGLETON);
        return true;
    }

    @Override protected void actor(StackNode stack, ParseState parseState) {
        IncrementalParseForest originalLookahead = parseState.inputStack.getNode();

        Iterable<IAction> actions = breakDownUntilValidActions(stack, parseState);

        // If we already had something to shift and the lookahead has been broken down,
        // update the goto states in forShifter based on the new lookahead.
        // If we wouldn't do this, it would cause different shifts to be desynchronised.
        if(!parseState.forShifter.isEmpty() && parseState.inputStack.getNode() != originalLookahead)
            updateForShifterStates(parseState);

        if(hasMoreThanOne(actions))
            parseState.setMultipleStates(true);

        observing.notify(observer -> observer.actor(stack, parseState, actions));

        for(IAction action : actions)
            actor(stack, parseState, action);
    }

    /**
     * Based on {@link com.google.common.collect.Iterables#size}.
     *
     * @return Whether the iterable has more than one element.
     */
    public static boolean hasMoreThanOne(Iterable<?> iterable) {
        if(iterable instanceof Collection)
            return ((Collection<?>) iterable).size() > 1;
        Iterator<?> iterator = iterable.iterator();
        if(!iterator.hasNext())
            return false;
        iterator.next();
        return iterator.hasNext();
    }

    private Iterable<IAction> breakDownUntilValidActions(StackNode stack, ParseState parseState) {
        // Get actions based on the lookahead terminal from `inputStack.actionQueryCharacter`
        Iterable<IAction> originalActions = stack.state().getApplicableActions(parseState.inputStack, parseState.mode);

        IncrementalParseForest lookahead = parseState.inputStack.getNode();
        if(lookahead.isTerminal())
            return originalActions;

        // Pre-calculate whether the list of actions contains shift actions, we need this information in the loop
        boolean hasShiftActions = false;
        for(IAction action : originalActions) {
            if(action.actionType() == ActionType.SHIFT) {
                hasShiftActions = true;
                break;
            }
        }

        do {
            IncrementalParseNode lookaheadNode = (IncrementalParseNode) lookahead;

            // Only allow shifting the subtree if the saved state matches the current state
            if(lookaheadNode.isReusable(stack.state()))
                return reduceActionsAndGotoShift(stack, parseState, lookaheadNode, originalActions);

            // Break down the lookahead in either of the following scenarios:
            // - the lookahead is not reusable, or
            // - the lookahead has applicable shift actions
            // If neither scenario is the case, directly return the current list of actions.
            if(lookaheadNode.isReusable() && !hasShiftActions)
                return originalActions;

            // If we are already planning to reuse a parse node, and the lookahead is not changed,
            // then we don't have to break down this parse node.
            if(!parseState.forShifter.isEmpty() && parseState.inputStack.lookaheadIsUnchanged())
                return Collections.emptyList();

            observing.notify(observer -> observer.breakDown(parseState.inputStack,
                lookaheadNode.production() == null ? TEMPORARY : lookaheadNode.isReusable()
                    ? lookaheadNode.isReusable(stack.state()) ? NO_ACTIONS : WRONG_STATE : IRREUSABLE));

            parseState.inputStack.breakDown();
            observing.notify(observer -> observer.parseRound(parseState, parseState.activeStacks));

            // If the broken-down node has no children, it has been removed from the input stack.
            // Therefore, any GotoShift actions that were in the forShifter list become invalid.
            // They can be discarded, because they will replaced by 0-arity reductions.
            if(!parseState.forShifter.isEmpty() && (lookaheadNode instanceof IncrementalSkippedNode
                ? lookaheadNode.width() == 0 : lookaheadNode.getFirstDerivation().parseForests.length == 0))
                parseState.forShifter.clear();

            lookahead = parseState.inputStack.getNode();
            if(lookahead.isTerminal())
                return originalActions;
        } while(true);
    }

    private void updateForShifterStates(ParseState parseState) {
        List<ForShifterElement<StackNode>> oldForShifter = new ArrayList<>(parseState.forShifter);
        parseState.forShifter.clear();

        IncrementalParseForest newLookaheadNode = parseState.inputStack.getNode();
        if(newLookaheadNode instanceof IParseNode) {
            // If the new lookahead node is a parse node, replace the forShifter states
            // with new goto states based on the production of the new lookahead node.
            int productionId = ((IParseNode<?, ?>) newLookaheadNode).production().id();
            for(ForShifterElement<StackNode> forShifterElement : oldForShifter) {
                StackNode forShifterStack = forShifterElement.stack;

                addForShifter(parseState, forShifterStack,
                    parseTable.getState(forShifterStack.state().getGotoId(productionId)));
            }
        } else {
            // If the new lookahead node is a character node, replace the forShifter states
            // with the shift states from the parse table.
            for(ForShifterElement<StackNode> forShifterElement : oldForShifter) {
                StackNode forShifterStack = forShifterElement.stack;

                // Note that there can be multiple shift states per stack,
                // due to shift/shift conflicts in the parse table.
                // However, we are certain that `oldForShifter` must contain each stack at most once,
                // because it can only contain GotoShift actions and the goto table cannot have conflicts.
                for(IAction action : forShifterStack.state().getApplicableActions(parseState.inputStack,
                    parseState.mode)) {
                    if(action.actionType() != ActionType.SHIFT)
                        continue;
                    addForShifter(parseState, forShifterStack, parseTable.getState(((IShift) action).shiftStateId()));
                }
            }
        }
    }

    // Returns the list of applicable reduce actions plus a new GotoShift action
    private List<IAction> reduceActionsAndGotoShift(StackNode stack, ParseState parseState,
        IncrementalParseNode lookaheadNode, Iterable<IAction> originalActions) {

        // Reusable nodes have only one derivation, by definition, so the production of the node is correct
        GotoShift gotoShift = new GotoShift(stack.state().getGotoId(lookaheadNode.production().id()));

        // Remove shift actions from the original actions list
        List<IAction> filteredActions = new ArrayList<>();
        // noinspection StaticPseudoFunctionalStyleMethod
        Iterables.addAll(filteredActions, Iterables.filter(originalActions, a -> a.actionType() != ActionType.SHIFT));

        // Optimization: if the production of the (only) reduce action
        // is the leftmost descendant of the to-be-reused lookahead, the reduce action can be removed.
        // This is to avoid multipleStates = true,
        // and should only happen in case multipleStates == false to avoid messing up other parse branches.
        if(parseState.newParseNodesAreReusable() && nullReduceMatchesLookahead(stack, filteredActions, lookaheadNode))
            return Collections.singletonList(gotoShift);

        filteredActions.add(gotoShift);
        return filteredActions;
    }

    // If there are two actions, with one reduce of arity 0 and one GotoShift
    // with a lookahead whose leftmost descendant has the same production as the reduce action,
    // then the reduce of arity 0 is not necessary.
    // This method returns whether this is the case.
    // Note that "descendant" includes the root, so the parameter `lookaheadNode` may already be the null-yield node.
    private boolean nullReduceMatchesLookahead(StackNode stack, List<IAction> reduceActions,
        IncrementalParseNode lookaheadNode) {
        if(reduceActions.isEmpty() || lookaheadNode instanceof IncrementalSkippedNode)
            return false;

        int[] reduceActionProductions = new int[reduceActions.size()];

        int i = 0;
        for(IAction action : reduceActions) {
            IReduce reduceAction = (IReduce) action;

            // If any reduce action in the list does NOT have arity 0, we cannot apply this optimization, so abort.
            if(reduceAction.arity() != 0)
                return false;

            reduceActionProductions[i++] = reduceAction.production().id();
        }

        while(true) {
            IncrementalParseForest[] children = lookaheadNode.getFirstDerivation().parseForests;

            // When we arrive at the leftmost descendant in the lookahead node, check if it is a null-yield node
            if(children.length == 0 && lookaheadNode.width() == 0) {
                IState state = stack.state();
                int lookaheadNodeGotoId = state.getGotoId(lookaheadNode.production().id());

                // If so, the production of this node must match any of the 0-arity reduce action productions
                for(int reduceActionProduction : reduceActionProductions) {
                    if(state.getGotoId(reduceActionProduction) == lookaheadNodeGotoId)
                        return true;
                }

                return false;
            }

            IncrementalParseForest child = children[0];
            if(child.isTerminal() || child instanceof IncrementalSkippedNode)
                return false;
            lookaheadNode = (IncrementalParseNode) child;
        }
    }

    @Override protected IncrementalParseForest getNodeToShift(ParseState parseState) {
        return parseState.inputStack.getNode();
    }
}
