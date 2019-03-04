package org.spoofax.jsglr2.layoutsensitive;

import org.spoofax.jsglr2.parseforest.AbstractParseForest;
import org.spoofax.jsglr2.parser.Position;

public abstract class LayoutSensitiveParseForest extends AbstractParseForest {

    protected LayoutSensitiveParseForest(Position startPosition, Position endPosition) {
        super(startPosition, endPosition);
    }

}
