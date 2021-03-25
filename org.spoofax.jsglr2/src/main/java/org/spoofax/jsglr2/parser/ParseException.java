package org.spoofax.jsglr2.parser;

import org.metaborg.parsetable.characterclasses.CharacterClassFactory;
import org.spoofax.jsglr2.parser.result.ParseFailureCause;

public class ParseException extends Exception {

    private static final long serialVersionUID = 5070826083429554841L;

    public final ParseFailureCause cause;

    public ParseException(ParseFailureCause cause, Integer character) {
        super(message(cause, character));

        this.cause = cause;
    }

    private static String message(ParseFailureCause cause, Integer character) {
        StringBuilder message = new StringBuilder();

        message.append(cause.causeMessage());

        if(cause.position != null)
            message.append(" at offset " + cause.position.offset + " (line " + cause.position.line + ", column "
                + cause.position.column + ")");

        if(character != null)
            message.append(
                String.format(" at character %s (U+%04X)", CharacterClassFactory.intToString(character), character));

        return message.toString();
    }

}
