package org.spoofax.jsglr2.parser.result;

import org.spoofax.jsglr2.parseforest.AbstractParseForest;
import org.spoofax.jsglr2.parser.Parse;

public class ParseSuccess<ParseForest extends AbstractParseForest, AbstractSyntaxTree>
    extends ParseResult<ParseForest, AbstractSyntaxTree> {

    public final ParseForest parseResult;

    public ParseSuccess(Parse<ParseForest, ?> parse, ParseForest parseResult) {
        super(parse, true);

        this.parseResult = parseResult;
    }

}
