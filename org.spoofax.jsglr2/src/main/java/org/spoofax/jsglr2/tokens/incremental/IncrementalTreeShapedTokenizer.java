package org.spoofax.jsglr2.tokens.incremental;

import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.jsglr2.JSGLR2Request;
import org.spoofax.jsglr2.imploder.TokenizeResult;
import org.spoofax.jsglr2.imploder.TreeImploder;
import org.spoofax.jsglr2.parser.Position;
import org.spoofax.jsglr2.tokens.treeshaped.AbstractTreeShapedTokenizer;
import org.spoofax.jsglr2.tokens.treeshaped.TokenTree;
import org.spoofax.jsglr2.tokens.treeshaped.TreeShapedTokenizer;
import org.spoofax.jsglr2.tokens.treeshaped.TreeTokens;

public class IncrementalTreeShapedTokenizer extends AbstractTreeShapedTokenizer<IncrementalTreeTokens> {

    private final TreeShapedTokenizer regularTokenizer = new TreeShapedTokenizer();

    @Override public TokenizeResult<IncrementalTreeTokens> tokenize(JSGLR2Request input,
        TreeImploder.SubTree<IStrategoTerm> tree, IncrementalTreeTokens previousResult) {

        IncrementalTreeTokens tokens =
            !input.isCacheable() || previousResult == null ? new IncrementalTreeTokens(input) : previousResult;
        tokens.setInput(input.input);

        TokenTree tokenTree =
            (input.isCacheable() ? this : regularTokenizer).tokenizeInternal(tokens, tree, Position.START_POSITION);
        finalize(tree, tokens, tokenTree);

        return new TokenizeResult<>(tokens);
    }

    @Override public TokenTree tokenizeInternal(TreeTokens tokens, TreeImploder.SubTree<IStrategoTerm> tree,
        Position pivotPosition) {
        IncrementalTreeTokens incrementalTreeTokens = ((IncrementalTreeTokens) tokens);
        if(incrementalTreeTokens.resultCache.containsKey(tree))
            return incrementalTreeTokens.resultCache.get(tree);

        TokenTree result = super.tokenizeInternal(incrementalTreeTokens, tree, pivotPosition);

        incrementalTreeTokens.resultCache.put(tree, result);
        return result;
    }

}
