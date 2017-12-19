package org.spoofax.jsglr2.parser;

import org.spoofax.jsglr2.JSGLR2Result;
import org.spoofax.jsglr2.parseforest.AbstractParseForest;

public abstract class ParseResult<ParseForest extends AbstractParseForest, AbstractSyntaxTree>
    extends JSGLR2Result<ParseForest, AbstractSyntaxTree> {

    protected ParseResult(Parse<ParseForest, ?> parse, boolean isSuccess) {
        super(parse, isSuccess);
    }

}
