package org.spoofax.jsglr2.reducing;

import org.spoofax.jsglr2.JSGLR2Variants.ParseForestConstruction;
import org.spoofax.jsglr2.actions.IReduce;
import org.spoofax.jsglr2.parseforest.AbstractParseForest;
import org.spoofax.jsglr2.parseforest.ParseForestManager;
import org.spoofax.jsglr2.parser.Parse;
import org.spoofax.jsglr2.parsetable.IParseTable;
import org.spoofax.jsglr2.stack.StackLink;
import org.spoofax.jsglr2.stack.StackPath;
import org.spoofax.jsglr2.stack.elkhound.AbstractElkhoundStackManager;
import org.spoofax.jsglr2.stack.elkhound.AbstractElkhoundStackNode;
import org.spoofax.jsglr2.stack.elkhound.DeterministicStackPath;
import org.spoofax.jsglr2.states.IState;

public class ReduceManagerElkhound<ParseForest extends AbstractParseForest, ParseNode extends ParseForest, Derivation>
    extends ReduceManager<AbstractElkhoundStackNode<ParseForest>, ParseForest, ParseNode, Derivation> {

    protected final AbstractElkhoundStackManager<AbstractElkhoundStackNode<ParseForest>, ParseForest> stackManager;

    public ReduceManagerElkhound(IParseTable parseTable,
        AbstractElkhoundStackManager<AbstractElkhoundStackNode<ParseForest>, ParseForest> stackManager,
        ParseForestManager<ParseForest, ParseNode, Derivation> parseForestManager,
        ParseForestConstruction parseForestConstruction) {
        super(parseTable, stackManager, parseForestManager, parseForestConstruction);

        this.stackManager = stackManager;
    }

    @Override protected void doReductionsHelper(Parse<AbstractElkhoundStackNode<ParseForest>, ParseForest> parse,
        AbstractElkhoundStackNode<ParseForest> stack, IReduce reduce,
        StackLink<AbstractElkhoundStackNode<ParseForest>, ParseForest> throughLink) {
        if(stack.deterministicDepth >= reduce.arity()) {
            DeterministicStackPath<AbstractElkhoundStackNode<ParseForest>, ParseForest> deterministicPath =
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
            for(StackPath<AbstractElkhoundStackNode<ParseForest>, ParseForest> path : stackManager
                .findAllPathsOfLength(stack, reduce.arity()))
                if(throughLink == null || path.contains(throughLink)) {
                    AbstractElkhoundStackNode<ParseForest> pathBegin = path.head();

                    reducer(parse, pathBegin, reduce, stackManager.getParseForests(parseForestManager, path));
                }
        }
    }

    private void reducerElkhound(Parse<AbstractElkhoundStackNode<ParseForest>, ParseForest> parse,
        AbstractElkhoundStackNode<ParseForest> stack, IReduce reduce, ParseForest[] parseForests) {
        int gotoId = stack.state.getGotoId(reduce.production().productionNumber());
        IState gotoState = parseTable.getState(gotoId);

        parse.notify(observer -> observer.reducerElkhound(stack, reduce, parseForests));

        AbstractElkhoundStackNode<ParseForest> newStack =
            reducer.reducerNoExistingStack(parse, reduce, stack, gotoState, parseForests);

        parse.activeStacks.add(newStack);
        parse.forActorStacks.add(newStack);
    }

}
