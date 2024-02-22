package org.spoofax.jsglr2.tokens;

import org.spoofax.jsglr2.JSGLR2Request;
import org.spoofax.jsglr2.imploder.ITokenizer;
import org.spoofax.jsglr2.imploder.TokenizeResult;

import mb.jsglr.shared.ITokens;

/**
 * Tokenizer to use in conjunction with the TokenizedTreeImploder. That imploder already returns a Tokens object as
 * "intermediate result", so this tokenizer can simply use this Tokens object in the TokenizeResult.
 */
public class StubTokenizer implements ITokenizer<Tokens, ITokens> {

    @Override public TokenizeResult<ITokens> tokenize(JSGLR2Request request, Tokens tokens, ITokens previousResult) {
        return new TokenizeResult<>(tokens);
    }

}
