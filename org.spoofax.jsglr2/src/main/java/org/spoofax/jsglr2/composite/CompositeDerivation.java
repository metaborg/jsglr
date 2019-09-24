package org.spoofax.jsglr2.composite;

import org.metaborg.parsetable.productions.IProduction;
import org.metaborg.parsetable.productions.ProductionType;
import org.spoofax.jsglr2.datadependent.IDataDependentDerivation;
import org.spoofax.jsglr2.parseforest.basic.BasicDerivation;
import org.spoofax.jsglr2.parser.Position;

class CompositeDerivation
//@formatter:off
   <ParseForest extends ICompositeParseForest>
//@formatter:on
    extends BasicDerivation<ParseForest> implements ICompositeDerivation<ParseForest> {

    private final long contextBitmap;

    private final Position startPosition, endPosition;
    private final Position leftPosition, rightPosition;

    CompositeDerivation(Position startPosition, Position leftPosition, Position rightPosition, Position endPosition,
        IProduction production, ProductionType productionType, ParseForest[] parseForests) {
        super(production, productionType, parseForests);

        contextBitmap = IDataDependentDerivation.calculateContextBitmap(parseForests);

        this.startPosition = startPosition;
        this.endPosition = endPosition;
        this.leftPosition = leftPosition;
        this.rightPosition = rightPosition;
    }

    @Override public final long getContextBitmap() {
        return contextBitmap;
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
