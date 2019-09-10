package org.spoofax.jsglr2.parser.result;

import org.spoofax.jsglr2.parseforest.IParseForest;
import org.spoofax.jsglr2.parser.Parse;

public abstract class ParseResult<ParseForest extends IParseForest> {

    public final Parse<ParseForest, ?, ?> parse;

    ParseResult(Parse<ParseForest, ?, ?> parse) {
        this.parse = parse;
    }

    public abstract boolean isSuccess();

}
