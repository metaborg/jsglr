package org.spoofax.jsglr2.parser;

import org.spoofax.jsglr2.JSGLR2Result;

public abstract class ParseResult<ParseForest> extends JSGLR2Result {

    protected ParseResult(Parse parse, boolean isSuccess) {
        super(parse, isSuccess);
    }
    
}
