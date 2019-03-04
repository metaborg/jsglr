package org.spoofax.jsglr2.imploder;

import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.jsglr2.parseforest.hybrid.HybridParseForest;

public class NullParseForestStrategoImploder implements IImploder<HybridParseForest, IStrategoTerm> {

    @Override public ImplodeResult<IStrategoTerm> implode(String input, String filename, HybridParseForest forest) {
        return null;
    }

}
