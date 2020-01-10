package org.spoofax.jsglr2.parser.result;

import java.util.Collection;

import org.metaborg.core.messages.IMessage;
import org.spoofax.jsglr2.parseforest.IParseForest;
import org.spoofax.jsglr2.parser.AbstractParseState;

public abstract class ParseResult<ParseForest extends IParseForest> {

    public final AbstractParseState<?, ?> parseState;
    public final Collection<IMessage> messages;

    ParseResult(AbstractParseState<?, ?> parseState, Collection<IMessage> messages) {
        this.parseState = parseState;
        this.messages = messages;
    }

    public abstract boolean isSuccess();

}
