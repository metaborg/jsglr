package org.spoofax.jsglr2;

import java.util.Collection;

import org.spoofax.jsglr2.messages.Message;
import org.spoofax.jsglr2.tokens.Tokens;

public class JSGLR2Success<AbstractSyntaxTree> extends JSGLR2Result<AbstractSyntaxTree> {

    public final Tokens tokens;
    public final AbstractSyntaxTree ast;

    JSGLR2Success(AbstractSyntaxTree ast, Tokens tokens, Collection<Message> messages) {
        super(messages);
        this.tokens = tokens;
        this.ast = ast;
    }

    public boolean isSuccess() {
        return true;
    }

}