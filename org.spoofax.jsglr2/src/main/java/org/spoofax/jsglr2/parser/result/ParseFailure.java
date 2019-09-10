package org.spoofax.jsglr2.parser.result;

import org.spoofax.jsglr2.parseforest.IParseForest;
import org.spoofax.jsglr2.parser.Parse;
import org.spoofax.jsglr2.parser.ParseException;

public class ParseFailure<ParseForest extends IParseForest> extends ParseResult<ParseForest> {

    public final ParseFailureType failureType;

    public ParseFailure(Parse<ParseForest, ?, ?> parse, ParseFailureType failureType) {
        super(parse);

        this.failureType = failureType;
    }

    public boolean isSuccess() {
        return false;
    }

    public ParseException exception() {
        return new ParseException(failureType);
    }

}
