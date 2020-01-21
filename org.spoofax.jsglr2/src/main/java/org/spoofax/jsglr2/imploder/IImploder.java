package org.spoofax.jsglr2.imploder;

import org.spoofax.jsglr2.parseforest.IParseForest;

public interface IImploder<ParseForest extends IParseForest, IntermediateResult, Tree, Result extends IImplodeResult<IntermediateResult, Tree>> {

    Result implode(String input, String fileName, ParseForest parseForest);

}
