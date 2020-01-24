package org.spoofax.jsglr2.imploder;

import org.spoofax.jsglr2.parseforest.IParseForest;

public interface IImploder
//@formatter:off
   <ParseForest extends IParseForest,
    IntermediateResult,
    Cache,
    Tree,
    Result extends IImplodeResult<IntermediateResult, Cache, Tree>>
//@formatter:on
{

    Result implode(String input, String fileName, ParseForest parseForest, Cache resultCache);

    default Result implode(String input, String fileName, ParseForest parseForest) {
        return implode(input, fileName, parseForest, null);
    }

}
