package org.spoofax.jsglr2;

import org.spoofax.jsglr2.parser.IParser;
import org.spoofax.jsglr2.parser.ParseException;
import org.spoofax.jsglr2.parser.observing.IParserObserver;

public interface JSGLR2<AbstractSyntaxTree> {

    IParser<?> parser();

    void attachObserver(IParserObserver<?, ?, ?, ?, ?> parserObserver);

    JSGLR2Result<AbstractSyntaxTree> parseResult(JSGLR2Request request);

    default JSGLR2Result<AbstractSyntaxTree> parseResult(String input) {
        return parseResult(new JSGLR2Request(input));
    }

    default JSGLR2Result<AbstractSyntaxTree> parseResult(String input, String fileName, String startSymbol) {
        return parseResult(new JSGLR2Request(input, fileName, startSymbol));
    }

    default AbstractSyntaxTree parse(JSGLR2Request request) {
        JSGLR2Result<AbstractSyntaxTree> result = parseResult(request);

        if(result.isSuccess())
            return ((JSGLR2Success<AbstractSyntaxTree>) result).ast;
        else
            return null;
    }

    default AbstractSyntaxTree parse(String input) {
        return parse(new JSGLR2Request(input));
    }

    default AbstractSyntaxTree parse(String input, String fileName, String startSymbol) {
        return parse(new JSGLR2Request(input, fileName, startSymbol));
    }

    default AbstractSyntaxTree parseUnsafe(JSGLR2Request request) throws ParseException {
        JSGLR2Result<AbstractSyntaxTree> result = parseResult(request);

        if(result.isSuccess())
            return ((JSGLR2Success<AbstractSyntaxTree>) result).ast;
        else
            throw((JSGLR2Failure<AbstractSyntaxTree>) result).parseFailure.exception();
    }

    default AbstractSyntaxTree parseUnsafe(String input) throws ParseException {
        return parseUnsafe(new JSGLR2Request(input));
    }

    default AbstractSyntaxTree parseUnsafe(String input, String fileName, String startSymbol) throws ParseException {
        return parseUnsafe(new JSGLR2Request(input, fileName, startSymbol));
    }
}
