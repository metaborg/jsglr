package org.spoofax.jsglr2;

import org.spoofax.jsglr2.imploder.TokenizeResult;
import org.spoofax.jsglr2.tokens.Tokens;

public class JSGLR2Success<AbstractSyntaxTree> extends JSGLR2Result<AbstractSyntaxTree> {

    public final Tokens tokens;
    public final AbstractSyntaxTree ast;

    JSGLR2Success(TokenizeResult<AbstractSyntaxTree> tokenizeResult) {
        this.tokens = tokenizeResult.tokens;
        this.ast = tokenizeResult.ast;
    }

    public boolean isSuccess() {
        return true;
    }

}