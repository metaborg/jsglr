package org.spoofax.jsglr2.imploder;

import java.util.Collections;

import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.jsglr2.parseforest.IParseForest;

public class NullStrategoImploder<ParseForest extends IParseForest>
    implements IImploder<ParseForest, IStrategoTerm, IStrategoTerm, IImplodeResult<IStrategoTerm, IStrategoTerm>> {

    @Override public IImplodeResult<IStrategoTerm, IStrategoTerm> implode(String input, String fileName,
        ParseForest forest) {
        return new ImplodeResult<>(null, null, Collections.emptyList());
    }

}
