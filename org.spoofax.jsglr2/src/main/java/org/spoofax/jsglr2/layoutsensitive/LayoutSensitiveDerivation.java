package org.spoofax.jsglr2.layoutsensitive;

import org.metaborg.parsetable.productions.IProduction;
import org.metaborg.parsetable.productions.ProductionType;
import org.spoofax.jsglr2.parseforest.basic.BasicDerivation;
import org.spoofax.jsglr2.parser.Position;

public class LayoutSensitiveDerivation
//@formatter:off
   <ParseForest extends ILayoutSensitiveParseForest>
//@formatter:on
    extends BasicDerivation<ParseForest> implements ILayoutSensitiveDerivation<ParseForest> {

    private final Position startPosition, endPosition;
    private final Position leftPosition, rightPosition;

    public LayoutSensitiveDerivation(Position startPosition, Position leftPosition, Position rightPosition,
        Position endPosition, IProduction production, ProductionType productionType, ParseForest[] parseForests) {
        super(production, productionType, parseForests);

        this.startPosition = startPosition;
        this.endPosition = endPosition;
        this.leftPosition = leftPosition;
        this.rightPosition = rightPosition;
    }

    @Override public Position getStartPosition() {
        return startPosition;
    }

    @Override public Position getEndPosition() {
        return endPosition;
    }

    @Override public Position getLeftPosition() {
        return leftPosition;
    }

    @Override public Position getRightPosition() {
        return rightPosition;
    }

}
