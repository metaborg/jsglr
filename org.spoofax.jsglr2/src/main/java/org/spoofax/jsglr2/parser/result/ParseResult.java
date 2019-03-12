package org.spoofax.jsglr2.parser.result;

import org.spoofax.jsglr2.JSGLR2Result;
import org.spoofax.jsglr2.parseforest.AbstractParseForest;
import org.spoofax.jsglr2.parser.AbstractParse;

public abstract class ParseResult<ParseForest extends AbstractParseForest, AbstractSyntaxTree>
    extends JSGLR2Result<ParseForest, AbstractSyntaxTree> {

    protected ParseResult(AbstractParse<ParseForest, ?> parse, boolean isSuccess) {
        super(parse, isSuccess);
    }

}
