package org.spoofax.jsglr2.parser;

import org.spoofax.jsglr2.parseforest.AbstractParseForest;
import org.spoofax.jsglr2.stack.AbstractStackNode;

public interface IParser<ParseForest extends AbstractParseForest, StackNode extends AbstractStackNode<ParseForest>> {

    public ParseResult<ParseForest, StackNode, ?> parse(String input, String filename, String startSymbol);

    public default ParseResult<ParseForest, StackNode, ?> parse(String input, String filename) {
        return parse(input, filename, null);
    }

    public default ParseResult<ParseForest, StackNode, ?> parse(String input) {
        return parse(input, "");
    }

    /*
     * Parses an input and directly returns the parse forest in case of a successful parse or throws a ParseException
     * otherwise.
     */
    public default ParseForest parseUnsafe(String input, String filename, String startSymbol) throws ParseException {
        ParseResult<ParseForest, StackNode, ?> result = parse(input, filename, startSymbol);

        if(result.isSuccess) {
            ParseSuccess<ParseForest, StackNode, ?> success = (ParseSuccess<ParseForest, StackNode, ?>) result;

            return success.parseResult;
        } else {
            ParseFailure<ParseForest, StackNode, ?> failure = (ParseFailure<ParseForest, StackNode, ?>) result;

            throw failure.parseException;
        }
    }

    public default ParseForest parseUnsafe(String input, String filename) throws ParseException {
        return parseUnsafe(input, filename, null);
    }

    public default ParseForest parseUnsafe(String input) throws ParseException {
        return parseUnsafe(input, "");
    }

    public void attachObserver(IParserObserver<ParseForest, StackNode> observer);

}
