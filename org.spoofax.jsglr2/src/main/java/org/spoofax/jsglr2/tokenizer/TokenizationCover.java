package org.spoofax.jsglr2.tokenizer;

import org.spoofax.jsglr.client.imploder.IToken;

public final class TokenizationCover {

    public final IToken firstToken, lastToken;

    public TokenizationCover(IToken firstToken, IToken lastToken) {
        this.firstToken = firstToken;
        this.lastToken = lastToken;
    }

}
