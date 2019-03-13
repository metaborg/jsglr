package org.spoofax.jsglr2.layoutsensitive;

import org.spoofax.jsglr2.parseforest.IParseForest;
import org.spoofax.jsglr2.parser.Position;

public abstract class LayoutSensitiveParseForest implements IParseForest {

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
