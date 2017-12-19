package org.spoofax.jsglr2.imploder;

import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.jsglr2.parseforest.hybrid.HybridParseForest;
import org.spoofax.jsglr2.parser.Parse;

public class NullParseForestStrategoImploder implements IImploder<HybridParseForest, IStrategoTerm> {

    @Override
    public ImplodeResult<HybridParseForest, IStrategoTerm> implode(Parse<HybridParseForest, ?> parse,
        HybridParseForest parseForest) {
        return null;
    }

}
