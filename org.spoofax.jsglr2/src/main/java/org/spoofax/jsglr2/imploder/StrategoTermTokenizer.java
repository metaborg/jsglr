package org.spoofax.jsglr2.imploder;

import org.metaborg.parsetable.symbols.ISymbol;
import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.jsglr.client.imploder.IToken;
import org.spoofax.jsglr2.imploder.treefactory.TokenizedTermTreeFactory;
import org.spoofax.terms.util.TermUtils;

public class StrategoTermTokenizer extends TreeTokenizer<IStrategoTerm> {
    @Override protected void configure(IStrategoTerm term, String sort, IToken leftToken, IToken rightToken) {
        configureStatic(term, sort, leftToken, rightToken);
    }

    public static void configureStatic(IStrategoTerm term, String sort, IToken leftToken, IToken rightToken) {
        TokenizedTermTreeFactory.configure(term, sort, leftToken, rightToken);
        if (TermUtils.isAppl(term)) {
            String name = TermUtils.toAppl(term).getName();
            if ("amb".equals(name)) {
                TokenizedTermTreeFactory.configure(term.getSubterm(0), null, leftToken, rightToken);
            }
            if ("meta-var".equals(name) || "meta-listvar".equals(name)) {
                TokenizedTermTreeFactory.configure(term.getSubterm(0), sort, leftToken, rightToken);
            }
        }
    }

    @Override protected void configureInjection(ISymbol lhs, IStrategoTerm term, boolean isBracket) {
        TokenizedTermTreeFactory.configureInjection(lhs, term, isBracket);
    }

    @Override protected void tokenTreeBinding(IToken token, IStrategoTerm term) {
        token.setAstNode(term);
    }
}
