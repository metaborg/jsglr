package org.spoofax.jsglr2.imploder;

import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.jsglr2.imploder.treefactory.StrategoTermTreeFactory;
import org.spoofax.jsglr2.parseforest.IDerivation;
import org.spoofax.jsglr2.parseforest.IParseForest;
import org.spoofax.jsglr2.parseforest.IParseNode;

public class IterativeStrategoTermImploder
//@formatter:off
   <ParseForest extends IParseForest,
    ParseNode   extends IParseNode<ParseForest, Derivation>,
    Derivation  extends IDerivation<ParseForest>>
//@formatter:on
    extends IterativeTreeImploder<ParseForest, ParseNode, Derivation, IStrategoTerm> {

    public IterativeStrategoTermImploder() {
        super(new StrategoTermTreeFactory());
    }

}
