package org.spoofax.jsglr2.imploder;

import java.util.Collections;

import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.jsglr2.parseforest.IParseForest;

public class NullStrategoImploder<ParseForest extends IParseForest>
    implements IImploder<ParseForest, Void, Void, IStrategoTerm, IImplodeResult<Void, Void, IStrategoTerm>> {

    @Override public IImplodeResult<Void, Void, IStrategoTerm> implode(String input, String fileName,
        ParseForest forest, Void resultCache) {
        return new ImplodeResult<>(null, null, null, Collections.emptyList());
    }

}
