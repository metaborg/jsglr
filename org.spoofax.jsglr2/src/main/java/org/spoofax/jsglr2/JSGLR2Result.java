package org.spoofax.jsglr2;

import java.util.Collection;

import org.spoofax.jsglr2.messages.Message;

public abstract class JSGLR2Result<AbstractSyntaxTree> {

    public final Collection<Message> messages;

    JSGLR2Result(Collection<Message> messages) {
        this.messages = messages;
    }

    public abstract boolean isSuccess();

}
