package org.spoofax.jsglr2.parser.result;

import org.spoofax.jsglr2.parseforest.IParseForest;
import org.spoofax.jsglr2.parser.AbstractParseState;

public class ParseSuccess<ParseForest extends IParseForest> extends ParseResult {

    public final ParseForest parseResult;

    public ParseSuccess(AbstractParseState<?, ?> parseState, ParseForest parseResult) {
        super(parseState);

        this.parseResult = parseResult;
    }

    public boolean isSuccess() {
        return true;
    }

}
