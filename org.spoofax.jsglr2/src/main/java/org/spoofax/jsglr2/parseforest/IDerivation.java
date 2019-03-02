package org.spoofax.jsglr2.parseforest;

import org.metaborg.parsetable.IProduction;
import org.metaborg.parsetable.ProductionType;

public interface IDerivation<ParseForest extends AbstractParseForest> extends IParseForestWidth {

    IProduction production();

    ProductionType productionType();

    ParseForest[] parseForests();

    default int width() {
        int width = 0;

        for(ParseForest parseForest : parseForests()) {
            if(parseForest != null)
                width += parseForest.width();
        }

        return width;
    }

}
