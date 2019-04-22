package org.spoofax.jsglr2.imploder;

import org.spoofax.jsglr2.tokens.Tokens;

public interface ITokenizer<AbstractSyntaxTree> {

    AbstractSyntaxTree tokenize(Tokens tokens, TreeImploder.SubTree<AbstractSyntaxTree> tree);

    default ImplodeResult<AbstractSyntaxTree> tokenize(String input, String filename,
        TreeImploder.SubTree<AbstractSyntaxTree> tree) {
        Tokens tokens = new Tokens(input, filename);
        return new ImplodeResult<>(tokens, tokenize(tokens, tree));
    }

}
