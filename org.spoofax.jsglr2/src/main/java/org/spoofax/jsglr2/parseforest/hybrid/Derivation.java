package org.spoofax.jsglr2.parseforest.hybrid;

import org.metaborg.parsetable.IProduction;
import org.metaborg.parsetable.ProductionType;
import org.spoofax.jsglr2.parseforest.IDerivation;

public class Derivation implements IDerivation<HybridParseForest> {

    public final IProduction production;
    public final ProductionType productionType;
    public final HybridParseForest[] parseForests;

    public Derivation(IProduction production, ProductionType productionType, HybridParseForest[] parseForests) {
        this.production = production;
        this.productionType = productionType;
        this.parseForests = parseForests;
    }

    @Override
    public IProduction production() {
        return production;
    }

    @Override
    public HybridParseForest[] parseForests() {
        return parseForests;
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        
        s.append(production.id());
        s.append("(");
        
        for (int i = 0; i < parseForests.length; i++) {
            if (i > 0)
                s.append(", ");
            
            s.append(parseForests[i].toString());
        }
        
        s.append(")");
        
        return s.toString();
    }

}
