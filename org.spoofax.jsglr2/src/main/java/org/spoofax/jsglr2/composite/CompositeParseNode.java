package org.spoofax.jsglr2.composite;

import org.metaborg.parsetable.productions.IProduction;
import org.spoofax.jsglr2.parseforest.basic.BasicParseNode;
import org.spoofax.jsglr2.parser.Position;

class CompositeParseNode
//@formatter:off
   <ParseForest extends ICompositeParseForest,
    Derivation  extends ICompositeDerivation<ParseForest>>
//@formatter:on
    extends BasicParseNode<ParseForest, Derivation> implements ICompositeParseNode<ParseForest, Derivation> {

    private final Position startPosition, endPosition;
    private final Position leftPosition, rightPosition;

    CompositeParseNode(int width, IProduction production, Position startPosition, Position endPosition,
        Position leftPosition, Position rightPosition) {
        super(width, production);

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
