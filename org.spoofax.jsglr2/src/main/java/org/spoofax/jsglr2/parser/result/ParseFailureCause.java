package org.spoofax.jsglr2.parser.result;

import org.spoofax.jsglr2.messages.Message;
import org.spoofax.jsglr2.parser.Position;

public class ParseFailureCause {

    public enum Type {

        UnexpectedEOF("Unexpected end of input"), UnexpectedInput("Unexpected input"),
        InvalidStartSymbol("Invalid start symbol"), Cycle("Cycle in parse forest"),
        NonAssoc("Operator is non-associative"), NonNested("Operator is non-nested");

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
        return Message.error(type.message, position);
    }

}
