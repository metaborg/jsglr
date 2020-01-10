package org.spoofax.jsglr2.imploder;

import javax.annotation.Nullable;

import org.apache.commons.vfs2.FileObject;
import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.jsglr2.parseforest.IParseForest;

public class NullStrategoImploder<ParseForest extends IParseForest>
    implements IImploder<ParseForest, TokenizeResult<IStrategoTerm>> {

    @Override public TokenizeResult<IStrategoTerm> implode(String input, @Nullable FileObject resource,
        ParseForest forest) {
        return new TokenizeResult<>(resource, null, null);
    }

}
