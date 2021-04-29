package org.spoofax.jsglr2.incremental;

import java.util.List;

import org.metaborg.parsetable.IParseTable;
import org.metaborg.parsetable.actions.IReduce;
import org.spoofax.jsglr2.incremental.parseforest.IncrementalParseForest;
import org.spoofax.jsglr2.inputstack.IInputStack;
import org.spoofax.jsglr2.parseforest.IDerivation;
import org.spoofax.jsglr2.parseforest.IParseNode;
import org.spoofax.jsglr2.parseforest.ParseForestManager;
import org.spoofax.jsglr2.parser.AbstractParseState;
import org.spoofax.jsglr2.parser.observing.ParserObserving;
import org.spoofax.jsglr2.reducing.ReduceManager;
import org.spoofax.jsglr2.reducing.ReduceManagerFactory;
import org.spoofax.jsglr2.reducing.ReducerFactory;
import org.spoofax.jsglr2.stack.AbstractStackManager;
import org.spoofax.jsglr2.stack.IStackNode;
import org.spoofax.jsglr2.stack.StackLink;
import org.spoofax.jsglr2.stack.paths.StackPath;

public class IncrementalReduceManager
//@formatter:off
   <ParseForest extends IncrementalParseForest,
    Derivation  extends IDerivation<ParseForest>,
    ParseNode   extends IParseNode<ParseForest, Derivation>,
    StackNode   extends IStackNode,
    InputStack  extends IInputStack,
    ParseState  extends AbstractParseState<InputStack, StackNode> & IIncrementalParseState>
//@formatter:on
    extends ReduceManager<ParseForest, Derivation, ParseNode, StackNode, InputStack, ParseState> {

    public IncrementalReduceManager(IParseTable parseTable,
        AbstractStackManager<ParseForest, Derivation, ParseNode, StackNode, ParseState> stackManager,
        ParseForestManager<ParseForest, Derivation, ParseNode, StackNode, ParseState> parseForestManager,
        ReducerFactory<ParseForest, Derivation, ParseNode, StackNode, InputStack, ParseState> reducerFactory) {
        super(parseTable, stackManager, parseForestManager, reducerFactory);
    }

    public static
    //@formatter:off
       <ParseForest_  extends IncrementalParseForest,
        Derivation_   extends IDerivation<ParseForest_>,
        ParseNode_    extends IParseNode<ParseForest_, Derivation_>,
        StackNode_    extends IStackNode,
        InputStack_   extends IInputStack,
        ParseState_   extends AbstractParseState<InputStack_, StackNode_> & IIncrementalParseState,
        StackManager_ extends AbstractStackManager<ParseForest_, Derivation_, ParseNode_, StackNode_, ParseState_>>
    //@formatter:on
    ReduceManagerFactory<ParseForest_, Derivation_, ParseNode_, StackNode_, InputStack_, ParseState_, StackManager_, IncrementalReduceManager<ParseForest_, Derivation_, ParseNode_, StackNode_, InputStack_, ParseState_>>
        factoryIncremental(
            ReducerFactory<ParseForest_, Derivation_, ParseNode_, StackNode_, InputStack_, ParseState_> reducerFactory) {
        return (parseTable, stackManager, parseForestManager) -> new IncrementalReduceManager<>(parseTable,
            stackManager, parseForestManager, reducerFactory);
    }

    @Override protected void doReductionsHelper(
        ParserObserving<ParseForest, Derivation, ParseNode, StackNode, ParseState> observing, ParseState parseState,
        StackNode activeStack, IReduce reduce, StackLink<ParseForest, StackNode> throughLink) {

        List<StackPath<ParseForest, StackNode>> paths = stackManager.findAllPathsOfLength(activeStack, reduce.arity());

        if(throughLink != null)
            paths.removeIf(path -> !path.contains(throughLink));

        if(reduceResultsInMultipleStates(paths, reduce.production().id()))
            parseState.setMultipleStates(true);

        for(StackPath<ParseForest, StackNode> path : paths) {
            StackNode originStack = path.head();
            ParseForest[] parseNodes = stackManager.getParseForests(parseForestManager, path);

            if(!ignoreReducePath(originStack, reduce, parseNodes))
                reducer(observing, parseState, activeStack, originStack, reduce, parseNodes);
        }
    }

    private boolean reduceResultsInMultipleStates(List<StackPath<ParseForest, StackNode>> paths, int productionId) {
        // If the number of reduce paths is not more than 1, the reduce does not result in multipleStates.
        if(paths.size() <= 1)
            return false;

        // If multiple reduction paths lead to different goto states, then multipleStates should become true.
        int commonGotoId = paths.get(0).head().state().getGotoId(productionId);
        for(StackPath<ParseForest, StackNode> path : paths.subList(1, paths.size())) {
            if(path.head().state().getGotoId(productionId) != commonGotoId)
                return true;
        }

        // If multiple reduction paths lead all to the same goto state, then multipleStates does not need to change.
        return false;
    }
}
