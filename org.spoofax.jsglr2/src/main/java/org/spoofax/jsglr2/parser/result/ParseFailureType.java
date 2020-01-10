package org.spoofax.jsglr2.parser.result;

import org.metaborg.core.messages.IMessage;
import org.metaborg.core.messages.MessageFactory;
import org.metaborg.core.source.ISourceRegion;
import org.metaborg.core.source.SourceRegion;
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

    public IMessage toMessage(AbstractParseState<?, ?> parseState) {
        if(errorAtTop)
            return MessageFactory.newParseErrorAtTop(parseState.inputStack.resource(), message, null);
        else {
            Position position = Position.atOffset(parseState.inputStack.inputString(), parseState.inputStack.offset());
            ISourceRegion region = new SourceRegion(position.offset, position.line, position.column, position.offset,
                position.line, position.column);

            return MessageFactory.newParseError(parseState.inputStack.resource(), region, message, null);
        }
    }

}