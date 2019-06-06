package org.spoofax.jsglr2.imploder;

public interface ITokenizer<ImplodeResult, AbstractSyntaxTree> {

    TokenizeResult<AbstractSyntaxTree> tokenize(String input, String filename, ImplodeResult tree);

}
