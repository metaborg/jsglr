package org.spoofax.jsglr2.parser;

import org.spoofax.jsglr2.parser.result.ParseFailureCause;

public class ParseException extends Exception {

    private static final long serialVersionUID = 5070826083429554841L;

    public ParseException(ParseFailureCause.Type failureType, int offset, Integer character) {
        super(failureType.message + (failureType == ParseFailureCause.Type.UnexpectedInput
            ? (" at offset " + offset + ": " + new String(Character.toChars(character))) : ""));
    }

}
