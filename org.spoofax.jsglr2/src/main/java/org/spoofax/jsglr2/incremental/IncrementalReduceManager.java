package org.spoofax.jsglr2.incremental;

import java.util.List;
import java.util.stream.Collectors;

import org.metaborg.parsetable.IParseTable;
import org.metaborg.parsetable.actions.IReduce;
import org.spoofax.jsglr2.incremental.parseforest.IncrementalParseForest;
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
    ParseState  extends AbstractParseState<?, StackNode> & IIncrementalParseState>
//@formatter:on
    extends ReduceManager<ParseForest, Derivation, ParseNode, StackNode, ParseState> {

    public IncrementalReduceManager(IParseTable parseTable,
        AbstractStackManager<ParseForest, Derivation, ParseNode, StackNode, ParseState> stackManager,
        ParseForestManager<ParseForest, Derivation, ParseNode, StackNode, ParseState> parseForestManager,
        ReducerFactory<ParseForest, Derivation, ParseNode, StackNode, ParseState> reducerFactory) {
        super(parseTable, stackManager, parseForestManager, reducerFactory);
    }

    public static
    //@formatter:off
       <ParseForest_  extends IncrementalParseForest,
        Derivation_   extends IDerivation<ParseForest_>,
        ParseNode_    extends IParseNode<ParseForest_, Derivation_>,
        StackNode_    extends IStackNode,
        ParseState_   extends AbstractParseState<?, StackNode_> & IIncrementalParseState,
        StackManager_ extends AbstractStackManager<ParseForest_, Derivation_, ParseNode_, StackNode_, ParseState_>>
    //@formatter:on
    ReduceManagerFactory<ParseForest_, Derivation_, ParseNode_, StackNode_, ParseState_, StackManager_, IncrementalReduceManager<ParseForest_, Derivation_, ParseNode_, StackNode_, ParseState_>>
        factoryIncremental(
            ReducerFactory<ParseForest_, Derivation_, ParseNode_, StackNode_, ParseState_> reducerFactory) {
        return (parseTable, stackManager, parseForestManager) -> new IncrementalReduceManager<>(parseTable,
            stackManager, parseForestManager, reducerFactory);
    }

    @Override protected void doReductionsHelper(
        ParserObserving<ParseForest, Derivation, ParseNode, StackNode, ParseState> observing, ParseState parseState,
        StackNode activeStack, IReduce reduce, StackLink<ParseForest, StackNode> throughLink) {

        List<StackPath<ParseForest, StackNode>> paths = stackManager.findAllPathsOfLength(activeStack, reduce.arity());

        if(throughLink != null)
            paths = paths.stream().filter(path -> path.contains(throughLink)).collect(Collectors.toList());

        if(paths.size() > 1)
            parseState.setMultipleStates(true);

        for(StackPath<ParseForest, StackNode> path : paths) {
            StackNode originStack = path.head();
            ParseForest[] parseNodes = stackManager.getParseForests(parseForestManager, path);

            if(!ignoreReducePath(originStack, reduce, parseNodes))
                reducer(observing, parseState, activeStack, originStack, reduce, parseNodes);
        }
    }
}
