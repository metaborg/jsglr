package org.spoofax.jsglr2.parser.result;

import java.util.Collection;

import org.spoofax.jsglr2.messages.Message;
import org.spoofax.jsglr2.parseforest.IParseForest;
import org.spoofax.jsglr2.parser.AbstractParseState;

import mb.jsglr.shared.ITokens;

public abstract class ParseResult<ParseForest extends IParseForest> {

    public final AbstractParseState<?, ?> parseState;
    public Collection<Message> messages;

    ParseResult(AbstractParseState<?, ?> parseState, Collection<Message> messages) {
        this.parseState = parseState;
        this.messages = messages;
    }

    public abstract boolean isSuccess();

    public void postProcessMessages(ITokens tokens) {
        messages = parseState.postProcessMessages(messages, tokens);
    }

}
