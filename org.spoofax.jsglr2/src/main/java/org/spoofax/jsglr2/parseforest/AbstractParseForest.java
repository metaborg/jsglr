package org.spoofax.jsglr2.parseforest;

import org.spoofax.jsglr2.parser.Position;

public abstract class AbstractParseForest implements IParseForest {

    private final Position startPosition, endPosition;

    protected AbstractParseForest(Position startPosition, Position endPosition) {
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
