package org.spoofax.jsglr2.incremental;

import static com.google.common.collect.Iterables.isEmpty;
import static com.google.common.collect.Iterables.size;
import static org.metaborg.util.iterators.Iterables2.stream;
import static org.spoofax.jsglr2.parser.observing.IParserObserver.BreakdownReason.*;

import java.util.*;
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
        Iterable<IAction> actions = getActions(stack, parseState);
        IncrementalParseForest lookaheadNode = parseState.inputStack.getNode();

        // Break down the lookahead in either of the following scenarios:
        // - The lookahead is not reusable (terminal nodes are always reusable).
        // - The lookahead is a non-terminal parse node AND there are no actions for it.
        while(!lookaheadNode.isReusable() || !lookaheadNode.isTerminal() && isEmpty(actions)) {
            IncrementalParseNode brokenDownNode = (IncrementalParseNode) lookaheadNode;
            observing.notify(observer -> observer.breakDown(parseState.inputStack,
                brokenDownNode.production() == null ? TEMPORARY : brokenDownNode.isReusable()
                    ? brokenDownNode.isReusable(stack.state()) ? NO_ACTIONS : WRONG_STATE : IRREUSABLE));

            parseState.inputStack.breakDown();
            observing.notify(observer -> observer.parseRound(parseState, parseState.activeStacks));
            actions = getActions(stack, parseState);
            lookaheadNode = parseState.inputStack.getNode();

            // If we already had something to shift, we should update the goto states based on the new lookahead.
            // If we wouldn't do this, it would cause different shifts to be desynchronised.
            if(!parseState.forShifter.isEmpty()) {
                List<ForShifterElement<StackNode>> oldForShifter = new ArrayList<>(parseState.forShifter);
                parseState.forShifter.clear();

                // If the broken-down node has no children, it has been removed from the input stack.
                // Therefore, any GotoShift actions that were in the forShifter list become invalid.
                // They can be discarded, because they will replaced by 0-arity reductions.
                if(brokenDownNode.getFirstDerivation().parseForests.length == 0)
                    continue;

                if(lookaheadNode instanceof IParseNode) {
                    // If the new lookahead node is a parse node, replace the forShifter states
                    // with new goto states based on the production of the new lookahead node.
                    int productionId = ((IParseNode<?, ?>) lookaheadNode).production().id();
                    for(ForShifterElement<StackNode> forShifterElement : oldForShifter) {
                        StackNode forShifterStack = forShifterElement.stack;
                        addForShifter(parseState, forShifterStack,
                            parseTable.getState(forShifterStack.state().getGotoId(productionId)));
                    }
                } else {
                    // If the new lookahead node is a character node, replace the forShifter states
                    // with the shift states from the parse table.
                    // Note that there can be multiple shift states per stack due to shift/shift conflicts in the table.
                    Set<StackNode> seen = new HashSet<>();
                    for(ForShifterElement<StackNode> forShifterElement : oldForShifter) {
                        StackNode forShifterStack = forShifterElement.stack;
                        if(seen.contains(forShifterStack))
                            continue;
                        seen.add(forShifterStack);

                        for(IAction action : getActions(forShifterStack, parseState)) {
                            if(action.actionType() != ActionType.SHIFT)
                                continue;
                            addForShifter(parseState, forShifterStack,
                                parseTable.getState(((IShift) action).shiftStateId()));
                        }
                    }
                }
            }
        }

        if(size(actions) > 1)
            parseState.setMultipleStates(true);

        Iterable<IAction> finalActions = actions;
        observing.notify(observer -> observer.actor(stack, parseState, finalActions));

        for(IAction action : actions)
            actor(stack, parseState, action);
    }

    // Inside this method, we can assume that the lookahead is a valid and complete subtree of the previous parse.
    // Else, the loop in `actor` will have broken it down
    private Iterable<IAction> getActions(StackNode stack, ParseState parseState) {
        // Get actions based on the lookahead terminal that `parse` will calculate in actionQueryCharacter
        Iterable<IAction> actions = stack.state().getApplicableActions(parseState.inputStack, parseState.mode);

        IncrementalParseForest lookahead = parseState.inputStack.getNode();
        if(lookahead.isTerminal()) {
            return actions;
        } else {
            // Split in shift and reduce actions
            List<IAction> shiftActions =
                stream(actions).filter(a -> a.actionType() == ActionType.SHIFT).collect(Collectors.toList());

            // By default, only the reduce actions are returned
            List<IAction> result = stream(actions)
                .filter(a -> a.actionType() == ActionType.REDUCE || a.actionType() == ActionType.REDUCE_LOOKAHEAD)
                .collect(Collectors.toList());

            IncrementalParseNode lookaheadNode = (IncrementalParseNode) lookahead;

            // Only allow shifting the subtree if the saved state matches the current state
            boolean reusable = lookaheadNode.isReusable(stack.state());
            if(reusable) {
                // If the (only) reduce action already appears in the to-be-reused lookahead,
                // the reduce action can be removed.
                // This is an optimization to avoid multipleStates == true,
                // and should only happen in case multipleStates == false to avoid messing up other parse branches.
                if(parseState.newParseNodesAreReusable() && result.size() == 1
                    && nullReduceMatchesLookahead(stack, (IReduce) result.get(0), lookaheadNode)) {
                    result.clear();
                }

                // Reusable nodes have only one derivation, by definition, so the production of the node is correct
                result.add(new GotoShift(stack.state().getGotoId(lookaheadNode.production().id())));
            }

            // If we don't have a GotoShift action, but do have regular shift actions, we should break down further.
            // In case we do NOT have regular shift actions, the reduce actions should be executed before breaking down.
            if(!reusable && !shiftActions.isEmpty()) {
                return Collections.emptyList(); // Return no actions, to trigger breakdown
            }

            return result;
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
