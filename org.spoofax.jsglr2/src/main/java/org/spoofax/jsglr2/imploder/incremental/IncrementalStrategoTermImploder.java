package org.spoofax.jsglr2.imploder.incremental;

import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.jsglr2.imploder.treefactory.StrategoTermTreeFactory;
import org.spoofax.jsglr2.parseforest.IDerivation;
import org.spoofax.jsglr2.parseforest.IParseForest;
import org.spoofax.jsglr2.parseforest.IParseNode;

public class IncrementalStrategoTermImploder
//@formatter:off
   <ParseForest extends IParseForest,
    ParseNode   extends IParseNode<ParseForest, Derivation>,
    Derivation  extends IDerivation<ParseForest>,
    Cache       extends IncrementalTreeImploder.ResultCache<ParseForest, ParseNode, Derivation, IStrategoTerm>>
//@formatter:on
    extends
    IncrementalTreeImploder<ParseForest, ParseNode, Derivation, Cache, IStrategoTerm, IncrementalImplodeInput<ParseNode, Cache, IStrategoTerm>> {

    public IncrementalStrategoTermImploder() {
        super(inputString -> new IncrementalImplodeInput<>(inputString, null), IncrementalImplodeInput::new,
            new StrategoTermTreeFactory());
    }

}
