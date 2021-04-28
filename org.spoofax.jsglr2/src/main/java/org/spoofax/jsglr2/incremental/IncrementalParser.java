package org.spoofax.jsglr2.incremental;

import static com.google.common.collect.Iterables.size;
import static org.metaborg.util.iterators.Iterables2.stream;
import static org.spoofax.jsglr2.parser.observing.IParserObserver.BreakdownReason.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.metaborg.parsetable.IParseTable;
import org.metaborg.parsetable.actions.*;
import org.spoofax.jsglr2.JSGLR2Request;
import org.spoofax.jsglr2.incremental.actions.GotoShift;
import org.spoofax.jsglr2.incremental.diff.IStringDiff;
import org.spoofax.jsglr2.incremental.diff.JGitHistogramDiff;
import org.spoofax.jsglr2.incremental.diff.ProcessUpdates;
import org.spoofax.jsglr2.incremental.parseforest.IncrementalDerivation;
import org.spoofax.jsglr2.incremental.parseforest.IncrementalParseForest;
import org.spoofax.jsglr2.incremental.parseforest.IncrementalParseForestManager;
import org.spoofax.jsglr2.incremental.parseforest.IncrementalParseNode;
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
    private final IStringDiff diff;
    private final ProcessUpdates<StackNode, ParseState> processUpdates;

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
        // TODO parametrize parser on diff algorithm for benchmarking
        this.diff = new JGitHistogramDiff();
        this.processUpdates =
            new ProcessUpdates<>((IncrementalParseForestManager<StackNode, ParseState>) parseForestManager);
    }

    @Override protected ParseState getParseState(JSGLR2Request request, String previousInput,
        IncrementalParseForest previousResult) {
        IncrementalParseForest updatedTree = previousInput != null && previousResult != null
            ? processUpdates.processUpdates(previousInput, previousResult, diff.diff(previousInput, request.input))
            : processUpdates.getParseNodeFromString(request.input);
        return parseStateFactory.get(request, incrementalInputStackFactory.get(updatedTree, request.input), observing);
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

        IncrementalParseNode rootNode = (IncrementalParseNode) parseState.inputStack.getNode();

        // ...the root node is a temporary node
        if(rootNode.production() == null)
            return false;

        // ...the root node does not span the entire input
        if(rootNode.width() != parseState.inputStack.length())
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

        if(size(actions) > 1)
            parseState.setMultipleStates(true);

        observing.notify(observer -> observer.actor(stack, parseState, actions));

        for(IAction action : actions)
            actor(stack, parseState, action);
    }

    private Iterable<IAction> breakDownUntilValidActions(StackNode stack, ParseState parseState) {
        // Get actions based on the lookahead terminal from `inputStack.actionQueryCharacter`
        Iterable<IAction> originalActions = stack.state().getApplicableActions(parseState.inputStack, parseState.mode);

        IncrementalParseForest lookahead = parseState.inputStack.getNode();
        if(lookahead.isTerminal()) {
            return originalActions;
        }

        boolean hasShiftActions = stream(originalActions).anyMatch(a -> a.actionType() == ActionType.SHIFT);

        do {
            IncrementalParseNode lookaheadNode = (IncrementalParseNode) lookahead;

            // Only allow shifting the subtree if the saved state matches the current state
            if(lookaheadNode.isReusable(stack.state())) {
                // Remove shift actions from the original actions list
                List<IAction> filteredActions = stream(originalActions)
                    .filter(a -> a.actionType() == ActionType.REDUCE || a.actionType() == ActionType.REDUCE_LOOKAHEAD)
                    .collect(Collectors.toList());

                // Optimization: if the (only) reduce action already appears in the to-be-reused lookahead,
                // the reduce action can be removed.
                // This is to avoid multipleStates = true,
                // and should only happen in case multipleStates == false to avoid messing up other parse branches.
                if(parseState.newParseNodesAreReusable() && filteredActions.size() == 1
                    && nullReduceMatchesLookahead(stack, (IReduce) filteredActions.get(0), lookaheadNode)) {
                    filteredActions.clear();
                }

                // Reusable nodes have only one derivation, by definition, so the production of the node is correct
                filteredActions.add(new GotoShift(stack.state().getGotoId(lookaheadNode.production().id())));
                return filteredActions;
            }

            // Break down the lookahead in either of the following scenarios:
            // - the lookahead is not reusable, or
            // - the lookahead has applicable shift actions
            // If neither scenario is the case, directly return the current list of actions.
            if(lookaheadNode.isReusable() && !hasShiftActions) {
                return originalActions;
            }

            observing.notify(observer -> observer.breakDown(parseState.inputStack,
                lookaheadNode.production() == null ? TEMPORARY : lookaheadNode.isReusable()
                    ? lookaheadNode.isReusable(stack.state()) ? NO_ACTIONS : WRONG_STATE : IRREUSABLE));

            parseState.inputStack.breakDown();
            observing.notify(observer -> observer.parseRound(parseState, parseState.activeStacks));

            // If the broken-down node has no children, it has been removed from the input stack.
            // Therefore, any GotoShift actions that were in the forShifter list become invalid.
            // They can be discarded, because they will replaced by 0-arity reductions.
            if(!parseState.forShifter.isEmpty() && lookaheadNode.getFirstDerivation().parseForests.length == 0)
                parseState.forShifter.clear();

            lookahead = parseState.inputStack.getNode();
            if(lookahead.isTerminal()) {
                return originalActions;
            }
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
            Set<StackNode> seen = new HashSet<>();
            for(ForShifterElement<StackNode> forShifterElement : oldForShifter) {
                StackNode forShifterStack = forShifterElement.stack;
                if(seen.contains(forShifterStack))
                    continue;
                seen.add(forShifterStack);

                // Note that there can be multiple shift states per stack,
                // due to shift/shift conflicts in the parse table.
                for(IAction action : forShifterStack.state().getApplicableActions(parseState.inputStack,
                    parseState.mode)) {
                    if(action.actionType() != ActionType.SHIFT)
                        continue;
                    addForShifter(parseState, forShifterStack, parseTable.getState(((IShift) action).shiftStateId()));
                }
            }
        }
    }

    // If there are two actions, with one reduce of arity 0 and one GotoShift that contains this subtree already,
    // then the reduce of arity 0 is not necessary.
    // This method returns whether this is the case.
    private boolean nullReduceMatchesLookahead(StackNode stack, IReduce reduceAction,
        IncrementalParseNode lookaheadNode) {
        if(reduceAction.arity() != 0)
            return false;
        int reduceGoto = stack.state().getGotoId(reduceAction.production().id());
        while(true) {
            if(reduceGoto == stack.state().getGotoId(lookaheadNode.production().id()))
                return true;
            IncrementalParseForest[] children = lookaheadNode.getFirstDerivation().parseForests;
            if(children.length == 0)
                return false;
            IncrementalParseForest child = children[0];
            if(child.isTerminal())
                return false;
            lookaheadNode = ((IncrementalParseNode) child);
        }
    }

    @Override protected IncrementalParseForest getNodeToShift(ParseState parseState) {
        return parseState.inputStack.getNode();
    }
}
