package org.spoofax.jsglr2.parser;

import org.spoofax.jsglr2.parseforest.AbstractParseForest;

public class ParseSuccess<ParseForest extends AbstractParseForest, AbstractSyntaxTree>
    extends ParseResult<ParseForest, AbstractSyntaxTree> {

    public final ParseForest parseResult;

    public ParseSuccess(Parse<ParseForest, ?> parse, ParseForest parseResult) {
        super(parse, true);

        this.parseResult = parseResult;
    }

}
