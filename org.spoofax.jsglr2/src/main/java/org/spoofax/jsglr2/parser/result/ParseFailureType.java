package org.spoofax.jsglr2.parser.result;

import org.metaborg.core.messages.IMessage;
import org.metaborg.core.messages.MessageFactory;
import org.spoofax.jsglr2.parser.AbstractParseState;

public enum ParseFailureType {

    Unknown("unknown parsing failure"), InvalidStartSymbol("invalid start symbol");

    public final String message;

    ParseFailureType(String message) {
        this.message = message;
    }

    public IMessage toMessage(AbstractParseState<?, ?> parseState) {
        return MessageFactory.newParseErrorAtTop(parseState.inputStack.resource(), message, null);
    }

}