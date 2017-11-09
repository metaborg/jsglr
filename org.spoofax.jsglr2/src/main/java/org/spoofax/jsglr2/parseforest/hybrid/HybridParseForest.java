package org.spoofax.jsglr2.parseforest.hybrid;

import org.spoofax.jsglr2.parseforest.AbstractParseForest;
import org.spoofax.jsglr2.parser.Parse;
import org.spoofax.jsglr2.parser.Position;

public abstract class HybridParseForest extends AbstractParseForest {

    protected HybridParseForest(int nodeNumber, Parse<?, AbstractParseForest> parse, Position startPosition,
        Position endPosition) {
        super(nodeNumber, parse, startPosition, endPosition);
    }

}
