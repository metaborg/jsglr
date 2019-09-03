package org.spoofax.jsglr2.incremental;

import java.util.List;
import java.util.stream.Collectors;

import org.metaborg.parsetable.IParseTable;
import org.metaborg.parsetable.actions.IReduce;
import org.spoofax.jsglr2.incremental.parseforest.IncrementalParseForest;
import org.spoofax.jsglr2.parseforest.IDerivation;
import org.spoofax.jsglr2.parseforest.ParseForestConstruction;
import org.spoofax.jsglr2.parseforest.ParseForestManager;
import org.spoofax.jsglr2.parser.AbstractParse;
import org.spoofax.jsglr2.parser.IParseState;
import org.spoofax.jsglr2.reducing.ReduceManager;
import org.spoofax.jsglr2.stack.AbstractStackManager;
import org.spoofax.jsglr2.stack.IStackNode;
import org.spoofax.jsglr2.stack.StackLink;
import org.spoofax.jsglr2.stack.paths.StackPath;

public class IncrementalReduceManager
//@formatter:off
   <ParseForest extends IncrementalParseForest,
    ParseNode   extends ParseForest,
    Derivation  extends IDerivation<ParseForest>,
    StackNode   extends IStackNode,
    ParseState  extends IParseState<ParseForest, StackNode>,
    Parse       extends AbstractParse<ParseForest, StackNode, ParseState> & IIncrementalParse>
//@formatter:on
    extends ReduceManager<ParseForest, ParseNode, Derivation, StackNode, ParseState, Parse> {

    public IncrementalReduceManager(IParseTable parseTable,
        AbstractStackManager<ParseForest, StackNode, ParseState, Parse> stackManager,
        ParseForestManager<ParseForest, ParseNode, Derivation, Parse> parseForestManager,
        ParseForestConstruction parseForestConstruction) {
        super(parseTable, stackManager, parseForestManager, parseForestConstruction);
    }

    @Override protected void doReductionsHelper(Parse parse, StackNode stack, IReduce reduce,
        StackLink<ParseForest, StackNode> throughLink) {

        List<StackPath<ParseForest, StackNode>> paths = stackManager.findAllPathsOfLength(stack, reduce.arity());

        if(throughLink != null)
            paths = paths.stream().filter(path -> path.contains(throughLink)).collect(Collectors.toList());

        if(paths.size() > 1)
            parse.setMultipleStates(true);

        for(StackPath<ParseForest, StackNode> path : paths) {
            StackNode pathBegin = path.head();
            ParseForest[] parseNodes = stackManager.getParseForests(parseForestManager, path);

            reducer(parse, pathBegin, reduce, parseNodes);
        }
    }
}
