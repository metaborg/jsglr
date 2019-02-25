package org.spoofax.jsglr2.parseforest;

import org.metaborg.parsetable.IProduction;
import org.metaborg.parsetable.ProductionType;

public interface IDerivation<ParseForest extends AbstractParseForest> {

    IProduction production();

    ProductionType productionType();

    ParseForest[] parseForests();

}
