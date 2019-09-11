package org.spoofax.jsglr2.reducing;

import org.metaborg.parsetable.IParseTable;
import org.metaborg.parsetable.actions.IReduce;
import org.metaborg.parsetable.states.IState;
import org.spoofax.jsglr2.parseforest.IDerivation;
import org.spoofax.jsglr2.parseforest.IParseForest;
import org.spoofax.jsglr2.parseforest.ParseForestConstruction;
import org.spoofax.jsglr2.parseforest.ParseForestManager;
import org.spoofax.jsglr2.parser.AbstractParseState;
import org.spoofax.jsglr2.parser.Parse;
import org.spoofax.jsglr2.stack.AbstractStackManager;
import org.spoofax.jsglr2.stack.IStackNode;
import org.spoofax.jsglr2.stack.StackLink;
import org.spoofax.jsglr2.stack.paths.StackPath;

import java.util.ArrayList;
import java.util.List;

public class ReduceManager
//@formatter:off
   <ParseForest extends IParseForest,
    ParseNode   extends ParseForest,
    Derivation  extends IDerivation<ParseForest>,
    StackNode   extends IStackNode,
    ParseState  extends AbstractParseState<ParseForest, StackNode>>
//@formatter:on
{

    protected final IParseTable parseTable;
    protected final AbstractStackManager<ParseForest, StackNode, ParseState> stackManager;
    protected final ParseForestManager<ParseForest, ParseNode, Derivation, StackNode, ParseState> parseForestManager;
    protected final Reducer<ParseForest, ParseNode, Derivation, StackNode, ParseState> reducer;
    protected final List<ReduceFilter<ParseForest, StackNode, ParseState>> reduceFilters;

    public ReduceManager(IParseTable parseTable, AbstractStackManager<ParseForest, StackNode, ParseState> stackManager,
        ParseForestManager<ParseForest, ParseNode, Derivation, StackNode, ParseState> parseForestManager,
        ParseForestConstruction parseForestConstruction) {
        this.parseTable = parseTable;
        this.stackManager = stackManager;
        this.parseForestManager = parseForestManager;
        this.reduceFilters = new ArrayList<>();

        if(parseForestConstruction == ParseForestConstruction.Optimized)
            this.reducer = new ReducerSkipLayoutAndLexicalAndRejects<>(stackManager, parseForestManager);
        else
            this.reducer = new Reducer<>(stackManager, parseForestManager);
    }

    public void addFilter(ReduceFilter<ParseForest, StackNode, ParseState> reduceFilter) {
        reduceFilters.add(reduceFilter);
    }

    public void doReductions(Parse<ParseForest, StackNode, ParseState> parse, StackNode stack, IReduce reduce) {
        if(ignoreReduce(parse, reduce))
            return;

        parse.observing.notify(observer -> observer.doReductions(parse, stack, reduce));

        doReductionsHelper(parse, stack, reduce, null);
    }

    private void doLimitedReductions(Parse<ParseForest, StackNode, ParseState> parse, StackNode stack, IReduce reduce,
        StackLink<ParseForest, StackNode> throughLink) {
        if(ignoreReduce(parse, reduce))
            return;

        parse.observing.notify(observer -> observer.doLimitedReductions(parse, stack, reduce, throughLink));

        doReductionsHelper(parse, stack, reduce, throughLink);
    }

    private boolean ignoreReduce(Parse<ParseForest, StackNode, ParseState> parse, IReduce reduce) {
        for(ReduceFilter<ParseForest, StackNode, ParseState> reduceFilter : reduceFilters) {
            if(reduceFilter.ignoreReduce(parse, reduce))
                return true;
        }

        return false;
    }

    protected void doReductionsHelper(Parse<ParseForest, StackNode, ParseState> parse, StackNode stack, IReduce reduce,
        StackLink<ParseForest, StackNode> throughLink) {
        for(StackPath<ParseForest, StackNode> path : stackManager.findAllPathsOfLength(stack, reduce.arity())) {
            if(throughLink == null || path.contains(throughLink)) {
                StackNode pathBegin = path.head();
                ParseForest[] parseNodes = stackManager.getParseForests(parseForestManager, path);

                reducer(parse, pathBegin, reduce, parseNodes);
            }
        }
    }

    /**
     * Perform a reduction for the given reduce action and parse forests. The reduce action contains which production
     * will be reduced and the parse forests represent the right hand side of this production. The reduced derivation
     * will end up on a stack link from the given stack to a stack with the goto state. The latter can already exist or
     * not and if such an active stack already exists, the link to it can also already exist. Based on the existence of
     * the stack with the goto state and the link to it, different actions are performed.
     */
    protected void reducer(Parse<ParseForest, StackNode, ParseState> parse, StackNode stack, IReduce reduce,
        ParseForest[] parseForests) {
        int gotoId = stack.state().getGotoId(reduce.production().id());
        IState gotoState = parseTable.getState(gotoId);

        StackNode activeStackWithGotoState = parse.state.activeStacks.findWithState(gotoState);

        parse.observing.notify(observer -> observer.reducer(stack, reduce, parseForests, activeStackWithGotoState));

        if(activeStackWithGotoState != null) {
            StackLink<ParseForest, StackNode> directLink = stackManager.findDirectLink(activeStackWithGotoState, stack);

            parse.observing.notify(observer -> observer.directLinkFound(parse, directLink));

            if(directLink != null) {
                reducer.reducerExistingStackWithDirectLink(parse, reduce, directLink, parseForests);
            } else {
                StackLink<ParseForest, StackNode> link = reducer.reducerExistingStackWithoutDirectLink(parse, reduce,
                    activeStackWithGotoState, stack, parseForests);

                for(StackNode activeStack : parse.state.activeStacks.forLimitedReductions(parse.state.forActorStacks)) {
                    for(IReduce reduceAction : activeStack.state().getApplicableReduceActions(parse.state))
                        doLimitedReductions(parse, activeStack, reduceAction, link);
                }
            }
        } else {
            StackNode newStack = reducer.reducerNoExistingStack(parse, reduce, stack, gotoState, parseForests);

            parse.state.activeStacks.add(newStack);
            parse.state.forActorStacks.add(newStack);
        }
    }

}
