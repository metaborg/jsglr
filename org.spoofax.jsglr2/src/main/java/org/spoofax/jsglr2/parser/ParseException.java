package org.spoofax.jsglr2.parser;

import org.spoofax.jsglr2.parser.result.ParseFailureType;

public class ParseException extends Exception {

    private static final long serialVersionUID = 5070826083429554841L;

    public ParseFailureType failureType;

    public ParseException(ParseFailureType failureType) {
        super(failureType.message);

        this.failureType = failureType;
    }

}
