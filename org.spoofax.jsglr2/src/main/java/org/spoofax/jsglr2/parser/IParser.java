package org.spoofax.jsglr2.parser;

import org.spoofax.jsglr2.parseforest.IParseForest;
import org.spoofax.jsglr2.parser.result.ParseFailure;
import org.spoofax.jsglr2.parser.result.ParseResult;
import org.spoofax.jsglr2.parser.result.ParseSuccess;

public interface IParser<ParseForest extends IParseForest> {

    ParseResult<ParseForest> parse(String input, String fileName, String startSymbol);

    default ParseResult<ParseForest> parse(String input, String fileName) {
        return parse(input, fileName, null);
    }

    default ParseResult<ParseForest> parse(String input) {
        return parse(input, "");
    }

    /*
     * Parses an input and directly returns the parse forest in case of a successful parse or throws a ParseException
     * otherwise.
     */
    default ParseForest parseUnsafe(String input, String fileName, String startSymbol) throws ParseException {
        ParseResult<ParseForest> result = parse(input, fileName, startSymbol);

        if(result.isSuccess()) {
            ParseSuccess<ParseForest> success = (ParseSuccess<ParseForest>) result;

            return success.parseResult;
        } else {
            ParseFailure<ParseForest> failure = (ParseFailure<ParseForest>) result;

            throw failure.exception();
        }
    }

}
