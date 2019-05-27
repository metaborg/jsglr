package org.spoofax.jsglr2.imploder;

import org.spoofax.jsglr.client.imploder.ITokens;

public class TokenizeResult<Tokens extends ITokens> {

    public final Tokens tokens;

    public TokenizeResult(Tokens tokens) {
        this.tokens = tokens;
    }

}
