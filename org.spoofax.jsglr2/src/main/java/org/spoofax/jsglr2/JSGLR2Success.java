package org.spoofax.jsglr2;

import java.util.Collection;

import org.spoofax.jsglr2.messages.Message;

import mb.jsglr.shared.ITokens;

public class JSGLR2Success<AbstractSyntaxTree> extends JSGLR2Result<AbstractSyntaxTree> {

    public final ITokens tokens;
    public final AbstractSyntaxTree ast;
    private final boolean isAmbiguous;

    JSGLR2Success(JSGLR2Request request, AbstractSyntaxTree ast, ITokens tokens, boolean isAmbiguous,
        Collection<Message> messages) {
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
