package org.spoofax.jsglr2.parseforest;

import org.spoofax.jsglr.client.imploder.IToken;
import org.spoofax.jsglr2.parser.AbstractParse;
import org.spoofax.jsglr2.parser.Position;

public abstract class AbstractParseForest {

    public final int nodeNumber;
    public final AbstractParse<?, ?> parse;

    public Position startPosition, endPosition;

    public IToken token, firstToken, lastToken;

    protected AbstractParseForest(int nodeNumber, AbstractParse<?, ?> parse, Position startPosition, Position endPosition) {
        this.nodeNumber = nodeNumber;
        this.parse = parse;

        this.startPosition = startPosition;
        this.endPosition = endPosition;

        this.token = null;
        this.firstToken = null;
        this.lastToken = null;
    }

    public String inputPart() {
        return parse.getPart(startPosition.offset, endPosition.offset);
    }

    public abstract String descriptor();

}
