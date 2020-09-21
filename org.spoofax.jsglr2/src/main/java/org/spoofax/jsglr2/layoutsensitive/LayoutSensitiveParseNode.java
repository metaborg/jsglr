package org.spoofax.jsglr2.layoutsensitive;

import java.util.Collections;
import java.util.List;

import org.metaborg.parsetable.productions.IProduction;
import org.spoofax.jsglr2.parseforest.basic.BasicParseNode;
import org.spoofax.jsglr2.parser.Position;

class LayoutSensitiveParseNode
//@formatter:off
   <ParseForest extends ILayoutSensitiveParseForest,
    Derivation  extends ILayoutSensitiveDerivation<ParseForest>>
//@formatter:on
    extends BasicParseNode<ParseForest, Derivation> implements ILayoutSensitiveParseNode<ParseForest, Derivation> {

    private final Position startPosition, endPosition;
    private final Position leftPosition, rightPosition;

    private List<PositionInterval> longestMatchPositions = null;
    private boolean filteredLongestMatch = false;

    LayoutSensitiveParseNode(int width, IProduction production, Position startPosition, Position endPosition,
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

    @Override public boolean filteredLongestMatch() {
        return filteredLongestMatch;
    }

    @Override public void setFilteredLongestMatch() {
        filteredLongestMatch = true;
    }

    @Override public List<PositionInterval> longestMatchPositions() {
        return longestMatchPositions != null ? longestMatchPositions : Collections.emptyList();
    }

    @Override public void setLongestMatchPositions(List<PositionInterval> longestMatchPositions) {
        this.longestMatchPositions = longestMatchPositions;
    }

}
