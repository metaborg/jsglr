package org.spoofax.jsglr2;

import org.spoofax.jsglr2.parser.result.ParseFailure;

public class JSGLR2Failure<AbstractSyntaxTree> extends JSGLR2Result<AbstractSyntaxTree> {

    public final ParseFailure<?> parseFailure;

    JSGLR2Failure(ParseFailure<?> parseFailure) {
        this.parseFailure = parseFailure;
    }

    public boolean isSuccess() {
        return false;
    }

}