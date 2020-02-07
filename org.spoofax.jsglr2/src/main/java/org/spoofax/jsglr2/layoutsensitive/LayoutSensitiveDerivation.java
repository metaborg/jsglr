package org.spoofax.jsglr2.layoutsensitive;

import org.metaborg.parsetable.productions.IProduction;
import org.metaborg.parsetable.productions.ProductionType;
import org.spoofax.jsglr2.parseforest.basic.BasicDerivation;

class LayoutSensitiveDerivation
//@formatter:off
   <ParseForest extends ILayoutSensitiveParseForest>
//@formatter:on
    extends BasicDerivation<ParseForest> implements ILayoutSensitiveDerivation<ParseForest> {

    LayoutSensitiveDerivation(IProduction production, ProductionType productionType, ParseForest[] parseForests) {
        super(production, productionType, parseForests);
    }

}
