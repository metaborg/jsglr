package org.spoofax.jsglr2.parser.result;

import org.spoofax.jsglr2.parseforest.IParseForest;
import org.spoofax.jsglr2.parser.AbstractParse;

public abstract class ParseResult<ParseForest extends IParseForest> {

    public final AbstractParse<ParseForest, ?, ?> parse;

    ParseResult(AbstractParse<ParseForest, ?, ?> parse) {
        this.parse = parse;
    }

    public abstract boolean isSuccess();

}
