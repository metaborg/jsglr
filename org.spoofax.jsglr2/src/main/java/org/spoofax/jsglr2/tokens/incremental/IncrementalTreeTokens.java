package org.spoofax.jsglr2.tokens.incremental;

import java.util.WeakHashMap;

import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.jsglr2.JSGLR2Request;
import org.spoofax.jsglr2.imploder.TokenizeResult;
import org.spoofax.jsglr2.imploder.TreeImploder;
import org.spoofax.jsglr2.parser.Position;
import org.spoofax.jsglr2.tokens.TreeTokens;

public class IncrementalTreeTokens extends TreeTokens {
    protected final WeakHashMap<TreeImploder.SubTree<IStrategoTerm>, TreeTokens.TokenTree> resultCache =
        new WeakHashMap<>();

    private String input; // Shadows the `final` field of TreeTokens. Set in Tokenizer.tokenize .

    public IncrementalTreeTokens(JSGLR2Request input) {
        super(input);
    }

    @Override public String getInput() {
        return input;
    }

    public static class Tokenizer extends TreeTokens.AbstractTokenizer<IncrementalTreeTokens> {

        private TreeTokens.Tokenizer regularTokenizer = new TreeTokens.Tokenizer();

        @Override public TokenizeResult<IncrementalTreeTokens> tokenize(JSGLR2Request input,
            TreeImploder.SubTree<IStrategoTerm> tree, IncrementalTreeTokens previousResult) {

            IncrementalTreeTokens tokens =
                !input.isCacheable() || previousResult == null ? new IncrementalTreeTokens(input) : previousResult;
            tokens.input = input.input;

            TokenTree tokenTree =
                (input.isCacheable() ? this : regularTokenizer).tokenizeInternal(tokens, tree, Position.START_POSITION);
            finalize(tree, tokens, tokenTree);

            return new TokenizeResult<>(tokens);
        }

        @Override public TreeTokens.TokenTree tokenizeInternal(TreeTokens tokens,
            TreeImploder.SubTree<IStrategoTerm> tree, Position pivotPosition) {
            IncrementalTreeTokens incrementalTreeTokens = ((IncrementalTreeTokens) tokens);
            if(incrementalTreeTokens.resultCache.containsKey(tree))
                return incrementalTreeTokens.resultCache.get(tree);

            TreeTokens.TokenTree result = super.tokenizeInternal(incrementalTreeTokens, tree, pivotPosition);

            incrementalTreeTokens.resultCache.put(tree, result);
            return result;
        }

    }

}
