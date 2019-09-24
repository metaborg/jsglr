package org.spoofax.jsglr2.datadependent;

import org.metaborg.parsetable.productions.IProduction;
import org.metaborg.parsetable.productions.ProductionType;
import org.metaborg.sdf2table.parsetable.ParseTableProduction;
import org.spoofax.jsglr2.parseforest.basic.BasicDerivation;

class DataDependentDerivation
//@formatter:off
   <ParseForest extends IDataDependentParseForest>
//@formatter:on
    extends BasicDerivation<ParseForest> implements IDataDependentDerivation<ParseForest> {

    private final long contextBitmap;

    DataDependentDerivation(IProduction production, ProductionType productionType, ParseForest[] parseForests) {
        super(production, productionType, parseForests);

        contextBitmap = calculateContextBitmap(parseForests);
    }

    @Override public final long getContextBitmap() {
        return contextBitmap;
    }

    static private <ParseForest_ extends IDataDependentParseForest> long
        calculateContextBitmap(ParseForest_[] parseForests) {
        long contextBitmap = 0L;

        if(parseForests.length > 0) {
            if(parseForests.length == 1) {
                if(parseForests[0] instanceof IDataDependentParseNode) {
                    final IDataDependentParseNode<ParseForest_, IDataDependentDerivation<ParseForest_>> parseNode =
                        (IDataDependentParseNode<ParseForest_, IDataDependentDerivation<ParseForest_>>) parseForests[0];
                    final ParseTableProduction onlyProduction = (ParseTableProduction) parseNode.production();

                    // introduction of contextual token
                    contextBitmap |= onlyProduction.contextL();
                    contextBitmap |= onlyProduction.contextR();

                    // aggregation of recursive contextual tokens
                    for(IDataDependentDerivation<ParseForest_> derivation : parseNode.getDerivations()) {
                        contextBitmap |= (derivation.getContextBitmap());
                    }
                }
            } else {
                if(parseForests[0] instanceof IDataDependentParseNode) {
                    final IDataDependentParseNode<ParseForest_, IDataDependentDerivation<ParseForest_>> parseForest =
                        (IDataDependentParseNode<ParseForest_, IDataDependentDerivation<ParseForest_>>) parseForests[0];
                    final ParseTableProduction leftmostProduction = (ParseTableProduction) parseForest.production();

                    // introduction of contextual token
                    contextBitmap |= leftmostProduction.contextL();

                    // aggregation of recursive contextual tokens
                    for(IDataDependentDerivation<ParseForest_> derivation : parseForest.getDerivations()) {
                        contextBitmap |= (derivation.getContextBitmap());
                    }
                }

                if(parseForests[parseForests.length - 1] instanceof IDataDependentParseNode) {
                    final IDataDependentParseNode<ParseForest_, IDataDependentDerivation<ParseForest_>> parseForest =
                        (IDataDependentParseNode<ParseForest_, IDataDependentDerivation<ParseForest_>>) parseForests[parseForests.length
                            - 1];
                    final ParseTableProduction rightmostProduction = (ParseTableProduction) parseForest.production();

                    // introduction of contextual token
                    contextBitmap |= rightmostProduction.contextR();

                    // aggregation of recursive contextual tokens
                    for(IDataDependentDerivation<ParseForest_> derivation : parseForest.getDerivations()) {
                        contextBitmap |= (derivation.getContextBitmap());
                    }
                }

            }
        }

        return contextBitmap;
    }

}
