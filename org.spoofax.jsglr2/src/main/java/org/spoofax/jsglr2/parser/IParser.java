package org.spoofax.jsglr2.parser;

import org.spoofax.jsglr2.parseforest.IParseForest;
import org.spoofax.jsglr2.parser.result.ParseFailure;
import org.spoofax.jsglr2.parser.result.ParseResult;
import org.spoofax.jsglr2.parser.result.ParseSuccess;

public interface IParser<ParseForest extends IParseForest> {

    ParseResult<ParseForest> parse(String input, String startSymbol, String previousInput, ParseForest previousResult);

    default ParseResult<ParseForest> parse(String input, String startSymbol) {
        return parse(input, startSymbol, null, null);
    }

    default ParseResult<ParseForest> parse(String input) {
        return parse(input, null);
    }

    /**
     * Parses an input and directly returns the parse forest in case of a successful parse or throws a ParseException
     * otherwise.
     */
    default ParseForest parseUnsafe(String input, String startSymbol, String previousInput, ParseForest previousResult)
        throws ParseException {
        ParseResult<ParseForest> result = parse(input, startSymbol, previousInput, previousResult);

        if(result.isSuccess()) {
            ParseSuccess<ParseForest> success = (ParseSuccess<ParseForest>) result;

            return success.parseResult;
        } else {
            ParseFailure<ParseForest> failure = (ParseFailure<ParseForest>) result;

            throw failure.exception();
        }
    }

    default ParseForest parseUnsafe(String input, String startSymbol) throws ParseException {
        return parseUnsafe(input, startSymbol, null, null);
    }

}
