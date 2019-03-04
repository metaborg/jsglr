package org.spoofax.jsglr2.parser.result;

import org.spoofax.jsglr2.parseforest.IParseForest;
import org.spoofax.jsglr2.parser.AbstractParse;

public class ParseSuccess<ParseForest extends IParseForest, AbstractSyntaxTree>
    extends ParseResult<ParseForest, AbstractSyntaxTree> {

    public final ParseForest parseResult;

    public ParseSuccess(AbstractParse<ParseForest, ?> parse, ParseForest parseResult) {
        super(parse, true);

        this.parseResult = parseResult;
    }

}
