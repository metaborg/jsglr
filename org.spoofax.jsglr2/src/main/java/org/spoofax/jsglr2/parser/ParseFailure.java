package org.spoofax.jsglr2.parser;

import org.spoofax.jsglr2.parseforest.AbstractParseForest;

public class ParseFailure<ParseForest extends AbstractParseForest, AbstractSyntaxTree>
    extends ParseResult<ParseForest, AbstractSyntaxTree> {

    public final ParseException parseException;

    public ParseFailure(Parse<ParseForest, ?> parse, ParseException parseException) {
        super(parse, false);

        this.parseException = parseException;
    }

}
