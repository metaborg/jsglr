package org.spoofax.jsglr2.imploder;

import org.spoofax.jsglr2.JSGLR2Request;

import jsglr.shared.ITokens;

public interface ITokenizer<ImplodeIntermediateResult, TokensResult extends ITokens> {

    TokenizeResult<TokensResult> tokenize(JSGLR2Request request, ImplodeIntermediateResult tree,
        TokensResult previousResult);

    default TokenizeResult<TokensResult> tokenize(JSGLR2Request request, ImplodeIntermediateResult tree) {
        return tokenize(request, tree, null);
    }

}
