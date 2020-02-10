package org.spoofax.jsglr2.tokens;

import java.util.Collections;

import org.spoofax.jsglr2.imploder.ITokenizer;
import org.spoofax.jsglr2.imploder.TokenizeResult;

/**
 * Tokenizer to use in conjunction with the TokenizedTreeImploder. That imploder already returns a Tokens object as
 * "intermediate result", so this tokenizer can simply use this Tokens object in the TokenizeResult.
 */
public class StubTokenizer implements ITokenizer<Tokens> {

    @Override public TokenizeResult tokenize(String input, String fileName, Tokens tokens) {
        return new TokenizeResult(tokens, Collections.emptyList());
    }

}
