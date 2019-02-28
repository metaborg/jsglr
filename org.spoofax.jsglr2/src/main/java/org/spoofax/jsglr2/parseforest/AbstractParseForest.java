package org.spoofax.jsglr2.parseforest;

import org.spoofax.jsglr.client.imploder.IToken;
import org.spoofax.jsglr2.parser.Position;

public abstract class AbstractParseForest {

    public Position startPosition, endPosition;

    public IToken token, firstToken, lastToken;

    protected AbstractParseForest(Position startPosition, Position endPosition) {
        this.startPosition = startPosition;
        this.endPosition = endPosition;

        this.token = null;
        this.firstToken = null;
        this.lastToken = null;
    }

    public abstract String descriptor();

}
