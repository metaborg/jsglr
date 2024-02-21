package org.spoofax.jsglr2.imploder;

import org.metaborg.parsetable.symbols.ISymbol;
import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.jsglr2.imploder.treefactory.TokenizedTermTreeFactory;

import jsglr.shared.IToken;

public class IterativeStrategoTermTokenizer extends IterativeTreeTokenizer<IStrategoTerm> {
    @Override protected void configure(IStrategoTerm term, String sort, IToken leftToken, IToken rightToken) {
        StrategoTermTokenizer.configureStatic(term, sort, leftToken, rightToken);
    }

    @Override protected void configureInjection(ISymbol lhs, IStrategoTerm term, boolean isBracket) {
        TokenizedTermTreeFactory.configureInjection(lhs, term, isBracket);
    }

    @Override protected void tokenTreeBinding(IToken token, IStrategoTerm term) {
        token.setAstNode(term);
    }
}
