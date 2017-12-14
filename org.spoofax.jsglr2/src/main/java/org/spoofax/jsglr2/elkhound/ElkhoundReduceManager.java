package org.spoofax.jsglr2.elkhound;

import org.spoofax.jsglr2.actions.IReduce;
import org.spoofax.jsglr2.parseforest.AbstractParseForest;
import org.spoofax.jsglr2.parseforest.ParseForestConstruction;
import org.spoofax.jsglr2.parseforest.ParseForestManager;
import org.spoofax.jsglr2.parser.Parse;
import org.spoofax.jsglr2.parsetable.IParseTable;
import org.spoofax.jsglr2.reducing.ReduceManager;
import org.spoofax.jsglr2.stack.StackLink;
import org.spoofax.jsglr2.stack.paths.StackPath;
import org.spoofax.jsglr2.states.IState;

public class ElkhoundReduceManager<ParseForest extends AbstractParseForest, ParseNode extends ParseForest, Derivation>
    extends ReduceManager<ParseForest, ParseNode, Derivation, AbstractElkhoundStackNode<ParseForest>> {

    protected final AbstractElkhoundStackManager<ParseForest, AbstractElkhoundStackNode<ParseForest>> stackManager;

    public ElkhoundReduceManager(IParseTable parseTable,
        AbstractElkhoundStackManager<ParseForest, AbstractElkhoundStackNode<ParseForest>> stackManager,
        ParseForestManager<ParseForest, ParseNode, Derivation> parseForestManager,
        ParseForestConstruction parseForestConstruction) {
        super(parseTable, stackManager, parseForestManager, parseForestConstruction);

        this.stackManager = stackManager;
    }

    @Override
    protected void doReductionsHelper(Parse<ParseForest, AbstractElkhoundStackNode<ParseForest>> parse,
        AbstractElkhoundStackNode<ParseForest> stack, IReduce reduce,
        StackLink<ParseForest, AbstractElkhoundStackNode<ParseForest>> throughLink) {
        if(stack.deterministicDepth >= reduce.arity()) {
            DeterministicStackPath<ParseForest, AbstractElkhoundStackNode<ParseForest>> deterministicPath =
                stackManager.findDeterministicPathOfLength(parseForestManager, stack, reduce.arity());

            if(throughLink == null || deterministicPath.contains(throughLink)) {
                AbstractElkhoundStackNode<ParseForest> pathBegin = deterministicPath.head();

                if(parse.activeStacks.isSingle())
                    // Do standard LR if there is only 1 active stack
                    reducerElkhound(parse, pathBegin, reduce, deterministicPath.parseForests);
                else
                    // Benefit from faster path retrieval, but still do extra checks since there are other active stacks
                    reducer(parse, pathBegin, reduce, deterministicPath.parseForests);
            }
        } else {
            // Fall back to regular GLR
            for(StackPath<ParseForest, AbstractElkhoundStackNode<ParseForest>> path : stackManager
                .findAllPathsOfLength(stack, reduce.arity()))
                if(throughLink == null || path.contains(throughLink)) {
                    AbstractElkhoundStackNode<ParseForest> pathBegin = path.head();

                    reducer(parse, pathBegin, reduce, stackManager.getParseForests(parseForestManager, path));
                }
        }
    }

    private void reducerElkhound(Parse<ParseForest, AbstractElkhoundStackNode<ParseForest>> parse,
        AbstractElkhoundStackNode<ParseForest> stack, IReduce reduce, ParseForest[] parseForests) {
        int gotoId = stack.state.getGotoId(reduce.production().id());
        IState gotoState = parseTable.getState(gotoId);

        parse.observing.notify(observer -> observer.reducerElkhound(stack, reduce, parseForests));

        AbstractElkhoundStackNode<ParseForest> newStack =
            reducer.reducerNoExistingStack(parse, reduce, stack, gotoState, parseForests);

        parse.activeStacks.add(newStack);
        parse.forActorStacks.add(newStack);
    }

}
