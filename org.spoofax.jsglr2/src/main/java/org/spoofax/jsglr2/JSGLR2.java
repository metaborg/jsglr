package org.spoofax.jsglr2;

import org.spoofax.jsglr2.parser.ParseException;

public interface JSGLR2<AbstractSyntaxTree> {

    JSGLR2Result<AbstractSyntaxTree> parseResult(String input, String filename, String startSymbol);

    default JSGLR2Result<AbstractSyntaxTree> parseResult(String input) {
        return parseResult(input, "", null);
    }

    default AbstractSyntaxTree parse(String input, String filename, String startSymbol) {
        JSGLR2Result<AbstractSyntaxTree> result = parseResult(input, filename, startSymbol);

        if(result.isSuccess())
            return ((JSGLR2Success<AbstractSyntaxTree>) result).ast;
        else
            return null;
    }

    default AbstractSyntaxTree parse(String input) {
        return parse(input, "", null);
    }

    default AbstractSyntaxTree parseUnsafe(String input, String filename, String startSymbol) throws ParseException {
        JSGLR2Result<AbstractSyntaxTree> result = parseResult(input, filename, startSymbol);

        if(result.isSuccess())
            return ((JSGLR2Success<AbstractSyntaxTree>) result).ast;
        else
            throw((JSGLR2Failure<AbstractSyntaxTree>) result).parseFailure.exception();
    }

    default AbstractSyntaxTree parseUnsafe(String input) throws ParseException {
        return parseUnsafe(input, "", null);
    }
}
