package org.spoofax.jsglr2.parser.result;

import org.spoofax.jsglr2.messages.Message;
import org.spoofax.jsglr2.messages.SourceRegion;
import org.spoofax.jsglr2.parser.Position;

public class ParseFailureCause {

    public enum Type {

        UnexpectedEOF("Unexpected end of input"), UnexpectedInput("Unexpected input"),
        InvalidStartSymbol("Invalid start symbol");

        public final String message;

        Type(String message) {
            this.message = message;
        }

    }

    public final Type type;
    public final Position position;

    public ParseFailureCause(Type type, Position position) {
        this.type = type;
        this.position = position;
    }

    public ParseFailureCause(Type type) {
        this(type, null);
    }

    public Message toMessage() {
        if(position == null)
            return Message.errorAtTop(type.message);
        else {
            SourceRegion region = new SourceRegion(position.offset, position.line, position.column, position.offset,
                position.line, position.column);

            return Message.error(type.message, region);
        }
    }

}