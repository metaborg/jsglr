package org.spoofax.jsglr2.parser.result;

import org.spoofax.jsglr2.parseforest.AbstractParseForest;
import org.spoofax.jsglr2.parser.Parse;
import org.spoofax.jsglr2.parser.ParseException;

public class ParseFailure<ParseForest extends AbstractParseForest, AbstractSyntaxTree>
    extends ParseResult<ParseForest, AbstractSyntaxTree> {

    public final ParseException parseException;

    public ParseFailure(Parse<ParseForest, ?> parse, ParseException parseException) {
        super(parse, false);

        this.parseException = parseException;
    }

}
