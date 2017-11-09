package org.spoofax.jsglr2.parser;

import org.spoofax.jsglr2.parseforest.AbstractParseForest;
import org.spoofax.jsglr2.stack.AbstractStackNode;

public interface IParser<StackNode extends AbstractStackNode<ParseForest>, ParseForest extends AbstractParseForest> {

    public ParseResult<StackNode, ParseForest, ?> parse(String input, String filename, String startSymbol);

    public default ParseResult<StackNode, ParseForest, ?> parse(String input, String filename) {
        return parse(input, filename, null);
    }

    public default ParseResult<StackNode, ParseForest, ?> parse(String input) {
        return parse(input, "");
    }

    /*
     * Parses an input and directly returns the parse forest in case of a successful parse or throws a ParseException
     * otherwise.
     */
    public default ParseForest parseUnsafe(String input, String filename, String startSymbol) throws ParseException {
        ParseResult<StackNode, ParseForest, ?> result = parse(input, filename, startSymbol);

        if(result.isSuccess) {
            ParseSuccess<StackNode, ParseForest, ?> success = (ParseSuccess<StackNode, ParseForest, ?>) result;

            return success.parseResult;
        } else {
            ParseFailure<StackNode, ParseForest, ?> failure = (ParseFailure<StackNode, ParseForest, ?>) result;

            throw failure.parseException;
        }
    }

    public default ParseForest parseUnsafe(String input, String filename) throws ParseException {
        return parseUnsafe(input, filename, null);
    }

    public default ParseForest parseUnsafe(String input) throws ParseException {
        return parseUnsafe(input, "");
    }

    public void attachObserver(IParserObserver<StackNode, ParseForest> observer);

}
