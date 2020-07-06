package org.spoofax.jsglr2.tokens.treeshaped;

import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.jsglr2.JSGLR2Request;
import org.spoofax.jsglr2.imploder.TokenizeResult;
import org.spoofax.jsglr2.imploder.TreeImploder;
import org.spoofax.jsglr2.parser.Position;

public class TreeShapedTokenizer extends AbstractTreeShapedTokenizer<TreeTokens> {

    @Override public TokenizeResult<TreeTokens> tokenize(JSGLR2Request input, TreeImploder.SubTree<IStrategoTerm> tree,
        TreeTokens previousResult) {

        TreeTokens tokens = new TreeTokens(input);

        TokenTree tokenTree = tokenizeInternal(tokens, tree, Position.START_POSITION);
        finalize(tree, tokens, tokenTree);

        return new TokenizeResult<>(tokens);
    }

}
