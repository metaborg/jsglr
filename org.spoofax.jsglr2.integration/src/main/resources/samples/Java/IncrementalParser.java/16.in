package org.spoofax.jsglr2.incremental;


import static org.spoofax.jsglr2.incremental.IncrementalParse.NO_STATE;

import java.util.*;

import org.metaborg.parsetable.IParseTable;
import org.metaborg.parsetable.actions.IAction;
import org.metaborg.util.log.ILogger;
import org.metaborg.util.log.LoggerUtils;
import org.spoofax.jsglr2.incremental.actions.GotoShift;
import org.spoofax.jsglr2.incremental.diff.SingleDiff;
import org.spoofax.jsglr2.incremental.parseforest.IncrementalCharacterNode;
import org.spoofax.jsglr2.incremental.parseforest.IncrementalDerivation;
import org.spoofax.jsglr2.incremental.parseforest.IncrementalParseForest;
import org.spoofax.jsglr2.incremental.parseforest.IncrementalParseNode;
import org.spoofax.jsglr2.parseforest.ParseForestManager;
import org.spoofax.jsglr2.parser.ParseFactory;
import org.spoofax.jsglr2.parser.Parser;
import org.spoofax.jsglr2.parser.result.ParseResult;
import org.spoofax.jsglr2.parser.result.ParseSuccess;
import org.spoofax.jsglr2.reducing.ReduceManager;
import org.spoofax.jsglr2.stack.AbstractStackManager;
import org.spoofax.jsglr2.stack.IStackNode;
import org.spoofax.jsglr2.stack.collections.IActiveStacks;
import org.spoofax.jsglr2.stack.collections.IActiveStacksFactory;
import org.spoofax.jsglr2.stack.collections.IForActorStacks;
import org.spoofax.jsglr2.stack.collections.IForActorStacksFactory;

public class IncrementalParser
// @formatter:off
   <ParseNode extends IncrementalParseNode,
    Derivation extends IncrementalDerivation,
    StackNode extends IStackNode,
    Parse extends IncrementalParse<StackNode>>
// @formatter:on
    extends Parser<IncrementalParseForest, ParseNode, Derivation, StackNode, Parse> {

    private static final ILogger logger = LoggerUtils.logger(IncrementalParser.class);
    private final IncrementalParseFactory<StackNode, Parse> incrementalParseFactory;
    private final HashMap<String, IncrementalParseForest> cache = new HashMap<>();
    private final HashMap<String, String> oldString = new HashMap<>();
    private final SingleDiff diff;

    public IncrementalParser(ParseFactory<IncrementalParseForest, StackNode, Parse> parseFactory,
        IncrementalParseFactory<StackNode, Parse> incrementalParseFactory, IParseTable parseTable,
        IActiveStacksFactory activeStacksFactory, IForActorStacksFactory forActorStacksFactory,
        AbstractStackManager<IncrementalParseForest, StackNode, Parse> stackManager,
        ParseForestManager<IncrementalParseForest, ParseNode, Derivation> parseForestManager,
        ReduceManager<IncrementalParseForest, ParseNode, Derivation, StackNode, Parse> reduceManager) {

        super(parseFactory, parseTable, activeStacksFactory, forActorStacksFactory, stackManager, parseForestManager,
            reduceManager);
        this.incrementalParseFactory = incrementalParseFactory;
        // TODO different diffing types, probably based on:
        // https://en.wikipedia.org/wiki/Longest_common_subsequence_problem
        this.diff = new SingleDiff();
    }

    @Override public ParseResult<IncrementalParseForest> parse(String inputString, String filename,
        String startSymbol) {
        ParseResult<IncrementalParseForest> result;
        long begin = System.currentTimeMillis();
        IncrementalParseForest previous = null;

        if(!filename.equals("") && cache.containsKey(filename) && oldString.containsKey(filename)) {
            previous = cache.get(filename);
            result = incrementalParse(diff.diff(oldString.get(filename), inputString), previous, filename, startSymbol);
        } else
            result = super.parse(inputString, filename, startSymbol);

        long time = System.currentTimeMillis() - begin;
        logger.info((previous == null ? "Clean" : "Incremental") + " parse "
            + (result.isSuccess ? "success" : "failure") + "! (" + time + " ms)");

        if(result.isSuccess && !filename.equals("")) {
            oldString.put(filename, inputString);
            cache.put(filename, (IncrementalParseForest) ((ParseSuccess) result).parseResult);
        }

        return result;
    }

    public ParseResult<IncrementalParseForest> incrementalParse(List<EditorUpdate> editorUpdates,
        IncrementalParseForest previousVersion, String filename, String startSymbol) {

        IActiveStacks<StackNode> activeStacks = activeStacksFactory.get(observing);
        IForActorStacks<StackNode> forActorStacks = forActorStacksFactory.get(observing);

        Parse parse = incrementalParseFactory.get(editorUpdates, previousVersion, filename, activeStacks,
            forActorStacks, observing);

        ParseResult<IncrementalParseForest> result = parseInternal(startSymbol, parse);
        // Commented out because the `incrementalParse` method is not called externally, and `parse` already logs
        // logger.info(result.isSuccess ? "Incremental parse success!" : "Incremental parse failure!");
        return result;
    }

    @Override protected void actor(StackNode stack, Parse parse) {
        Collection<IAction> actions =
            lookaheadHasNoState(parse.reducerLookahead.get()) ? Collections.emptyList() : getActions(stack, parse);
        while(!parse.reducerLookahead.get().isTerminal() && (lookaheadHasNoState(parse.reducerLookahead.get())
            || actions.size() == 0 && parse.forShifter.isEmpty())) {
            parse.reducerLookahead.leftBreakdown();
            actions = getActions(stack, parse);
        }

        if(actions.size() > 1)
            parse.multipleStates = true;

        Collection<IAction> finalActions = actions;
        observing.notify(observer -> observer.actor(stack, parse, finalActions));

        for(IAction action : actions)
            actor(stack, parse, action);
        // TODO case Accept, if reducerLookahead != EOF then abort parsing and return error? (should never happen)
    }

    private boolean lookaheadHasNoState(IncrementalParseForest lookahead) {
        return !lookahead.isTerminal()
            && ((IncrementalParseNode) lookahead).getFirstDerivation().state.equals(NO_STATE);
    }

    // Inside this method, we can assume that the lookahead is a valid subtree of the previous parse.
    // Else, the loop in `actor` will have broken it down
    private Collection<IAction> getActions(StackNode stack, Parse parse) {
        IncrementalParseForest lookahead = parse.reducerLookahead.get();
        if(lookahead.isTerminal()) {
            LinkedList<IAction> actions = new LinkedList<>();
            stack.state().getApplicableActions(parse).forEach(actions::add);
            return actions;
        } else {
            LinkedList<IAction> actions = new LinkedList<>();

            // Get reduce actions based on the lookahead terminal that `parse` will calculate in actionQueryCharacter
            stack.state().getApplicableReduceActions(parse).forEach(actions::add);

            // Only allow shifting the subtree if the saved state matches the current state
            // TODO if lookahead.width() == 0 && production matches, there is a duplicate action
            for(IncrementalDerivation derivation : ((IncrementalParseNode) lookahead).getDerivations()) {
                if(stack.state().id() == derivation.state.id()) {
                    actions.add(new GotoShift(stack.state().getGotoId(derivation.production().id())));
                }
            }
            return actions;
        }
    }

    @Override protected IncrementalParseForest getCharacterNodeToShift(Parse parse) {
        parse.multipleStates = parse.forShifter.size() > 1;

        // This should only happen when the parser has already accepted or has failed
        if(parse.forShifter.size() == 0) {
            // If the lookahead has no yield, the actor will have called leftBreakdown on the reducerLookahead.
            // This should also happen for the shiftLookahead, so that the lock-step property does not break.
            while(parse.shiftLookahead.get().width() <= 0)
                parse.shiftLookahead.leftBreakdown();
            return IncrementalCharacterNode.EOF_NODE;
        }

        // Only used if forShifter.size() == 1, because multipleStates == false in that case
        int forShifterState = parse.forShifter.peek().state.id();

        while(!parse.shiftLookahead.get().isTerminal()) {
            IncrementalParseNode shiftLookahead = (IncrementalParseNode) parse.shiftLookahead.get();
            try {
                // forShifterState is the state being shifted to.
                // In the shiftLookahead, the state is stored _before_ shifting.
                // If goto(stored_state, stored_production) == forShifterState, then shift subtree.
                if(!parse.multipleStates && !shiftLookahead.isAmbiguous() && !lookaheadHasNoState(shiftLookahead)
                    && forShifterState == shiftLookahead.getFirstDerivation().state
                        .getGotoId(shiftLookahead.production().id()))
                    break;
            } catch(NullPointerException ignored) {
                // NPE can be thrown when the shiftLookahead does not have a state and/or valid goto.
                // In that case, we just need to continue breaking down the tree
            }
            parse.shiftLookahead.leftBreakdown();
        }

        return parse.shiftLookahead.get();
    }
}
