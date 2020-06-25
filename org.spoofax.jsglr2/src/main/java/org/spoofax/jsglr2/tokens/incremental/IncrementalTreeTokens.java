package org.spoofax.jsglr2.tokens.incremental;

import java.util.WeakHashMap;

import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.jsglr2.JSGLR2Request;
import org.spoofax.jsglr2.imploder.TreeImploder;
import org.spoofax.jsglr2.tokens.treeshaped.TokenTree;
import org.spoofax.jsglr2.tokens.treeshaped.TreeTokens;

public class IncrementalTreeTokens extends TreeTokens {

    protected final WeakHashMap<TreeImploder.SubTree<IStrategoTerm>, TokenTree> resultCache = new WeakHashMap<>();

    // Shadows the `final` field of TreeTokens. Set in IncrementalTreeShapedTokenizer.tokenize .
    // This is because the incremental tokenizer needs to set the input string after every incremental run.
    private String input;

    public IncrementalTreeTokens(JSGLR2Request input) {
        super(input);
    }

    @Override public String getInput() {
        return input;
    }

    void setInput(String input) {
        this.input = input;
    }

}
