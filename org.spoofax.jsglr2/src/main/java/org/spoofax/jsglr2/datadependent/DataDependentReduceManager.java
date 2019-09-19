package org.spoofax.jsglr2.datadependent;

import org.metaborg.parsetable.IParseTable;
import org.metaborg.parsetable.actions.IReduce;
import org.metaborg.sdf2table.deepconflicts.ContextualProduction;
import org.metaborg.sdf2table.deepconflicts.ContextualSymbol;
import org.metaborg.sdf2table.grammar.IProduction;
import org.metaborg.sdf2table.grammar.ISymbol;
import org.metaborg.sdf2table.parsetable.ParseTableProduction;
import org.spoofax.jsglr2.parseforest.ParseForestConstruction;
import org.spoofax.jsglr2.parseforest.ParseForestManager;
import org.spoofax.jsglr2.parser.AbstractParseState;
import org.spoofax.jsglr2.parser.ParserVariant;
import org.spoofax.jsglr2.reducing.ReduceManager;
import org.spoofax.jsglr2.reducing.ReduceManagerFactory;
import org.spoofax.jsglr2.stack.AbstractStackManager;
import org.spoofax.jsglr2.stack.IStackNode;

import java.util.Iterator;
import java.util.List;

public class DataDependentReduceManager
//@formatter:off
   <ParseForest extends IDataDependentParseForest,
    Derivation  extends IDataDependentDerivation<ParseForest>,
    ParseNode   extends IDataDependentParseNode<ParseForest, Derivation>,
    StackNode   extends IStackNode,
    ParseState  extends AbstractParseState<ParseForest, StackNode>>
//@formatter:on
    extends ReduceManager<ParseForest, Derivation, ParseNode, StackNode, ParseState> {

    public DataDependentReduceManager(IParseTable parseTable,
        AbstractStackManager<ParseForest, Derivation, ParseNode, StackNode, ParseState> stackManager,
        ParseForestManager<ParseForest, Derivation, ParseNode, StackNode, ParseState> parseForestManager,
        ParseForestConstruction parseForestConstruction) {
        super(parseTable, stackManager, parseForestManager, parseForestConstruction);
    }

    public static
    //@formatter:off
       <ParseForest_  extends IDataDependentParseForest,
        Derivation_   extends IDataDependentDerivation<ParseForest_>,
        ParseNode_    extends IDataDependentParseNode<ParseForest_, Derivation_>,
        StackNode_    extends IStackNode,
        ParseState_   extends AbstractParseState<ParseForest_, StackNode_>,
        StackManager_ extends AbstractStackManager<ParseForest_, Derivation_, ParseNode_, StackNode_, ParseState_>>
    //@formatter:on
    ReduceManagerFactory<ParseForest_, Derivation_, ParseNode_, StackNode_, ParseState_, StackManager_, DataDependentReduceManager<ParseForest_, Derivation_, ParseNode_, StackNode_, ParseState_>>
        factoryDataDependent(ParserVariant parserVariant) {
        return (parseTable, stackManager, parseForestManager) -> new DataDependentReduceManager<>(parseTable,
            stackManager, parseForestManager, parserVariant.parseForestConstruction);
    }

    @Override protected boolean ignoreReducer(StackNode pathBegin, IReduce reduce, ParseForest[] parseNodes) {
        final IProduction production = ((ParseTableProduction) reduce.production()).getProduction();

        if(production instanceof ContextualProduction) {
            final List<ISymbol> rightHandSymbols = production.rightHand();

            for(int i = 0; i < rightHandSymbols.size(); i++) {
                final ParseForest nextParseForest = parseNodes[i];
                final ISymbol nextSymbol = rightHandSymbols.get(i);

                if(nextSymbol instanceof ContextualSymbol && checkContexts(nextParseForest, nextSymbol))
                    return true; // prohibit reduction
            }
        }

        return false;
    }

    private static
    //@formatter:off
       <ParseForest_  extends IDataDependentParseForest,
        Derivation_   extends IDataDependentDerivation<ParseForest_>,
        ParseNode_    extends IDataDependentParseNode<ParseForest_, Derivation_>>
    //@formatter:on
    boolean checkContexts(ParseForest_ parseForest, ISymbol symbol) {
        final ContextualSymbol contextualSymbol = (ContextualSymbol) symbol;

        final long contextBitmap = contextualSymbol.deepContexts();

        if(contextBitmap == 0) {
            return false;
        }

        assert parseForest instanceof IDataDependentParseNode;

        final List<Derivation_> derivations = ((ParseNode_) parseForest).getDerivations();

        if(derivations.size() == 1) {
            final Derivation_ derivation = derivations.get(0);

            final boolean hasDeepConflict = (derivation.getContextBitmap() & contextBitmap) != 0;

            // check if bitmaps intersect
            return hasDeepConflict;
        } else {
            for(Iterator<Derivation_> iterator = derivations.iterator(); iterator.hasNext();) {
                final Derivation_ derivation = iterator.next();

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
