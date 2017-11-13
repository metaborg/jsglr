package org.spoofax.jsglr2.reducing;

import org.spoofax.jsglr2.JSGLR2Variants.ParseForestConstruction;
import org.spoofax.jsglr2.actions.IReduce;
import org.spoofax.jsglr2.parseforest.AbstractParseForest;
import org.spoofax.jsglr2.parseforest.ParseForestManager;
import org.spoofax.jsglr2.parser.Parse;
import org.spoofax.jsglr2.parsetable.IParseTable;
import org.spoofax.jsglr2.parsetable.IState;
import org.spoofax.jsglr2.stack.StackLink;
import org.spoofax.jsglr2.stack.StackPath;
import org.spoofax.jsglr2.stack.elkhound.AbstractElkhoundStackManager;
import org.spoofax.jsglr2.stack.elkhound.AbstractElkhoundStackNode;
import org.spoofax.jsglr2.stack.elkhound.DeterministicStackPath;

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

                int gotoId = pathBegin.state.getGotoId(reduce.production().productionNumber());
                IState gotoState = parseTable.getState(gotoId);

                if(parse.activeStacks.size() == 1)
                    // Do standard LR if there is only 1 active stack
                    reducerElkhound(parse, pathBegin, gotoState, reduce, deterministicPath.parseForests);
                else
                    // Benefit from faster path retrieval, but still do extra checks since there are other active stacks
                    reducer(parse, pathBegin, gotoState, reduce, deterministicPath.parseForests);
            }
        } else {
            // Fall back to regular GLR
            for(StackPath<AbstractElkhoundStackNode<ParseForest>, ParseForest> path : stackManager
                .findAllPathsOfLength(stack, reduce.arity()))
                if(throughLink == null || path.contains(throughLink)) {
                    AbstractElkhoundStackNode<ParseForest> pathBegin = path.head();

                    int gotoId = pathBegin.state.getGotoId(reduce.production().productionNumber());
                    IState gotoState = parseTable.getState(gotoId);

                    reducer(parse, pathBegin, gotoState, reduce,
                        stackManager.getParseForests(parseForestManager, path));
                }
        }
    }

    private void reducerElkhound(Parse<AbstractElkhoundStackNode<ParseForest>, ParseForest> parse,
        AbstractElkhoundStackNode<ParseForest> stack, IState gotoState, IReduce reduce, ParseForest[] parseForests) {
        parse.notify(observer -> observer.reducerElkhound(reduce, parseForests));

        AbstractElkhoundStackNode<ParseForest> newStack =
            reducer.reducerNoExistingStack(parse, reduce, stack, gotoState, parseForests);

        parse.activeStacks.add(newStack);
        parse.forActor.add(newStack);
    }

}
