package org.spoofax.jsglr2.imploder;

import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.jsglr2.imploder.input.ImplodeInput;
import org.spoofax.jsglr2.imploder.treefactory.StrategoTermTreeFactory;
import org.spoofax.jsglr2.parseforest.IDerivation;
import org.spoofax.jsglr2.parseforest.IParseForest;
import org.spoofax.jsglr2.parseforest.IParseNode;

public class IterativeStrategoTermImploder
//@formatter:off
   <ParseForest extends IParseForest,
    ParseNode   extends IParseNode<ParseForest, Derivation>,
    Derivation  extends IDerivation<ParseForest>,
    Cache>
//@formatter:on
    extends IterativeTreeImploder<ParseForest, ParseNode, Derivation, Cache, IStrategoTerm, ImplodeInput> {

    public IterativeStrategoTermImploder() {
        super(ImplodeInput::new, new StrategoTermTreeFactory());
    }

}
