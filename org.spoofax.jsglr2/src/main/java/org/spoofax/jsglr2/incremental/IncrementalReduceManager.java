package org.spoofax.jsglr2.incremental;

import org.metaborg.parsetable.IParseTable;
import org.metaborg.parsetable.actions.IReduce;
import org.spoofax.jsglr2.incremental.parseforest.IncrementalParseForest;
import org.spoofax.jsglr2.parseforest.IDerivation;
import org.spoofax.jsglr2.parseforest.IParseNode;
import org.spoofax.jsglr2.parseforest.ParseForestConstruction;
import org.spoofax.jsglr2.parseforest.ParseForestManager;
import org.spoofax.jsglr2.parser.AbstractParseState;
import org.spoofax.jsglr2.parser.ParserVariant;
import org.spoofax.jsglr2.parser.observing.ParserObserving;
import org.spoofax.jsglr2.reducing.ReduceManager;
import org.spoofax.jsglr2.reducing.ReduceManagerFactory;
import org.spoofax.jsglr2.stack.AbstractStackManager;
import org.spoofax.jsglr2.stack.IStackNode;
import org.spoofax.jsglr2.stack.StackLink;
import org.spoofax.jsglr2.stack.paths.StackPath;

import java.util.List;
import java.util.stream.Collectors;

public class IncrementalReduceManager
//@formatter:off
   <ParseForest extends IncrementalParseForest,
    Derivation  extends IDerivation<ParseForest>,
    ParseNode   extends IParseNode<ParseForest, Derivation>,
    StackNode   extends IStackNode,
    ParseState  extends AbstractParseState<ParseForest, StackNode> & IIncrementalParseState>
//@formatter:on
    extends ReduceManager<ParseForest, Derivation, ParseNode, StackNode, ParseState> {

    public IncrementalReduceManager(IParseTable parseTable,
        AbstractStackManager<ParseForest, Derivation, ParseNode, StackNode, ParseState> stackManager,
        ParseForestManager<ParseForest, Derivation, ParseNode, StackNode, ParseState> parseForestManager,
        ParseForestConstruction parseForestConstruction) {
        super(parseTable, stackManager, parseForestManager, parseForestConstruction);
    }

    public static
    //@formatter:off
       <ParseForest_  extends IncrementalParseForest,
        Derivation_   extends IDerivation<ParseForest_>,
        ParseNode_    extends IParseNode<ParseForest_, Derivation_>,
        StackNode_    extends IStackNode,
        ParseState_   extends AbstractParseState<ParseForest_, StackNode_> & IIncrementalParseState,
        StackManager_ extends AbstractStackManager<ParseForest_, Derivation_, ParseNode_, StackNode_, ParseState_>>
    //@formatter:on
    ReduceManagerFactory<ParseForest_, Derivation_, ParseNode_, StackNode_, ParseState_, StackManager_, IncrementalReduceManager<ParseForest_, Derivation_, ParseNode_, StackNode_, ParseState_>>
        factoryIncremental(ParserVariant parserVariant) {
        return (parseTable, stackManager, parseForestManager) -> new IncrementalReduceManager<>(parseTable,
            stackManager, parseForestManager, parserVariant.parseForestConstruction);
    }

    @Override protected void doReductionsHelper(
        ParserObserving<ParseForest, Derivation, ParseNode, StackNode, ParseState> observing, ParseState parseState,
        StackNode stack, IReduce reduce, StackLink<ParseForest, StackNode> throughLink) {

        List<StackPath<ParseForest, StackNode>> paths = stackManager.findAllPathsOfLength(stack, reduce.arity());

        if(throughLink != null)
            paths = paths.stream().filter(path -> path.contains(throughLink)).collect(Collectors.toList());

        if(paths.size() > 1)
            parseState.setMultipleStates(true);

        for(StackPath<ParseForest, StackNode> path : paths) {
            StackNode pathBegin = path.head();
            ParseForest[] parseNodes = stackManager.getParseForests(parseForestManager, path);

            reducer(observing, parseState, pathBegin, reduce, parseNodes);
        }
    }
}
