package org.spoofax.jsglr2.datadependent;

import java.util.List;

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

public class DataDependentReduceManager
//@formatter:off
   <StackNode  extends IStackNode,
    ParseState extends AbstractParseState<?, StackNode>>
    extends
    ReduceManager
       <IDataDependentParseForest,
        IDataDependentDerivation<IDataDependentParseForest>,
        IDataDependentParseNode<IDataDependentParseForest, IDataDependentDerivation<IDataDependentParseForest>>,
        StackNode,
        ParseState>
//@formatter:on
{

    private DataDependentReduceManager(IParseTable parseTable,
        AbstractStackManager<IDataDependentParseForest, IDataDependentDerivation<IDataDependentParseForest>, IDataDependentParseNode<IDataDependentParseForest, IDataDependentDerivation<IDataDependentParseForest>>, StackNode, ParseState> stackManager,
        ParseForestManager<IDataDependentParseForest, IDataDependentDerivation<IDataDependentParseForest>, IDataDependentParseNode<IDataDependentParseForest, IDataDependentDerivation<IDataDependentParseForest>>, StackNode, ParseState> parseForestManager,
        ParseForestConstruction parseForestConstruction) {
        super(parseTable, stackManager, parseForestManager, parseForestConstruction);
    }

    public static
    //@formatter:off
       <StackNode_    extends IStackNode,
        ParseState_   extends AbstractParseState<?, StackNode_>,
        StackManager_ extends AbstractStackManager
           <IDataDependentParseForest,
            IDataDependentDerivation<IDataDependentParseForest>,
            IDataDependentParseNode<IDataDependentParseForest, IDataDependentDerivation<IDataDependentParseForest>>,
            StackNode_,
            ParseState_>>
    //@formatter:on
    ReduceManagerFactory<IDataDependentParseForest, IDataDependentDerivation<IDataDependentParseForest>, IDataDependentParseNode<IDataDependentParseForest, IDataDependentDerivation<IDataDependentParseForest>>, StackNode_, ParseState_, StackManager_, DataDependentReduceManager<StackNode_, ParseState_>>
        factoryDataDependent(ParserVariant parserVariant) {
        return (parseTable, stackManager, parseForestManager) -> new DataDependentReduceManager<>(parseTable,
            stackManager, parseForestManager, parserVariant.parseForestConstruction);
    }

    @Override protected boolean ignoreReducePath(StackNode pathBegin, IReduce reduce,
        IDataDependentParseForest[] parseNodes) {
        return ignoreByDeepPriorityConflict(reduce, parseNodes);
    }

    public static boolean ignoreByDeepPriorityConflict(IReduce reduce, IDataDependentParseForest[] parseNodes) {
        final IProduction production = ((ParseTableProduction) reduce.production()).getProduction();

        if(production instanceof ContextualProduction) {
            final List<ISymbol> rightHandSymbols = production.rightHand();

            for(int i = 0; i < rightHandSymbols.size(); i++) {
                final IDataDependentParseForest nextParseForest = parseNodes[i];
                final ISymbol nextSymbol = rightHandSymbols.get(i);

                if(nextSymbol instanceof ContextualSymbol && hasDeepConflict(nextParseForest, nextSymbol))
                    return true; // prohibit reduction
            }
        }

        return false;
    }

    private static
    //@formatter:off
       <ParseForest_ extends IDataDependentParseForest,
        Derivation_  extends IDataDependentDerivation<ParseForest_>,
        ParseNode_   extends IDataDependentParseNode<ParseForest_, Derivation_>>
    //@formatter:on
    boolean hasDeepConflict(ParseForest_ parseForest, ISymbol symbol) {
        final ContextualSymbol contextualSymbol = (ContextualSymbol) symbol;

        final long contextBitmap = contextualSymbol.deepContexts();

        if(contextBitmap == 0)
            return false;

        assert parseForest instanceof IDataDependentParseNode;

        final List<Derivation_> derivations = ((ParseNode_) parseForest).getDerivations();

        if(derivations.size() == 1) {
            final Derivation_ derivation = derivations.get(0);

            // check if bitmaps intersect
            return hasDeepConflict(derivation, contextBitmap);
        } else {
            // discard rule nodes where bitmaps intersect
            derivations.removeIf(derivation -> hasDeepConflict(derivation, contextBitmap));

            return derivations.isEmpty();
        }
    }

    private static boolean hasDeepConflict(IDataDependentDerivation<?> derivation, long contextBitmap) {
        return (derivation.getContextBitmap() & contextBitmap) != 0;
    }

}
