package org.spoofax.jsglr2.reducing;

import org.spoofax.jsglr2.JSGLR2Variants.ParseForestConstruction;
import org.spoofax.jsglr2.actions.IReduce;
import org.spoofax.jsglr2.parseforest.AbstractParseForest;
import org.spoofax.jsglr2.parseforest.ParseForestManager;
import org.spoofax.jsglr2.parser.Parse;
import org.spoofax.jsglr2.parsetable.IParseTable;
import org.spoofax.jsglr2.parsetable.IState;
import org.spoofax.jsglr2.stack.AbstractStackNode;
import org.spoofax.jsglr2.stack.StackLink;
import org.spoofax.jsglr2.stack.StackManager;
import org.spoofax.jsglr2.stack.StackPath;

public class ReduceManager<StackNode extends AbstractStackNode<ParseForest>, ParseForest extends AbstractParseForest, ParseNode extends ParseForest, Derivation> {

    protected final IParseTable parseTable;
    protected final StackManager<StackNode, ParseForest> stackManager;
    protected final ParseForestManager<ParseForest, ParseNode, Derivation> parseForestManager;
    protected final Reducer<StackNode, ParseForest, ParseNode, Derivation> reducer;

    public ReduceManager(IParseTable parseTable, StackManager<StackNode, ParseForest> stackManager,
        ParseForestManager<ParseForest, ParseNode, Derivation> parseForestManager,
        ParseForestConstruction parseForestConstruction) {
        this.parseTable = parseTable;
        this.stackManager = stackManager;
        this.parseForestManager = parseForestManager;

        if(parseForestConstruction == ParseForestConstruction.Optimized)
            this.reducer = new ReducerSkipLayoutAndLexicalAndRejects<StackNode, ParseForest, ParseNode, Derivation>(
                stackManager, parseForestManager);
        else
            this.reducer = new Reducer<StackNode, ParseForest, ParseNode, Derivation>(stackManager, parseForestManager);
    }

    public void doReductions(Parse<StackNode, ParseForest> parse, StackNode stack, IReduce reduce) {
        if(reduce.production().isCompletionOrRecovery())
            return;

        parse.notify(observer -> observer.doReductions(parse, stack, reduce));

        doReductionsHelper(parse, stack, reduce, null);
    }

    private void doLimitedRedutions(Parse<StackNode, ParseForest> parse, StackNode stack, IReduce reduce,
        StackLink<StackNode, ParseForest> throughLink) {
        if(reduce.production().isCompletionOrRecovery())
            return;

        parse.notify(observer -> observer.doLimitedReductions(parse, stack, reduce, throughLink));

        doReductionsHelper(parse, stack, reduce, throughLink);
    }

    protected void doReductionsHelper(Parse<StackNode, ParseForest> parse, StackNode stack, IReduce reduce,
        StackLink<StackNode, ParseForest> throughLink) {
        for(StackPath<StackNode, ParseForest> path : stackManager.findAllPathsOfLength(stack, reduce.arity())) {
            if(throughLink == null || path.contains(throughLink)) {
                ParseForest[] parseNodes = stackManager.getParseForests(parseForestManager, path);
                StackNode pathBegin = path.head();

                reducer(parse, pathBegin, reduce, stackManager.getParseForests(parseForestManager, path));
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
    protected void reducer(Parse<StackNode, ParseForest> parse, StackNode stack, IReduce reduce,
        ParseForest[] parseForests) {
        int gotoId = stack.state.getGotoId(reduce.production().productionNumber());
        IState gotoState = parseTable.getState(gotoId);

        StackNode activeStackWithGotoState = parse.activeStacks.findWithState(gotoState);

        parse.notify(observer -> observer.reducer(stack, reduce, parseForests, activeStackWithGotoState));

        if(activeStackWithGotoState != null) {
            StackLink<StackNode, ParseForest> directLink = stackManager.findDirectLink(activeStackWithGotoState, stack);

            parse.notify(observer -> observer.directLinkFound(directLink));

            if(directLink != null) {
                reducer.reducerExistingStackWithDirectLink(parse, reduce, directLink, parseForests);
            } else {
                StackLink<StackNode, ParseForest> link = reducer.reducerExistingStackWithoutDirectLink(parse, reduce,
                    activeStackWithGotoState, stack, parseForests);

                // Save the number of active stacks to prevent the for loop from processing active stacks that are added
                // by doLimitedReductions.
                // We can safely limit the loop by the current number of stacks since new stack are added at the end.
                int size = parse.activeStacks.size();

                for(int i = 0; i < size; i++) {
                    StackNode activeStack = parse.activeStacks.get(i);

                    if(!activeStack.allOutLinksRejected() && !parse.forActor.contains(activeStack)
                        && !parse.forActorDelayed.contains(activeStack))
                        for(IReduce reduceAction : activeStack.state.applicableReduceActions(parse))
                            doLimitedRedutions(parse, activeStack, reduceAction, link);
                }
            }
        } else {
            StackNode newStack = reducer.reducerNoExistingStack(parse, reduce, stack, gotoState, parseForests);

            parse.activeStacks.add(newStack);

            if(newStack.state.isRejectable())
                parse.forActorDelayed.add(newStack);
            else
                parse.forActor.add(newStack);
        }
    }

}
