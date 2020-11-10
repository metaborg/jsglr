package org.spoofax.jsglr2.parser;

import org.spoofax.jsglr2.JSGLR2Request;
import org.spoofax.jsglr2.parseforest.IParseForest;
import org.spoofax.jsglr2.parser.result.ParseFailure;
import org.spoofax.jsglr2.parser.result.ParseResult;
import org.spoofax.jsglr2.parser.result.ParseSuccess;

public interface IParser<ParseForest extends IParseForest> {

    ParseResult<ParseForest> parse(JSGLR2Request request, String previousInput, ParseForest previousResult);

    default ParseResult<ParseForest> parse(JSGLR2Request request) {
        return parse(request, null, null);
    }

    default ParseResult<ParseForest> parse(String input, String startSymbol, String previousInput,
        ParseForest previousResult) {
        return parse(new JSGLR2Request(input, "", startSymbol), previousInput, previousResult);
    }

    default ParseResult<ParseForest> parse(String input) {
        return parse(input, null, null, null);
    }

    default ParseResult<ParseForest> parse(String input, String previousInput, ParseForest previousResult) {
        return parse(input, null, previousInput, previousResult);
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

    default ParseForest parseUnsafe(String input) throws ParseException {
        return parseUnsafe(input, null, null, null);
    }

    default ParseForest parseUnsafe(String input, String startSymbol) throws ParseException {
        return parseUnsafe(input, startSymbol, null, null);
    }

}
