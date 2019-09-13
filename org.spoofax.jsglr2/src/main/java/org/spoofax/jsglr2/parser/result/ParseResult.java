package org.spoofax.jsglr2.parser.result;

import org.spoofax.jsglr2.parseforest.IParseForest;
import org.spoofax.jsglr2.parser.AbstractParseState;

public abstract class ParseResult<ParseForest extends IParseForest> {

    public final AbstractParseState<ParseForest, ?> parseState;

    ParseResult(AbstractParseState<ParseForest, ?> parseState) {
        this.parseState = parseState;
    }

    public abstract boolean isSuccess();

}
