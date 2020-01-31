package org.spoofax.jsglr2.parseforest.basic;

import static org.spoofax.jsglr2.parseforest.IParseForest.sumWidth;

import org.metaborg.parsetable.productions.IProduction;
import org.spoofax.jsglr2.parseforest.IDerivation;

public class BasicSkippedNode<ParseForest extends IBasicParseForest, Derivation extends IDerivation<ParseForest>>
    extends BasicParseNode<ParseForest, Derivation> implements IBasicSkippedNode<ParseForest, Derivation> {

    private final int width;

    public BasicSkippedNode(IProduction production, ParseForest[] parseForests) {
        super(production);
        this.width = sumWidth(parseForests);
    }

    @Override public int width() {
        return width;
    }
}
