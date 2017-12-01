package org.spoofax.jsglr2.parseforest.basic;

import org.spoofax.jsglr2.parseforest.AbstractParseForest;
import org.spoofax.jsglr2.parser.Parse;
import org.spoofax.jsglr2.parser.Position;

public abstract class BasicParseForest extends AbstractParseForest {

    protected BasicParseForest(int nodeNumber, Parse<?, ?> parse, Position startPosition, Position endPosition) {
        super(nodeNumber, parse, startPosition, endPosition);
    }

}
