package org.spoofax.jsglr2.parseforest.hybrid;

import org.spoofax.jsglr2.parseforest.IDerivation;
import org.spoofax.jsglr2.parsetable.IProduction;
import org.spoofax.jsglr2.parsetable.ProductionType;

public class Derivation implements IDerivation<HybridParseForest> {

    public final IProduction production;
    public final ProductionType productionType;
    public final HybridParseForest[] parseForests;
    
    public Derivation(IProduction production, ProductionType productionType, HybridParseForest[] parseForests) {
        this.production = production;
        this.productionType = productionType;
        this.parseForests = parseForests;
    }

    public IProduction production() {
        return production;
    }
    
    public HybridParseForest[] parseForests() {
        return parseForests;
    }
    
}
