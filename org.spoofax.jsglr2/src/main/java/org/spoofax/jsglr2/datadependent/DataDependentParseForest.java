package org.spoofax.jsglr2.datadependent;

import org.spoofax.jsglr2.parseforest.AbstractParseForest;
import org.spoofax.jsglr2.parser.Position;

public abstract class DataDependentParseForest extends AbstractParseForest {

    protected DataDependentParseForest(Position startPosition, Position endPosition) {
        super(startPosition, endPosition);
    }

}
