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
        parse.state = stack.state();

        Collection<IAction> actions;
        while((actions = getActions(stack, parse, parse.reducerLookahead)).size() == 0
            && parse.reducerLookahead instanceof IncrementalParseNode)
            parse.reducerLookahead = parse.reducerLookahead.leftBreakdown();

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
            try {
                return Collections.singletonList(new GotoShift(stack.state().getGotoId(production.id())));
            } catch(NullPointerException e) { // Can be thrown inside getGotoId or because production == null
                return Collections.emptyList();
            }
        }
    }

    @Override protected IncrementalParseForest getCharacterNodeToShift(Parse parse) {
        parse.multipleStates = parse.forShifter.size() > 1;

        if(parse.forShifter.size() == 0) // This should only happen when the parser has already accepted or has failed
            return IncrementalCharacterNode.EOF_NODE;

        int forShifterState = parse.forShifter.peek().state.id();

        while(!parse.shiftLookahead.isTerminal()) {
            IncrementalParseNode shiftLookahead = (IncrementalParseNode) parse.shiftLookahead;
            if(!parse.multipleStates && !shiftLookahead.isAmbiguous()
                && forShifterState == shiftLookahead.getFirstDerivation().state.id())
                break;
            parse.shiftLookahead = parse.shiftLookahead.leftBreakdown();
        }

        return parse.shiftLookahead;
    }
}
