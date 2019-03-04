package org.spoofax.jsglr2.imploder;

import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.jsglr2.parseforest.IParseForest;

public class NullParseForestStrategoImploder implements IImploder<IParseForest, IStrategoTerm> {

    @Override public ImplodeResult<IStrategoTerm> implode(String input, String filename, IParseForest forest) {
        return null;
    }

}
