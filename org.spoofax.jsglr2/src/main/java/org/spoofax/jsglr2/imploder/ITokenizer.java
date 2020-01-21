package org.spoofax.jsglr2.imploder;

public interface ITokenizer<ImplodeResult> {

    TokenizeResult tokenize(String input, String fileName, ImplodeResult tree);

}
