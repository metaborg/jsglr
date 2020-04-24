package org.spoofax.jsglr2.parseforest.basic;

import org.metaborg.parsetable.productions.IProduction;
import org.metaborg.parsetable.productions.ProductionType;
import org.spoofax.jsglr2.parseforest.IParseForest;

public class BasicDerivation
//@formatter:off
   <ParseForest extends IParseForest>
//@formatter:on
    implements IBasicDerivation<ParseForest> {

    public final IProduction production;
    public final ProductionType productionType;
    public final ParseForest[] parseForests;

    public BasicDerivation(IProduction production, ProductionType productionType, ParseForest[] parseForests) {
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

    @Override public ParseForest[] parseForests() {
        return parseForests;
    }

}
