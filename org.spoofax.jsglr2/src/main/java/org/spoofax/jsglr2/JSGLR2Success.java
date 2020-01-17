package org.spoofax.jsglr2;

import java.util.Collection;

import org.metaborg.core.messages.IMessage;
import org.spoofax.jsglr2.tokens.Tokens;

public class JSGLR2Success<AbstractSyntaxTree> extends JSGLR2Result<AbstractSyntaxTree> {

    public final Tokens tokens;
    public final AbstractSyntaxTree ast;

    JSGLR2Success(AbstractSyntaxTree ast, Tokens tokens, Collection<IMessage> messages) {
        super(messages);
        this.tokens = tokens;
        this.ast = ast;
    }

    public boolean isSuccess() {
        return true;
    }

}