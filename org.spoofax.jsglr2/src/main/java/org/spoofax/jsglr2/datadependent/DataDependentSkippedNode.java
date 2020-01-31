package org.spoofax.jsglr2.datadependent;

import static org.spoofax.jsglr2.parseforest.IParseForest.sumWidth;

import org.metaborg.parsetable.productions.IProduction;
import org.spoofax.jsglr2.parseforest.basic.IBasicSkippedNode;

public class DataDependentSkippedNode<ParseForest extends IDataDependentParseForest, Derivation extends IDataDependentDerivation<ParseForest>>
    extends DataDependentParseNode<ParseForest, Derivation> implements IBasicSkippedNode<ParseForest, Derivation> {

    private final int width;

    DataDependentSkippedNode(IProduction production, ParseForest[] parseForests) {
        super(production);
        this.width = sumWidth(parseForests);
    }

    @Override public int width() {
        return width;
    }

}
