package org.spoofax.jsglr2.parser;

import org.metaborg.parsetable.characterclasses.CharacterClassFactory;
import org.spoofax.jsglr2.parser.result.ParseFailureCause;

public class ParseException extends Exception {

    private static final long serialVersionUID = 5070826083429554841L;

    private final ParseFailureCause.Type failureType;
    private final Position position;

    public ParseException(ParseFailureCause.Type failureType, Position position, Integer character) {
        super(message(failureType, position, character));

        this.failureType = failureType;
        this.position = position;
    }

    public final ParseFailureCause cause() {
        return new ParseFailureCause(failureType, position);
    }

    private static String message(ParseFailureCause.Type failureType, Position position, Integer character) {
        StringBuilder message = new StringBuilder();

        message.append(failureType.message);

        if(position != null)
            message.append(
                " at offset " + position.offset + " (line " + position.line + ", column " + position.column + ")");

        if(character != null)
            message.append(
                String.format(" at character %s (U+%04X)", CharacterClassFactory.intToString(character), character));

        return message.toString();
    }

}
