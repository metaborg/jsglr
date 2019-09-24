package org.spoofax.jsglr2.layoutsensitive;

import org.metaborg.parsetable.productions.IProduction;
import org.spoofax.jsglr2.parseforest.basic.BasicParseNode;
import org.spoofax.jsglr2.parser.Position;

import java.util.List;

class LayoutSensitiveParseNode
//@formatter:off
   <ParseForest extends ILayoutSensitiveParseForest,
    Derivation  extends ILayoutSensitiveDerivation<ParseForest>>
//@formatter:on
    extends BasicParseNode<ParseForest, Derivation> implements ILayoutSensitiveParseNode<ParseForest, Derivation> {

    private final Position startPosition, endPosition;

    private List<PositionInterval> longestMatchPositions = null;
    private boolean filteredLongestMatch = false;

    LayoutSensitiveParseNode(Position startPosition, Position endPosition, IProduction production) {
        super(production);

        this.startPosition = startPosition;
        this.endPosition = endPosition;
    }

    @Override public Position getStartPosition() {
        return startPosition;
    }

    @Override public Position getEndPosition() {
        return endPosition;
    }

    @Override public boolean filteredLongestMatch() {
        return filteredLongestMatch;
    }

    @Override public void setFilteredLongestMatch() {
        filteredLongestMatch = true;
    }

    @Override public List<PositionInterval> longestMatchPositions() {
        return longestMatchPositions;
    }

    @Override public void setLongestMatchPositions(List<PositionInterval> longestMatchPositions) {
        this.longestMatchPositions = longestMatchPositions;
    }

}
