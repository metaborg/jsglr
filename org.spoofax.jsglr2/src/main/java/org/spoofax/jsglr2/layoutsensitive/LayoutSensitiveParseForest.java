package org.spoofax.jsglr2.layoutsensitive;

import org.spoofax.jsglr2.parseforest.AbstractParseForest;
import org.spoofax.jsglr2.parser.Position;

public abstract class LayoutSensitiveParseForest extends AbstractParseForest {

    private final Position startPosition, endPosition;

    protected LayoutSensitiveParseForest(Position startPosition, Position endPosition) {
        this.startPosition = startPosition;
        this.endPosition = endPosition;
    }

    public Position getStartPosition() {
        return startPosition;
    }

    public Position getEndPosition() {
        return endPosition;
    }

}
