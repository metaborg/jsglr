package org.spoofax.jsglr2.imploder;

import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.jsglr2.parseforest.IParseForest;

public class NullStrategoImploder<ParseForest extends IParseForest> implements IImploder<ParseForest, IStrategoTerm> {

    @Override public ImplodeResult<IStrategoTerm> implode(String input, String filename, ParseForest forest) {
        return null;
    }

}
