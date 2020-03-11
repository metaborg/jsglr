package org.spoofax.jsglr2.parser;

import org.spoofax.jsglr2.parser.result.ParseFailureType;

public class ParseException extends Exception {

    private static final long serialVersionUID = 5070826083429554841L;

    public ParseFailureType failureType;

    public ParseException(ParseFailureType failureType, int offset, int character) {
        super(failureType.message + (failureType == ParseFailureType.UnexpectedInput
            ? " at offset " + offset + ": " + new String(Character.toChars(character)) : ""));

        this.failureType = failureType;
    }

}
