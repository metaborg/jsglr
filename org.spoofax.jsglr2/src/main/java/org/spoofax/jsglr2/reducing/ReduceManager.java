package org.spoofax.jsglr2.reducing;

import org.metaborg.parsetable.IParseTable;
import org.metaborg.parsetable.IState;
import org.metaborg.parsetable.actions.IReduce;
import org.spoofax.jsglr2.parseforest.AbstractParseForest;
import org.spoofax.jsglr2.parseforest.ParseForestConstruction;
import org.spoofax.jsglr2.parseforest.ParseForestManager;
import org.spoofax.jsglr2.parser.Parse;
import org.spoofax.jsglr2.stack.AbstractStackNode;
import org.spoofax.jsglr2.stack.StackLink;
import org.spoofax.jsglr2.stack.StackManager;
import org.spoofax.jsglr2.stack.paths.StackPath;

public class ReduceManager<ParseForest extends AbstractParseForest, ParseNode extends ParseForest, Derivation, StackNode extends AbstractStackNode<ParseForest>> {

    protected final IParseTable parseTable;
    protected final StackManager<ParseForest, StackNode> stackManager;
    protected final ParseForestManager<ParseForest, ParseNode, Derivation> parseForestManager;
    protected final Reducer<ParseForest, ParseNode, Derivation, StackNode> reducer;

    public ReduceManager(IParseTable parseTable, StackManager<ParseForest, StackNode> stackManager,
        ParseForestManager<ParseForest, ParseNode, Derivation> parseForestManager,
        ParseForestConstruction parseForestConstruction) {
        this.parseTable = parseTable;
        this.stackManager = stackManager;
        this.parseForestManager = parseForestManager;

        if(parseForestConstruction == ParseForestConstruction.Optimized)
            this.reducer = new ReducerSkipLayoutAndLexicalAndRejects<ParseForest, ParseNode, Derivation, StackNode>(
                stackManager, parseForestManager);
        else
            this.reducer = new Reducer<ParseForest, ParseNode, Derivation, StackNode>(stackManager, parseForestManager);
    }

    public void doReductions(Parse<ParseForest, StackNode> parse, StackNode stack, IReduce reduce) {
        if(reduce.production().isCompletionOrRecovery())
            return;

        parse.observing.notify(observer -> observer.doReductions(parse, stack, reduce));

        doReductionsHelper(parse, stack, reduce, null);
    }

    private void doLimitedRedutions(Parse<ParseForest, StackNode> parse, StackNode stack, IReduce reduce,
        StackLink<ParseForest, StackNode> throughLink) {
        if(reduce.production().isCompletionOrRecovery())
            return;

        parse.observing.notify(observer -> observer.doLimitedReductions(parse, stack, reduce, throughLink));

        doReductionsHelper(parse, stack, reduce, throughLink);
    }

    protected void doReductionsHelper(Parse<ParseForest, StackNode> parse, StackNode stack, IReduce reduce,
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
    protected void reducer(Parse<ParseForest, StackNode> parse, StackNode stack, IReduce reduce,
        ParseForest[] parseForests) {
        int gotoId = stack.state.getGotoId(reduce.production().id());
        IState gotoState = parseTable.getState(gotoId);

        StackNode activeStackWithGotoState = parse.activeStacks.findWithState(gotoState);

        parse.observing.notify(observer -> observer.reducer(stack, reduce, parseForests, activeStackWithGotoState));

        if(activeStackWithGotoState != null) {
            StackLink<ParseForest, StackNode> directLink = stackManager.findDirectLink(activeStackWithGotoState, stack);

            parse.observing.notify(observer -> observer.directLinkFound(parse, directLink));

            if(directLink != null) {
                reducer.reducerExistingStackWithDirectLink(parse, reduce, directLink, parseForests);
            } else {
                StackLink<ParseForest, StackNode> link = reducer.reducerExistingStackWithoutDirectLink(parse, reduce,
                    activeStackWithGotoState, stack, parseForests);

                for(StackNode activeStack : parse.activeStacks.forLimitedReductions(parse.forActorStacks)) {
                    for(IReduce reduceAction : activeStack.state.getApplicableReduceActions(parse))
                        doLimitedRedutions(parse, activeStack, reduceAction, link);
                }
            }
        } else {
            StackNode newStack = reducer.reducerNoExistingStack(parse, reduce, stack, gotoState, parseForests);

            parse.activeStacks.add(newStack);
            parse.forActorStacks.add(newStack);
        }
    }

}
