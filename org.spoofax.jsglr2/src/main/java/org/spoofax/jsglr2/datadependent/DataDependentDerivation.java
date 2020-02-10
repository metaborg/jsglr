package org.spoofax.jsglr2.datadependent;

import org.metaborg.parsetable.productions.IProduction;
import org.metaborg.parsetable.productions.ProductionType;
import org.spoofax.jsglr2.parseforest.basic.BasicDerivation;

class DataDependentDerivation
//@formatter:off
   <ParseForest extends IDataDependentParseForest>
//@formatter:on
    extends BasicDerivation<ParseForest> implements IDataDependentDerivation<ParseForest> {

    private final long contextBitmap;

    DataDependentDerivation(IProduction production, ProductionType productionType, ParseForest[] parseForests) {
        super(production, productionType, parseForests);

        contextBitmap = IDataDependentDerivation.calculateContextBitmap(parseForests);
    }

    @Override public final long getContextBitmap() {
        return contextBitmap;
    }

}
