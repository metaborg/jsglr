package org.spoofax.jsglr2.tokens;

import org.spoofax.jsglr2.imploder.ITokenizer;
import org.spoofax.jsglr2.imploder.TokenizeResult;

public class NullTokenizer<Tree> implements ITokenizer<TokenizeResult<Tree>, Tree> {

    @Override public TokenizeResult<Tree> tokenize(String input, String filename, TokenizeResult<Tree> tree) {
        return tree;
    }

}
