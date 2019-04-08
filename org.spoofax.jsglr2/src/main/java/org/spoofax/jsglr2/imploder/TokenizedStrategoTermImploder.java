package org.spoofax.jsglr2.imploder;

import static org.spoofax.interpreter.terms.IStrategoTerm.MUTABLE;

import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.jsglr.client.imploder.IToken;
import org.spoofax.jsglr2.parseforest.IDerivation;
import org.spoofax.jsglr2.parseforest.IParseForest;
import org.spoofax.jsglr2.parseforest.IParseNode;
import org.spoofax.terms.TermFactory;

public class TokenizedStrategoTermImploder
//@formatter:off
   <ParseForest extends IParseForest,
    ParseNode   extends IParseNode<ParseForest, Derivation>,
    Derivation  extends IDerivation<ParseForest>>
//@formatter:on
    extends TokenizedTreeImploder<ParseForest, ParseNode, Derivation, IStrategoTerm> {

    public TokenizedStrategoTermImploder() {
        super(new TokenizedTermTreeFactory(new TermFactory().getFactoryWithStorageType(MUTABLE)));
    }

    @Override protected void tokenTreeBinding(IToken token, IStrategoTerm term) {
        token.setAstNode(term);
    }

}
