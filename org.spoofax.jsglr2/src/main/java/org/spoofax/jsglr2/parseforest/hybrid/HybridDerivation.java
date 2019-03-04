package org.spoofax.jsglr2.parseforest.hybrid;

import org.metaborg.parsetable.IProduction;
import org.metaborg.parsetable.ProductionType;
import org.spoofax.jsglr2.parseforest.IDerivation;

public class HybridDerivation implements IDerivation<HybridParseForest> {

    public final IProduction production;
    public final ProductionType productionType;
    public final HybridParseForest[] parseForests;

    public HybridDerivation(IProduction production, ProductionType productionType, HybridParseForest[] parseForests) {
        this.production = production;
        this.productionType = productionType;
        this.parseForests = parseForests;
    }

    @Override public IProduction production() {
        return production;
    }

    @Override public ProductionType productionType() {
        return productionType;
    }

    @Override public HybridParseForest[] parseForests() {
        return parseForests;
    }

}
