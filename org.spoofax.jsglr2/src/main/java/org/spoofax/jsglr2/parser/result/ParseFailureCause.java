package org.spoofax.jsglr2.parser.result;

import org.spoofax.jsglr2.messages.Category;
import org.spoofax.jsglr2.messages.Message;
import org.spoofax.jsglr2.parser.Position;

public class ParseFailureCause {

    public enum Type {

        UnexpectedEOF("Unexpected end of input", Category.PARSING),
        UnexpectedInput("Unexpected input", Category.PARSING),
        InvalidStartSymbol("Invalid start symbol", Category.PARSING),
        Cycle("Parse forest contains a cycle", Category.CYCLE),
        NonAssoc("Operator is non-associative", Category.NON_ASSOC),
        NonNested("Operator is non-nested", Category.NON_ASSOC),
        RecoveryTimeout("Recovery timed out", Category.RECOVERY);

        public final String message;
        public final Category category;

        Type(String message, Category category) {
            this.message = message;
            this.category = category;
        }

    }

    public final Type type;
    public final Position position;
    public final String explanation;

    public ParseFailureCause(Type type, Position position, String explanation) {
        this.type = type;
        this.position = position;
        this.explanation = explanation;
    }

    public ParseFailureCause(Type type, Position position) {
        this(type, position, null);
    }

    public ParseFailureCause(Type type) {
        this(type, null, null);
    }

    public String causeMessage() {
        return explanation != null ? type.message + " (" + explanation + ")" : type.message;
    }

    public Message toMessage() {
        return new Message(causeMessage(), type.category, position);
    }

}
