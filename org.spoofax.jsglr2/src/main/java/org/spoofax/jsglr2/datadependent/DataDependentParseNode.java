package org.spoofax.jsglr2.datadependent;

import org.metaborg.parsetable.productions.IProduction;
import org.spoofax.jsglr2.parseforest.basic.BasicParseNode;

class DataDependentParseNode
//@formatter:off
   <ParseForest extends IDataDependentParseForest,
    Derivation  extends IDataDependentDerivation<ParseForest>>
//@formatter:on
    extends BasicParseNode<ParseForest, Derivation> implements IDataDependentParseNode<ParseForest, Derivation> {

    DataDependentParseNode(int width, IProduction production) {
        super(width, production);
    }

}
