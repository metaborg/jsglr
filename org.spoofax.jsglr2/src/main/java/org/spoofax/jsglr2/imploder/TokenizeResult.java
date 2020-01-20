package org.spoofax.jsglr2.imploder;

import java.util.Collection;

import org.metaborg.core.messages.IMessage;
import org.spoofax.jsglr2.tokens.Tokens;

public class TokenizeResult {

    public Tokens tokens;
    public Collection<IMessage> messages;

    public TokenizeResult(Tokens tokens, Collection<IMessage> messages) {
        this.tokens = tokens;
        this.messages = messages;
    }

}
