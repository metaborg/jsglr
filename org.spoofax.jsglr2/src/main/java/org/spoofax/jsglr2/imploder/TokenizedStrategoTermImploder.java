package org.spoofax.jsglr2.imploder;

import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.jsglr2.imploder.treefactory.TokenizedTermTreeFactory;
import org.spoofax.jsglr2.parseforest.IDerivation;
import org.spoofax.jsglr2.parseforest.IParseForest;
import org.spoofax.jsglr2.parseforest.IParseNode;

import mb.jsglr.shared.IToken;

public class TokenizedStrategoTermImploder
//@formatter:off
   <ParseForest extends IParseForest,
    ParseNode   extends IParseNode<ParseForest, Derivation>,
    Derivation  extends IDerivation<ParseForest>>
//@formatter:on
    extends TokenizedTreeImploder<ParseForest, ParseNode, Derivation, IStrategoTerm> {

    public TokenizedStrategoTermImploder() {
        super(new TokenizedTermTreeFactory());
    }

    @Override protected void tokenTreeBinding(IToken token, IStrategoTerm term) {
        token.setAstNode(term);
    }

}
