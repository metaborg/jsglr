package org.spoofax.jsglr2.elkhound;

import org.metaborg.parsetable.IParseTable;
import org.metaborg.parsetable.actions.IReduce;
import org.metaborg.parsetable.states.IState;
import org.spoofax.jsglr2.parseforest.IDerivation;
import org.spoofax.jsglr2.parseforest.IParseForest;
import org.spoofax.jsglr2.parseforest.ParseForestConstruction;
import org.spoofax.jsglr2.parseforest.ParseForestManager;
import org.spoofax.jsglr2.parser.AbstractParseState;
import org.spoofax.jsglr2.parser.ParserVariant;
import org.spoofax.jsglr2.parser.observing.ParserObserving;
import org.spoofax.jsglr2.reducing.ReduceManager;
import org.spoofax.jsglr2.reducing.ReduceManagerFactory;
import org.spoofax.jsglr2.stack.StackLink;
import org.spoofax.jsglr2.stack.paths.StackPath;

public class ElkhoundReduceManager
//@formatter:off
   <ParseForest       extends IParseForest,
    ParseNode         extends ParseForest,
    Derivation        extends IDerivation<ParseForest>,
    ElkhoundStackNode extends AbstractElkhoundStackNode<ParseForest>,
    ParseState        extends AbstractParseState<ParseForest, ElkhoundStackNode>>
//@formatter:on
    extends ReduceManager<ParseForest, ParseNode, Derivation, ElkhoundStackNode, ParseState> {

    protected final ElkhoundStackManager<ParseForest, ElkhoundStackNode, ParseState> stackManager;

    public ElkhoundReduceManager(IParseTable parseTable,
        ElkhoundStackManager<ParseForest, ElkhoundStackNode, ParseState> stackManager,
        ParseForestManager<ParseForest, ParseNode, Derivation, ElkhoundStackNode, ParseState> parseForestManager,
        ParseForestConstruction parseForestConstruction) {
        super(parseTable, stackManager, parseForestManager, parseForestConstruction);

        this.stackManager = stackManager;
    }

    public static
    //@formatter:off
       <ParseForest_  extends IParseForest,
        ParseNode_    extends ParseForest_,
        Derivation_   extends IDerivation<ParseForest_>,
        StackNode_    extends AbstractElkhoundStackNode<ParseForest_>,
        ParseState_   extends AbstractParseState<ParseForest_, StackNode_>,
        StackManager_ extends ElkhoundStackManager<ParseForest_, StackNode_, ParseState_>>
    //@formatter:on
    ReduceManagerFactory<ParseForest_, ParseNode_, Derivation_, StackNode_, ParseState_, StackManager_, ElkhoundReduceManager<ParseForest_, ParseNode_, Derivation_, StackNode_, ParseState_>>
        factoryElkhound(ParserVariant parserVariant) {
        return (parseTable, stackManager, parseForestManager) -> new ElkhoundReduceManager<>(parseTable, stackManager,
            parseForestManager, parserVariant.parseForestConstruction);
    }

    @Override protected void doReductionsHelper(ParserObserving<ParseForest, ElkhoundStackNode, ParseState> observing,
        ParseState parseState, ElkhoundStackNode stack, IReduce reduce,
        StackLink<ParseForest, ElkhoundStackNode> throughLink) {
        if(stack.deterministicDepth >= reduce.arity()) {
            DeterministicStackPath<ParseForest, ElkhoundStackNode> deterministicPath =
                stackManager.findDeterministicPathOfLength(parseForestManager, stack, reduce.arity());

            if(throughLink == null || deterministicPath.contains(throughLink)) {
                ElkhoundStackNode pathBegin = deterministicPath.head();

                if(parseState.activeStacks.isEmpty())
                    // Do LR if there are no other active stacks (the stack on which the current reduction is applied is
                    // removed from the activeStacks collection in ElkhoundParser)
                    reducerElkhound(observing, parseState, pathBegin, reduce, deterministicPath.parseForests);
                else
                    // Benefit from faster path retrieval, but still do regular (S)GLR reducing since there are other
                    // active stacks
                    reducer(observing, parseState, pathBegin, reduce, deterministicPath.parseForests);
            }
        } else {
            // Fall back to regular (S)GLR
            for(StackPath<ParseForest, ElkhoundStackNode> path : stackManager.findAllPathsOfLength(stack,
                reduce.arity()))
                if(throughLink == null || path.contains(throughLink)) {
                    ElkhoundStackNode pathBegin = path.head();

                    reducer(observing, parseState, pathBegin, reduce,
                        stackManager.getParseForests(parseForestManager, path));
                }
        }
    }

    private void reducerElkhound(ParserObserving<ParseForest, ElkhoundStackNode, ParseState> observing,
        ParseState parseState, ElkhoundStackNode stack, IReduce reduce, ParseForest[] parseForests) {
        int gotoId = stack.state.getGotoId(reduce.production().id());
        IState gotoState = parseTable.getState(gotoId);

        observing.notify(observer -> observer.reducerElkhound(stack, reduce, parseForests));

        ElkhoundStackNode newStack =
            reducer.reducerNoExistingStack(observing, parseState, reduce, stack, gotoState, parseForests);

        parseState.activeStacks.add(newStack);
        // We are doing LR and the new active stack is the only one, thus no need to add it to forActorStacks here for
        // further processing
    }

}
