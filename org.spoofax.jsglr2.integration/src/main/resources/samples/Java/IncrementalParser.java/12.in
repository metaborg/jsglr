package org.spoofax.jsglr2.incremental;


import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.metaborg.parsetable.IParseTable;
import org.metaborg.parsetable.IProduction;
import org.metaborg.parsetable.actions.IAction;
import org.metaborg.util.log.ILogger;
import org.metaborg.util.log.LoggerUtils;
import org.spoofax.jsglr2.incremental.actions.GotoShift;
import org.spoofax.jsglr2.incremental.parseforest.IncrementalCharacterNode;
import org.spoofax.jsglr2.incremental.parseforest.IncrementalDerivation;
import org.spoofax.jsglr2.incremental.parseforest.IncrementalParseForest;
import org.spoofax.jsglr2.incremental.parseforest.IncrementalParseNode;
import org.spoofax.jsglr2.parseforest.ParseForestManager;
import org.spoofax.jsglr2.parser.ParseFactory;
import org.spoofax.jsglr2.parser.Parser;
import org.spoofax.jsglr2.parser.result.ParseResult;
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

    public IncrementalParser(ParseFactory<IncrementalParseForest, StackNode, Parse> parseFactory,
        IncrementalParseFactory<StackNode, Parse> incrementalParseFactory, IParseTable parseTable,
        IActiveStacksFactory activeStacksFactory, IForActorStacksFactory forActorStacksFactory,
        AbstractStackManager<IncrementalParseForest, StackNode, Parse> stackManager,
        ParseForestManager<IncrementalParseForest, ParseNode, Derivation> parseForestManager,
        ReduceManager<IncrementalParseForest, ParseNode, Derivation, StackNode, Parse> reduceManager) {

        super(parseFactory, parseTable, activeStacksFactory, forActorStacksFactory, stackManager, parseForestManager,
            reduceManager);
        this.incrementalParseFactory = incrementalParseFactory;
    }

    @Override public ParseResult<IncrementalParseForest> parse(String inputString, String filename,
        String startSymbol) {
        ParseResult<IncrementalParseForest> result = super.parse(inputString, filename, startSymbol);
        logger.info(result.isSuccess ? "Parse success!" : "Parse failure!");
        return result;
    }

    public ParseResult<IncrementalParseForest> incrementalParse(List<EditorUpdate> editorUpdates,
        IncrementalParseForest previousVersion, String filename, String startSymbol) {

        IActiveStacks<StackNode> activeStacks = activeStacksFactory.get(observing);
        IForActorStacks<StackNode> forActorStacks = forActorStacksFactory.get(observing);

        Parse parse = incrementalParseFactory.get(editorUpdates, previousVersion, filename, activeStacks,
            forActorStacks, observing);

        ParseResult<IncrementalParseForest> result = parseInternal(startSymbol, parse);
        logger.info(result.isSuccess ? "Incremental parse success!" : "Incremental parse failure!");
        return result;
    }

    @Override protected void actor(StackNode stack, Parse parse) {
        Collection<IAction> actions;
        while((actions = getActions(stack, parse, parse.reducerLookahead.get())).size() == 0
            && !parse.reducerLookahead.get().isTerminal() && parse.forShifter.isEmpty())
            parse.reducerLookahead.leftBreakdown();

        if(actions.size() > 1)
            parse.multipleStates = true;

        Collection<IAction> finalActions = actions;
        observing.notify(observer -> observer.actor(stack, parse, finalActions));

        for(IAction action : actions)
            actor(stack, parse, action);
        // TODO case Accept, if reducerLookahead != EOF then abort parsing and return error? (should never happen)
    }

    private Collection<IAction> getActions(StackNode stack, Parse parse, IncrementalParseForest lookahead) {
        if(lookahead.isTerminal()) {
            LinkedList<IAction> actions = new LinkedList<>();
            stack.state().getApplicableActions(parse).forEach(actions::add);
            return actions;
        } else {
            IProduction production = ((IncrementalParseNode) lookahead).getFirstDerivation().production();
            if(production == null) // Force break down if production == null (TODO maybe move to actor?)
                return Collections.emptyList();
            LinkedList<IAction> actions = new LinkedList<>();
            // Get reduce actions based on the lookahead terminal that `parse` will calculate in actionQueryCharacter
            stack.state().getApplicableReduceActions(parse).forEach(actions::add);
            // Only allow shifting the subtree if the saved state matches the current state
            if(stack.state().id() == ((IncrementalParseNode) lookahead).getFirstDerivation().state.id()) {
                actions.add(new GotoShift(stack.state().getGotoId(production.id())));
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
                if(!parse.multipleStates && !shiftLookahead.isAmbiguous()
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
