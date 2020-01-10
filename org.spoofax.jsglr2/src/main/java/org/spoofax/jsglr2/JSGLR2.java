package org.spoofax.jsglr2;

import javax.annotation.Nullable;

import org.apache.commons.vfs2.FileObject;
import org.spoofax.jsglr2.parser.ParseException;

public interface JSGLR2<AbstractSyntaxTree> {

    JSGLR2Result<AbstractSyntaxTree> parseResult(String input, @Nullable FileObject resource, String startSymbol);

    default JSGLR2Result<AbstractSyntaxTree> parseResult(String input) {
        return parseResult(input, null, null);
    }

    default AbstractSyntaxTree parse(String input, @Nullable FileObject resource, String startSymbol) {
        JSGLR2Result<AbstractSyntaxTree> result = parseResult(input, resource, startSymbol);

        if(result.isSuccess())
            return ((JSGLR2Success<AbstractSyntaxTree>) result).ast;
        else
            return null;
    }

    default AbstractSyntaxTree parse(String input) {
        return parse(input, null, null);
    }

    default AbstractSyntaxTree parseUnsafe(String input, @Nullable FileObject resource, String startSymbol)
        throws ParseException {
        JSGLR2Result<AbstractSyntaxTree> result = parseResult(input, resource, startSymbol);

        if(result.isSuccess())
            return ((JSGLR2Success<AbstractSyntaxTree>) result).ast;
        else
            throw((JSGLR2Failure<AbstractSyntaxTree>) result).parseFailure.exception();
    }

    default AbstractSyntaxTree parseUnsafe(String input) throws ParseException {
        return parseUnsafe(input, null, null);
    }
}
