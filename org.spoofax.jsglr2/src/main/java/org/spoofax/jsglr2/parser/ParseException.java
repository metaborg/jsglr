package org.spoofax.jsglr2.parser;

import org.spoofax.jsglr2.parser.result.ParseFailureCause;

public class ParseException extends Exception {

    private static final long serialVersionUID = 5070826083429554841L;

    public ParseFailureCause failureCause;

    public ParseException(ParseFailureCause failureCause, int offset, int character) {
        super(failureCause.type.message + (failureCause.type == ParseFailureCause.Type.UnexpectedInput
            ? " at offset " + offset + ": " + new String(Character.toChars(character)) : ""));

        this.failureCause = failureCause;
    }

}
