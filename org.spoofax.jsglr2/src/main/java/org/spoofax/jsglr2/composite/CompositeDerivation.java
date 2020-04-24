package org.spoofax.jsglr2.composite;

import org.metaborg.parsetable.productions.IProduction;
import org.metaborg.parsetable.productions.ProductionType;
import org.spoofax.jsglr2.datadependent.IDataDependentDerivation;
import org.spoofax.jsglr2.parseforest.basic.BasicDerivation;

class CompositeDerivation
//@formatter:off
   <ParseForest extends ICompositeParseForest>
//@formatter:on
    extends BasicDerivation<ParseForest> implements ICompositeDerivation<ParseForest> {

    private final long contextBitmap;

    CompositeDerivation(IProduction production, ProductionType productionType, ParseForest[] parseForests) {
        super(production, productionType, parseForests);

        contextBitmap = IDataDependentDerivation.calculateContextBitmap(parseForests);

    }

    @Override public final long getContextBitmap() {
        return contextBitmap;
    }

}
