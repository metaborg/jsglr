package org.spoofax.jsglr2.parser;

import org.spoofax.jsglr2.parser.result.ParseFailureCause;

public class ParseException extends Exception {

    private static final long serialVersionUID = 5070826083429554841L;

    public ParseException(ParseFailureCause.Type failureType, Position position, Integer character) {
        super(message(failureType, position, character));
    }

    private static String message(ParseFailureCause.Type failureType, Position position, Integer character) {
        StringBuilder message = new StringBuilder();

        message.append(failureType.message);

        if(position != null)
            message.append(
                " at offset " + position.offset + "(line " + position.line + ", column " + position.column + ")");

        if(character != null)
            message.append(" at character " + new String(Character.toChars(character)));

        return message.toString();
    }

}
