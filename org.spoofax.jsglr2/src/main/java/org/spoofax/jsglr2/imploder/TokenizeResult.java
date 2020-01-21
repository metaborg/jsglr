package org.spoofax.jsglr2.imploder;

import java.util.Collection;

import org.spoofax.jsglr2.messages.Message;
import org.spoofax.jsglr2.tokens.Tokens;

public class TokenizeResult {

    public Tokens tokens;
    public Collection<Message> messages;

    public TokenizeResult(Tokens tokens, Collection<Message> messages) {
        this.tokens = tokens;
        this.messages = messages;
    }

}
