package org.spoofax.jsglr2;

import org.spoofax.jsglr2.parser.ParseException;
import org.spoofax.jsglr2.parser.observing.IParserObserver;

public interface JSGLR2<AbstractSyntaxTree> {

    void attachObserver(IParserObserver<?, ?, ?, ?, ?> parserObserver);

    JSGLR2Result<AbstractSyntaxTree> parseResult(String input, String fileName, String startSymbol);

    default JSGLR2Result<AbstractSyntaxTree> parseResult(String input) {
        return parseResult(input, "", null);
    }

    default AbstractSyntaxTree parse(String input, String fileName, String startSymbol) {
        JSGLR2Result<AbstractSyntaxTree> result = parseResult(input, fileName, startSymbol);

        if(result.isSuccess())
            return ((JSGLR2Success<AbstractSyntaxTree>) result).ast;
        else
            return null;
    }

    default AbstractSyntaxTree parse(String input) {
        return parse(input, "", null);
    }

    default AbstractSyntaxTree parseUnsafe(String input, String fileName, String startSymbol) throws ParseException {
        JSGLR2Result<AbstractSyntaxTree> result = parseResult(input, fileName, startSymbol);

        if(result.isSuccess())
            return ((JSGLR2Success<AbstractSyntaxTree>) result).ast;
        else
            throw((JSGLR2Failure<AbstractSyntaxTree>) result).parseFailure.exception();
    }

    default AbstractSyntaxTree parseUnsafe(String input) throws ParseException {
        return parseUnsafe(input, "", null);
    }
}
