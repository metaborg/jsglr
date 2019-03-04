package org.spoofax.jsglr2.datadependent;

import org.metaborg.parsetable.IProduction;
import org.metaborg.parsetable.ProductionType;
import org.metaborg.sdf2table.parsetable.ParseTableProduction;
import org.spoofax.jsglr2.parseforest.IDerivation;

public class DataDependentDerivation extends DataDependentParseForest implements IDerivation<DataDependentParseForest> {

    public final IProduction production;
    public final ProductionType productionType;
    public final DataDependentParseForest[] parseForests;

    private long contextBitmap = 0L;

    public DataDependentDerivation(IProduction production, ProductionType productionType,
        DataDependentParseForest[] parseForests) {
        this.production = production;
        this.productionType = productionType;
        this.parseForests = parseForests;

        if(parseForests.length > 0) {
            if(parseForests.length == 1) {
                if(parseForests[0] instanceof DataDependentParseNode) {
                    final DataDependentParseNode parseForest = (DataDependentParseNode) parseForests[0];
                    final ParseTableProduction onlyProduction = (ParseTableProduction) parseForest.production;

                    // introduction of contextual token
                    contextBitmap |= onlyProduction.contextL();
                    contextBitmap |= onlyProduction.contextR();

                    // aggregation of recursive contextual tokens
                    for(DataDependentDerivation derivation : parseForest.getDerivations()) {
                        contextBitmap |= (derivation.getContextBitmap());
                    }
                }
            } else {
                if(parseForests[0] instanceof DataDependentParseNode) {
                    final DataDependentParseNode parseForest = (DataDependentParseNode) parseForests[0];
                    final ParseTableProduction leftmostProduction = (ParseTableProduction) parseForest.production;

                    // introduction of contextual token
                    contextBitmap |= leftmostProduction.contextL();

                    // aggregation of recursive contextual tokens
                    for(DataDependentDerivation derivation : parseForest.getDerivations()) {
                        contextBitmap |= (derivation.getContextBitmap());
                    }
                }

                if(parseForests[parseForests.length - 1] instanceof DataDependentParseNode) {
                    final DataDependentParseNode parseForest =
                        (DataDependentParseNode) parseForests[parseForests.length - 1];
                    final ParseTableProduction rightmostProduction = (ParseTableProduction) parseForest.production;

                    // introduction of contextual token
                    contextBitmap |= rightmostProduction.contextR();

                    // aggregation of recursive contextual tokens
                    for(DataDependentDerivation derivation : parseForest.getDerivations()) {
                        contextBitmap |= (derivation.getContextBitmap());
                    }
                }

            }
        }
    }

    public IProduction production() {
        return production;
    }

    public ProductionType productionType() {
        return productionType;
    }

    public DataDependentParseForest[] parseForests() {
        return parseForests;
    }

    public final long getContextBitmap() {
        return contextBitmap;
    }

}
