package org.spoofax.jsglr2.parseforest.basic;

import org.metaborg.parsetable.productions.IProduction;
import org.metaborg.parsetable.productions.ProductionType;
import org.spoofax.jsglr2.parseforest.IDerivation;

public class BasicDerivation implements IDerivation<BasicParseForest> {

    public final IProduction production;
    public final ProductionType productionType;
    public final BasicParseForest[] parseForests;

    public BasicDerivation(IProduction production, ProductionType productionType, BasicParseForest[] parseForests) {
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

    @Override public BasicParseForest[] parseForests() {
        return parseForests;
    }

}
