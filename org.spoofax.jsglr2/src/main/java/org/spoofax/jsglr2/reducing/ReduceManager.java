package org.spoofax.jsglr2.reducing;

import java.util.ArrayList;
import java.util.List;

import org.metaborg.parsetable.IParseTable;
import org.metaborg.parsetable.actions.IReduce;
import org.metaborg.parsetable.states.IState;
import org.metaborg.util.iterators.Iterables2;
import org.spoofax.jsglr2.inputstack.IInputStack;
import org.spoofax.jsglr2.parseforest.IDerivation;
import org.spoofax.jsglr2.parseforest.IParseForest;
import org.spoofax.jsglr2.parseforest.IParseNode;
import org.spoofax.jsglr2.parseforest.ParseForestManager;
import org.spoofax.jsglr2.parser.AbstractParseState;
import org.spoofax.jsglr2.parser.observing.ParserObserving;
import org.spoofax.jsglr2.stack.AbstractStackManager;
import org.spoofax.jsglr2.stack.IStackNode;
import org.spoofax.jsglr2.stack.StackLink;
import org.spoofax.jsglr2.stack.paths.StackPath;

public class ReduceManager
//@formatter:off
   <ParseForest extends IParseForest,
    Derivation  extends IDerivation<ParseForest>,
    ParseNode   extends IParseNode<ParseForest, Derivation>,
    StackNode   extends IStackNode,
    InputStack  extends IInputStack,
    ParseState  extends AbstractParseState<InputStack, StackNode>>
//@formatter:on
{

    protected final IParseTable parseTable;
    protected final AbstractStackManager<ParseForest, Derivation, ParseNode, StackNode, ParseState> stackManager;
    protected final ParseForestManager<ParseForest, Derivation, ParseNode, StackNode, ParseState> parseForestManager;
    protected final Reducer<ParseForest, Derivation, ParseNode, StackNode, InputStack, ParseState> reducer;
    protected final List<ReduceActionFilter<ParseForest, StackNode, ParseState>> reduceActionFilters;

    public ReduceManager(IParseTable parseTable,
        AbstractStackManager<ParseForest, Derivation, ParseNode, StackNode, ParseState> stackManager,
        ParseForestManager<ParseForest, Derivation, ParseNode, StackNode, ParseState> parseForestManager,
        ReducerFactory<ParseForest, Derivation, ParseNode, StackNode, InputStack, ParseState> reducerFactory) {
        this.parseTable = parseTable;
        this.stackManager = stackManager;
        this.parseForestManager = parseForestManager;
        this.reduceActionFilters = new ArrayList<>();
        this.reducer = reducerFactory.get(stackManager, parseForestManager);
    }

    public static
    //@formatter:off
       <ParseForest_  extends IParseForest,
        Derivation_   extends IDerivation<ParseForest_>,
        ParseNode_    extends IParseNode<ParseForest_, Derivation_>,
        StackNode_    extends IStackNode,
        InputStack_   extends IInputStack,
        ParseState_   extends AbstractParseState<InputStack_, StackNode_>,
        StackManager_ extends AbstractStackManager<ParseForest_, Derivation_, ParseNode_, StackNode_, ParseState_>>
    //@formatter:on
    ReduceManagerFactory<ParseForest_, Derivation_, ParseNode_, StackNode_, InputStack_, ParseState_, StackManager_, ReduceManager<ParseForest_, Derivation_, ParseNode_, StackNode_, InputStack_, ParseState_>>
        factory(
            ReducerFactory<ParseForest_, Derivation_, ParseNode_, StackNode_, InputStack_, ParseState_> reducerFactory) {
        return (parseTable, stackManager, parseForestManager) -> new ReduceManager<>(parseTable, stackManager,
            parseForestManager, reducerFactory);
    }

    public void addFilter(ReduceActionFilter<ParseForest, StackNode, ParseState> reduceActionFilter) {
        reduceActionFilters.add(reduceActionFilter);
    }

    public void doReductions(ParserObserving<ParseForest, Derivation, ParseNode, StackNode, ParseState> observing,
        ParseState parseState, StackNode activeStack, IReduce reduce) {
        if(ignoreReduceAction(parseState, activeStack, reduce))
            return;

        observing.notify(observer -> observer.doReductions(parseState, activeStack, reduce));

        doReductionsHelper(observing, parseState, activeStack, reduce, null);
    }

    private void doLimitedReductions(
        ParserObserving<ParseForest, Derivation, ParseNode, StackNode, ParseState> observing, ParseState parseState,
        StackNode stack, IReduce reduce, StackLink<ParseForest, StackNode> throughLink) {
        if(ignoreReduceAction(parseState, stack, reduce))
            return;

        observing.notify(observer -> observer.doLimitedReductions(parseState, stack, reduce, throughLink));

        doReductionsHelper(observing, parseState, stack, reduce, throughLink);
    }

    private boolean ignoreReduceAction(ParseState parseState, StackNode stack, IReduce reduce) {
        for(ReduceActionFilter<ParseForest, StackNode, ParseState> reduceActionFilter : reduceActionFilters) {
            if(reduceActionFilter.ignoreReduce(parseState, stack, reduce))
                return true;
        }

        return false;
    }

    protected void doReductionsHelper(
        ParserObserving<ParseForest, Derivation, ParseNode, StackNode, ParseState> observing, ParseState parseState,
        StackNode activeStack, IReduce reduce, StackLink<ParseForest, StackNode> throughLink) {
        for(StackPath<ParseForest, StackNode> path : stackManager.findAllPathsOfLength(activeStack, reduce.arity())) {
            if(throughLink == null || path.contains(throughLink)) {
                StackNode originStack = path.head();
                ParseForest[] parseNodes = stackManager.getParseForests(parseForestManager, path);

                if(!ignoreReducePath(originStack, reduce, parseNodes))
                    reducer(observing, parseState, activeStack, originStack, reduce, parseNodes);
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
        ParseState parseState, StackNode activeStack, StackNode originStack, IReduce reduce,
        ParseForest[] parseForests) {
        int gotoId = originStack.state().getGotoId(reduce.production().id());
        IState gotoState = parseTable.getState(gotoId);

        StackNode gotoStack = parseState.activeStacks.findWithState(gotoState);

        if(gotoStack != null) {
            StackLink<ParseForest, StackNode> directLink = stackManager.findDirectLink(gotoStack, originStack);

            observing.notify(observer -> observer.directLinkFound(parseState, directLink));

            if(directLink != null) {
                reducer.reducerExistingStackWithDirectLink(observing, parseState, reduce, directLink, parseForests);
            } else {
                StackLink<ParseForest, StackNode> link = reducer.reducerExistingStackWithoutDirectLink(observing,
                    parseState, reduce, gotoStack, originStack, parseForests);

                ArrayList<StackNode> stackNodes = Iterables2.toArrayList(parseState.activeStacks
                        .forLimitedReductions(parseState.forActorStacks));
                for(StackNode activeStackForLimitedReductions : stackNodes) {
                    for(IReduce reduceAction : activeStackForLimitedReductions.state()
                        .getApplicableReduceActions(parseState.inputStack, parseState.mode))
                        doLimitedReductions(observing, parseState, activeStackForLimitedReductions, reduceAction, link);
                }
            }
        } else {
            gotoStack =
                reducer.reducerNoExistingStack(observing, parseState, reduce, originStack, gotoState, parseForests);

            parseState.activeStacks.add(gotoStack);
            parseState.forActorStacks.add(gotoStack);
        }

        StackNode finalGotoStack = gotoStack;
        observing.notify(
            observer -> observer.reducer(parseState, activeStack, originStack, reduce, parseForests, finalGotoStack));
    }

}
