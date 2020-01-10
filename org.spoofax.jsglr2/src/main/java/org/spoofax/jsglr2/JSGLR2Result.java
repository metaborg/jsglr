package org.spoofax.jsglr2;

import java.util.Collection;

import org.metaborg.core.messages.IMessage;

public abstract class JSGLR2Result<AbstractSyntaxTree> {

    public final Collection<IMessage> messages;

    JSGLR2Result(Collection<IMessage> messages) {
        this.messages = messages;
    }

    public abstract boolean isSuccess();

}
