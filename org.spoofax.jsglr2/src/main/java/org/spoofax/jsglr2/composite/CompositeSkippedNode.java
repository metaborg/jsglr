package org.spoofax.jsglr2.composite;

import static org.spoofax.jsglr2.parseforest.IParseForest.sumWidth;

import org.metaborg.parsetable.productions.IProduction;
import org.spoofax.jsglr2.parseforest.basic.IBasicSkippedNode;
import org.spoofax.jsglr2.parser.Position;

public class CompositeSkippedNode<ParseForest extends ICompositeParseForest, Derivation extends ICompositeDerivation<ParseForest>>
    extends CompositeParseNode<ParseForest, Derivation> implements IBasicSkippedNode<ParseForest, Derivation> {

    private final int width;

    CompositeSkippedNode(Position startPosition, Position endPosition, IProduction production,
        ParseForest[] parseForests) {
        super(startPosition, endPosition, production);
        this.width = sumWidth(parseForests);
    }

    @Override public int width() {
        return width;
    }

}
