package org.spoofax.jsglr2.datadependent;

import java.util.Iterator;
import java.util.List;

import org.metaborg.parsetable.IParseTable;
import org.metaborg.parsetable.actions.IReduce;
import org.metaborg.sdf2table.grammar.IProduction;
import org.metaborg.sdf2table.grammar.ISymbol;
import org.metaborg.sdf2table.deepconflicts.ContextualProduction;
import org.metaborg.sdf2table.deepconflicts.ContextualSymbol;
import org.metaborg.sdf2table.parsetable.ParseTableProduction;
import org.spoofax.jsglr2.parseforest.IDerivation;
import org.spoofax.jsglr2.parseforest.IParseForest;
import org.spoofax.jsglr2.parseforest.ParseForestConstruction;
import org.spoofax.jsglr2.parseforest.ParseForestManager;
import org.spoofax.jsglr2.parser.AbstractParse;
import org.spoofax.jsglr2.reducing.ReduceManager;
import org.spoofax.jsglr2.stack.AbstractStackManager;
import org.spoofax.jsglr2.stack.IStackNode;
import org.spoofax.jsglr2.stack.StackLink;
import org.spoofax.jsglr2.stack.paths.StackPath;

public class DataDependentReduceManager
//@formatter:off
   <ParseForest extends IParseForest,
    ParseNode   extends ParseForest,
    Derivation  extends IDerivation<ParseForest>,
    StackNode   extends IStackNode,
    Parse       extends AbstractParse<ParseForest, StackNode>>
//@formatter:on
    extends ReduceManager<ParseForest, ParseNode, Derivation, StackNode, Parse> {


    public DataDependentReduceManager(IParseTable parseTable,
        AbstractStackManager<ParseForest, StackNode, Parse> stackManager,
        ParseForestManager<ParseForest, ParseNode, Derivation> parseForestManager,
        ParseForestConstruction parseForestConstruction) {
        super(parseTable, stackManager, parseForestManager, parseForestConstruction);
    }

    @Override protected void doReductionsHelper(Parse parse, StackNode stack, IReduce reduce,
        StackLink<ParseForest, StackNode> throughLink) {
        for(StackPath<ParseForest, StackNode> path : stackManager.findAllPathsOfLength(stack, reduce.arity())) {
            if(throughLink == null || path.contains(throughLink)) {
                StackNode pathBegin = path.head();
                ParseForest[] parseNodes = stackManager.getParseForests(parseForestManager, path);
                boolean skipReduce = false;

                final IProduction production = ((ParseTableProduction) reduce.production()).getProduction();

                if(production instanceof ContextualProduction) {
                    final List<ISymbol> rightHandSymbols = production.rightHand();

                    for(int i = 0; i < rightHandSymbols.size(); i++) {
                        final ParseForest nextParseForest = parseNodes[i];
                        final ISymbol nextSymbol = rightHandSymbols.get(i);

                        if(nextSymbol instanceof ContextualSymbol && checkContexts(nextParseForest, nextSymbol)) {
                            skipReduce = true; // prohibit reduction
                        }
                    }
                }

                if(skipReduce) {
                    continue;
                }

                reducer(parse, pathBegin, reduce, parseNodes);
            }
        }
    }

    private static <ParseForest extends IParseForest> boolean checkContexts(ParseForest pf, ISymbol symbol) {
        final ContextualSymbol contextualSymbol = (ContextualSymbol) symbol;

        final long contextBitmap = contextualSymbol.deepContexts();

        if(contextBitmap == 0) {
            return false;
        }

        assert pf instanceof DataDependentParseNode;
        final List<DataDependentDerivation> derivations = ((DataDependentParseNode) pf).getDerivations();

        if(derivations.size() == 1) {
            final DataDependentDerivation derivation = derivations.get(0);

            final boolean hasDeepConflict = (derivation.getContextBitmap() & contextBitmap) != 0;

            // check if bitmaps intersect
            return hasDeepConflict;
        } else {
            for(Iterator<DataDependentDerivation> iterator = derivations.iterator(); iterator.hasNext();) {
                final DataDependentDerivation derivation = iterator.next();

                final boolean hasDeepConflict = (derivation.getContextBitmap() & contextBitmap) != 0;

                // discard rule nodes where bitmaps intersect
                if(hasDeepConflict) {
                    iterator.remove();
                }
            }
            return derivations.isEmpty();
        }
    }

}
