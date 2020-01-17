package org.spoofax.jsglr2.imploder;

import javax.annotation.Nullable;

import org.apache.commons.vfs2.FileObject;
import org.spoofax.jsglr2.parseforest.IParseForest;

public interface IImploder<ParseForest extends IParseForest, IntermediateResult, Tree, Result extends IImplodeResult<IntermediateResult, Tree>> {

    Result implode(String input, @Nullable FileObject resource, ParseForest parseForest);

}
