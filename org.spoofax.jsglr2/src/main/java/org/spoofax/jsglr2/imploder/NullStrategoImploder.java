package org.spoofax.jsglr2.imploder;

import java.util.Collections;

import javax.annotation.Nullable;

import org.apache.commons.vfs2.FileObject;
import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.jsglr2.parseforest.IParseForest;

public class NullStrategoImploder<ParseForest extends IParseForest>
    implements IImploder<ParseForest, IStrategoTerm, IStrategoTerm, IImplodeResult<IStrategoTerm, IStrategoTerm>> {

    @Override public IImplodeResult<IStrategoTerm, IStrategoTerm> implode(String input, @Nullable FileObject resource,
        ParseForest forest) {
        return new ImplodeResult<>(resource, null, null, Collections.emptyList());
    }

}
