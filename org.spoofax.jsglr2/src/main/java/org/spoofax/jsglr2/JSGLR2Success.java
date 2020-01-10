package org.spoofax.jsglr2;

import java.util.Collection;

import org.metaborg.core.messages.IMessage;
import org.spoofax.jsglr2.imploder.TokenizeResult;
import org.spoofax.jsglr2.tokens.Tokens;

public class JSGLR2Success<AbstractSyntaxTree> extends JSGLR2Result<AbstractSyntaxTree> {

    public final Tokens tokens;
    public final AbstractSyntaxTree ast;

    JSGLR2Success(TokenizeResult<AbstractSyntaxTree> tokenizeResult, Collection<IMessage> messages) {
        super(messages);
        this.tokens = tokenizeResult.tokens;
        this.ast = tokenizeResult.ast;
    }

    public boolean isSuccess() {
        return true;
    }

}