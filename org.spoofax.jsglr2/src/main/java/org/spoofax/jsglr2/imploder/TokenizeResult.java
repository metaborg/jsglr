package org.spoofax.jsglr2.imploder;

import org.spoofax.jsglr2.tokens.Tokens;

public class TokenizeResult<AbstractSyntaxTree> {

    public final Tokens tokens;
    public final AbstractSyntaxTree ast;

    public TokenizeResult(Tokens tokens, AbstractSyntaxTree ast) {
        this.tokens = tokens;
        this.ast = ast;
    }

}
