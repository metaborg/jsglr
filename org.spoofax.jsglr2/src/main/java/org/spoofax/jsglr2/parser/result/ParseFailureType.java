package org.spoofax.jsglr2.parser.result;

import org.spoofax.jsglr2.messages.Message;
import org.spoofax.jsglr2.messages.SourceRegion;
import org.spoofax.jsglr2.parser.AbstractParseState;
import org.spoofax.jsglr2.parser.Position;

public enum ParseFailureType {

    UnexpectedEOF("Unexpected end of input", false), UnexpectedInput("Unexpected input", false),
    InvalidStartSymbol("Invalid start symbol", true);

    public final String message;
    public final boolean errorAtTop;

    ParseFailureType(String message, boolean errorAtTop) {
        this.message = message;
        this.errorAtTop = errorAtTop;
    }

    public Message toMessage(AbstractParseState<?, ?> parseState) {
        if(errorAtTop)
            return Message.errorAtTop(message);
        else {
            Position position = Position.atOffset(parseState.inputStack.inputString(), parseState.inputStack.offset());
            SourceRegion region = new SourceRegion(position.offset, position.line, position.column, position.offset,
                position.line, position.column);

            return Message.error(message, region);
        }
    }

}