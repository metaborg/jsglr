package org.spoofax.jsglr2.imploder;

import mb.jsglr.shared.ITokens;

public class TokenizeResult<Tokens extends ITokens> {

    public final Tokens tokens;

    public TokenizeResult(Tokens tokens) {
        this.tokens = tokens;
    }

}
