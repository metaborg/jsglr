package org.spoofax.jsglr2;

import java.util.Collection;

import org.spoofax.jsglr2.messages.Message;
import org.spoofax.jsglr2.tokens.Tokens;

public class JSGLR2Success<AbstractSyntaxTree> extends JSGLR2Result<AbstractSyntaxTree> {

    public final Tokens tokens;
    public final AbstractSyntaxTree ast;
    private final boolean isAmbiguous;

    JSGLR2Success(JSGLR2Request request, AbstractSyntaxTree ast, Tokens tokens, boolean isAmbiguous, Collection<Message> messages) {
        super(request, messages);
        this.tokens = tokens;
        this.ast = ast;
        this.isAmbiguous = isAmbiguous;
    }

    public boolean isSuccess() {
        return true;
    }

    public boolean isAmbiguous() {
        return isAmbiguous;
    }

}