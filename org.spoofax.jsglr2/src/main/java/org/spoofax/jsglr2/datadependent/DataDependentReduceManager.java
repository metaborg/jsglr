package org.spoofax.jsglr2.datadependent;

import java.util.Iterator;
import java.util.List;

import org.metaborg.parsetable.IParseTable;
import org.metaborg.parsetable.actions.IReduce;
import org.metaborg.sdf2table.deepconflicts.ContextualProduction;
import org.metaborg.sdf2table.deepconflicts.ContextualSymbol;
import org.metaborg.sdf2table.grammar.IProduction;
import org.metaborg.sdf2table.grammar.Symbol;
import org.metaborg.sdf2table.parsetable.ParseTableProduction;
import org.spoofax.jsglr2.parseforest.AbstractParseForest;
import org.spoofax.jsglr2.parseforest.ParseForestConstruction;
import org.spoofax.jsglr2.parseforest.ParseForestManager;
import org.spoofax.jsglr2.parser.Parse;
import org.spoofax.jsglr2.reducing.ReduceManager;
import org.spoofax.jsglr2.stack.AbstractStackNode;
import org.spoofax.jsglr2.stack.StackLink;
import org.spoofax.jsglr2.stack.StackManager;
import org.spoofax.jsglr2.stack.paths.StackPath;

public class DataDependentReduceManager<ParseForest extends AbstractParseForest, ParseNode extends ParseForest, Derivation, StackNode extends AbstractStackNode<ParseForest>>
    extends ReduceManager<ParseForest, ParseNode, Derivation, StackNode> {
   

    public DataDependentReduceManager(IParseTable parseTable, StackManager<ParseForest, StackNode> stackManager,
        ParseForestManager<ParseForest, ParseNode, Derivation> parseForestManager,
        ParseForestConstruction parseForestConstruction) {
        super(parseTable, stackManager, parseForestManager, parseForestConstruction);
    }    

    @Override
    protected void doReductionsHelper(Parse<ParseForest, StackNode> parse, StackNode stack, IReduce reduce,
        StackLink<ParseForest, StackNode> throughLink) {
        for(StackPath<ParseForest, StackNode> path : stackManager.findAllPathsOfLength(stack, reduce.arity())) {
            if(throughLink == null || path.contains(throughLink)) {
                StackNode pathBegin = path.head();
                ParseForest[] parseNodes = stackManager.getParseForests(parseForestManager, path);
                boolean skipReduce = false;

                final IProduction production = ((ParseTableProduction) reduce.production()).getProduction();

                if(production instanceof ContextualProduction) {
                    final List<Symbol> rightHandSymbols = production.rightHand();

                    for(int i = 0; i < rightHandSymbols.size(); i++) {
                        final ParseForest nextParseForest = parseNodes[i];
                        final Symbol nextSymbol = rightHandSymbols.get(i);

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

    private static final <ParseForest extends AbstractParseForest> boolean checkContexts(ParseForest pf,
        Symbol symbol) {
        final ContextualSymbol contextualSymbol = (ContextualSymbol) symbol;

        final long contextBitmap = contextualSymbol.deepContexts();

        if(contextBitmap == 0) {
            return false;
        }

        assert pf instanceof DataDependentSymbolNode;
        final List<DataDependentRuleNode> derivations = ((DataDependentSymbolNode) pf).getDerivations();

        if(derivations.size() == 1) {
            final DataDependentRuleNode ruleNode = derivations.get(0);

            final boolean hasDeepConflict = (ruleNode.getContextBitmap() & contextBitmap) != 0;

            // check if bitmaps intersect
            return hasDeepConflict;
        } else {
            for(Iterator<DataDependentRuleNode> iterator = derivations.iterator(); iterator.hasNext();) {
                final DataDependentRuleNode ruleNode = iterator.next();

                final boolean hasDeepConflict = (ruleNode.getContextBitmap() & contextBitmap) != 0;

                // discard rule nodes where bitmaps intersect
                if(hasDeepConflict) {
                    iterator.remove();
                }
            }
            return derivations.isEmpty();
        }
    }

}
