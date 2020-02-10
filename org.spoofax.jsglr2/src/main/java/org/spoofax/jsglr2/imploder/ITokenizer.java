package org.spoofax.jsglr2.imploder;

public interface ITokenizer<ImplodeIntermediateResult> {

    TokenizeResult tokenize(String input, String fileName, ImplodeIntermediateResult tree);

}
