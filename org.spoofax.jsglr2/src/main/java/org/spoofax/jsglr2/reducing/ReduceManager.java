package org.spoofax.jsglr2.reducing;

import org.metaborg.parsetable.IParseTable;
import org.metaborg.parsetable.actions.IReduce;
import org.metaborg.parsetable.states.IState;
import org.spoofax.jsglr2.parseforest.*;
import org.spoofax.jsglr2.parser.AbstractParseState;
import org.spoofax.jsglr2.parser.ParserVariant;
import org.spoofax.jsglr2.parser.observing.ParserObserving;
import org.spoofax.jsglr2.stack.AbstractStackManager;
import org.spoofax.jsglr2.stack.IStackNode;
import org.spoofax.jsglr2.stack.StackLink;
import org.spoofax.jsglr2.stack.paths.StackPath;

import java.util.ArrayList;
import java.util.List;

public class ReduceManager
//@formatter:off
   <ParseForest extends IParseForest,
    Derivation  extends IDerivation<ParseForest>,
    ParseNode   extends IParseNode<ParseForest, Derivation>,
    StackNode   extends IStackNode,
    ParseState  extends AbstractParseState<ParseForest, StackNode>>
//@formatter:on
{

    protected final IParseTable parseTable;
    protected final AbstractStackManager<ParseForest, Derivation, ParseNode, StackNode, ParseState> stackManager;
    protected final ParseForestManager<ParseForest, Derivation, ParseNode, StackNode, ParseState> parseForestManager;
    protected final Reducer<ParseForest, Derivation, ParseNode, StackNode, ParseState> reducer;
    protected final List<ReduceActionFilter<ParseForest, StackNode, ParseState>> reduceActionFilters;

    public ReduceManager(IParseTable parseTable,
        AbstractStackManager<ParseForest, Derivation, ParseNode, StackNode, ParseState> stackManager,
        ParseForestManager<ParseForest, Derivation, ParseNode, StackNode, ParseState> parseForestManager,
        ParseForestConstruction parseForestConstruction) {
        this.parseTable = parseTable;
        this.stackManager = stackManager;
        this.parseForestManager = parseForestManager;
        this.reduceActionFilters = new ArrayList<>();

        if(parseForestConstruction == ParseForestConstruction.Optimized)
            this.reducer = new ReducerSkipLayoutAndLexicalAndRejects<>(stackManager, parseForestManager);
        else
            this.reducer = new Reducer<>(stackManager, parseForestManager);
    }

    public static
    //@formatter:off
       <ParseForest_  extends IParseForest,
        Derivation_   extends IDerivation<ParseForest_>,
        ParseNode_    extends IParseNode<ParseForest_, Derivation_>,
        StackNode_    extends IStackNode,
        ParseState_   extends AbstractParseState<ParseForest_, StackNode_>,
        StackManager_ extends AbstractStackManager<ParseForest_, Derivation_, ParseNode_, StackNode_, ParseState_>>
    //@formatter:on
    ReduceManagerFactory<ParseForest_, Derivation_, ParseNode_, StackNode_, ParseState_, StackManager_, ReduceManager<ParseForest_, Derivation_, ParseNode_, StackNode_, ParseState_>>
        factory(ParserVariant parserVariant) {
        return (parseTable, stackManager, parseForestManager) -> new ReduceManager<>(parseTable, stackManager,
            parseForestManager, parserVariant.parseForestConstruction);
    }

    public void addFilter(ReduceActionFilter<ParseForest, StackNode, ParseState> reduceActionFilter) {
        reduceActionFilters.add(reduceActionFilter);
    }

    public void doReductions(ParserObserving<ParseForest, Derivation, ParseNode, StackNode, ParseState> observing,
        ParseState parseState, StackNode stack, IReduce reduce) {
        if(ignoreReduceAction(parseState, reduce))
            return;

        observing.notify(observer -> observer.doReductions(parseState, stack, reduce));

        doReductionsHelper(observing, parseState, stack, reduce, null);
    }

    private void doLimitedReductions(
        ParserObserving<ParseForest, Derivation, ParseNode, StackNode, ParseState> observing, ParseState parseState,
        StackNode stack, IReduce reduce, StackLink<ParseForest, StackNode> throughLink) {
        if(ignoreReduceAction(parseState, reduce))
            return;

        observing.notify(observer -> observer.doLimitedReductions(parseState, stack, reduce, throughLink));

        doReductionsHelper(observing, parseState, stack, reduce, throughLink);
    }

    private boolean ignoreReduceAction(ParseState parseState, IReduce reduce) {
        for(ReduceActionFilter<ParseForest, StackNode, ParseState> reduceActionFilter : reduceActionFilters) {
            if(reduceActionFilter.ignoreReduce(parseState, reduce))
                return true;
        }

        return false;
    }

    protected void doReductionsHelper(
        ParserObserving<ParseForest, Derivation, ParseNode, StackNode, ParseState> observing, ParseState parseState,
        StackNode stack, IReduce reduce, StackLink<ParseForest, StackNode> throughLink) {
        for(StackPath<ParseForest, StackNode> path : stackManager.findAllPathsOfLength(stack, reduce.arity())) {
            if(throughLink == null || path.contains(throughLink)) {
                StackNode pathBegin = path.head();
                ParseForest[] parseNodes = stackManager.getParseForests(parseForestManager, path);

                if(!ignoreReducePath(pathBegin, reduce, parseNodes))
                    reducer(observing, parseState, pathBegin, reduce, parseNodes);
            }
        }
    }

    protected boolean ignoreReducePath(StackNode pathBegin, IReduce reduce, ParseForest[] parseNodes) {
        return false;
    }

    /**
     * Perform a reduction for the given reduce action and parse forests. The reduce action contains which production
     * will be reduced and the parse forests represent the right hand side of this production. The reduced derivation
     * will end up on a stack link from the given stack to a stack with the goto state. The latter can already exist or
     * not and if such an active stack already exists, the link to it can also already exist. Based on the existence of
     * the stack with the goto state and the link to it, different actions are performed.
     */
    protected void reducer(ParserObserving<ParseForest, Derivation, ParseNode, StackNode, ParseState> observing,
        ParseState parseState, StackNode stack, IReduce reduce, ParseForest[] parseForests) {
        int gotoId = stack.state().getGotoId(reduce.production().id());
        IState gotoState = parseTable.getState(gotoId);

        StackNode activeStackWithGotoState = parseState.activeStacks.findWithState(gotoState);

        observing.notify(observer -> observer.reducer(stack, reduce, parseForests, activeStackWithGotoState));

        if(activeStackWithGotoState != null) {
            StackLink<ParseForest, StackNode> directLink = stackManager.findDirectLink(activeStackWithGotoState, stack);

            observing.notify(observer -> observer.directLinkFound(parseState, directLink));

            if(directLink != null) {
                reducer.reducerExistingStackWithDirectLink(observing, parseState, reduce, directLink, parseForests);
            } else {
                StackLink<ParseForest, StackNode> link = reducer.reducerExistingStackWithoutDirectLink(observing,
                    parseState, reduce, activeStackWithGotoState, stack, parseForests);

                for(StackNode activeStack : parseState.activeStacks.forLimitedReductions(parseState.forActorStacks)) {
                    for(IReduce reduceAction : activeStack.state().getApplicableReduceActions(parseState))
                        doLimitedReductions(observing, parseState, activeStack, reduceAction, link);
                }
            }
        } else {
            StackNode newStack =
                reducer.reducerNoExistingStack(observing, parseState, reduce, stack, gotoState, parseForests);

            parseState.activeStacks.add(newStack);
            parseState.forActorStacks.add(newStack);
        }
    }

}
