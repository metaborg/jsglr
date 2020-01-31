package org.spoofax.jsglr2.layoutsensitive;

import static org.spoofax.jsglr2.parseforest.IParseForest.sumWidth;

import org.metaborg.parsetable.productions.IProduction;
import org.spoofax.jsglr2.parseforest.basic.IBasicSkippedNode;
import org.spoofax.jsglr2.parser.Position;

public class LayoutSensitiveSkippedNode<ParseForest extends ILayoutSensitiveParseForest, Derivation extends ILayoutSensitiveDerivation<ParseForest>>
    extends LayoutSensitiveParseNode<ParseForest, Derivation> implements IBasicSkippedNode<ParseForest, Derivation> {

    private final int width;

    LayoutSensitiveSkippedNode(Position startPosition, Position endPosition, IProduction production,
        ParseForest[] parseForests) {
        super(startPosition, endPosition, production);
        this.width = sumWidth(parseForests);
    }

    @Override public int width() {
        return width;
    }

}
