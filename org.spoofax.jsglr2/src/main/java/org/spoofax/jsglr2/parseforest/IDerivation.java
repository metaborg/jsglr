package org.spoofax.jsglr2.parseforest;

import org.metaborg.parsetable.productions.IProduction;
import org.metaborg.parsetable.productions.ProductionType;

public interface IDerivation<ParseForest extends IParseForest> extends IParseForest {

    IProduction production();

    ProductionType productionType();

    ParseForest[] parseForests();

    default int width() {
        return IParseForest.sumWidth(parseForests());
    }

    default String descriptor() {
        return production().descriptor();
    }

}
