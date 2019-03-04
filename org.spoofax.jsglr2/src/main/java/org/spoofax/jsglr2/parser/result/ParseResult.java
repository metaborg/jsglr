package org.spoofax.jsglr2.parser.result;

import org.spoofax.jsglr2.parseforest.IParseForest;
import org.spoofax.jsglr2.parser.AbstractParse;

public abstract class ParseResult<ParseForest extends IParseForest> {

    public final AbstractParse<ParseForest, ?> parse;
    public final boolean isSuccess;

    protected ParseResult(AbstractParse<ParseForest, ?> parse, boolean isSuccess) {
        this.parse = parse;
        this.isSuccess = isSuccess;
    }

}
