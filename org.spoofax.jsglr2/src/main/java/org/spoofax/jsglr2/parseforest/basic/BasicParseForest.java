package org.spoofax.jsglr2.parseforest.basic;

import org.spoofax.jsglr2.parseforest.AbstractParseForest;
import org.spoofax.jsglr2.parser.AbstractParse;
import org.spoofax.jsglr2.parser.Position;

public abstract class BasicParseForest extends AbstractParseForest {

    protected BasicParseForest(AbstractParse<?, ?> parse, Position startPosition, Position endPosition) {
        super(parse, startPosition, endPosition);
    }

}
