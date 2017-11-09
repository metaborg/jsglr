package org.spoofax.jsglr2.imploder;

import static org.spoofax.interpreter.terms.IStrategoTerm.MUTABLE;

import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.jsglr.client.imploder.IToken;
import org.spoofax.jsglr2.parseforest.AbstractParseForest;
import org.spoofax.jsglr2.parseforest.IDerivation;
import org.spoofax.jsglr2.stack.AbstractStackNode;
import org.spoofax.jsglr2.tokenizer.Tokenizer;
import org.spoofax.terms.TermFactory;

public abstract class StrategoTermImploder<StackNode extends AbstractStackNode<ParseForest>, ParseForest extends AbstractParseForest, ParseNode extends ParseForest, Derivation extends IDerivation<ParseForest>>
    extends TokenizedTreeImploder<StackNode, ParseForest, ParseNode, Derivation, IStrategoTerm> {

    public StrategoTermImploder(Tokenizer<ParseForest, ParseNode, Derivation> tokenizer) {
        super(new TermTreeFactory(new TermFactory().getFactoryWithStorageType(MUTABLE)), tokenizer);
    }

    protected void tokenTreeBinding(IToken token, IStrategoTerm term) {
        token.setAstNode(term);
    }

}
