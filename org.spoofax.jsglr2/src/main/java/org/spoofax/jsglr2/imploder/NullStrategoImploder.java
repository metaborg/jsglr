package org.spoofax.jsglr2.imploder;

import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.jsglr2.JSGLR2Request;
import org.spoofax.jsglr2.parseforest.IParseForest;

public class NullStrategoImploder<ParseForest extends IParseForest>
    implements IImploder<ParseForest, Void, Void, IStrategoTerm, IImplodeResult<Void, Void, IStrategoTerm>> {

    @Override public IImplodeResult<Void, Void, IStrategoTerm> implode(JSGLR2Request request, ParseForest forest,
        Void resultCache) {
        return new ImplodeResult<>(null, null, null);
    }

}
