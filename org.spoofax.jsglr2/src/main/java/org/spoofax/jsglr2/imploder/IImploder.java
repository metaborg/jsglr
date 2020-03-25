package org.spoofax.jsglr2.imploder;

import org.spoofax.jsglr2.JSGLR2Request;
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

    Result implode(JSGLR2Request request, ParseForest parseForest, Cache resultCache);

    default Result implode(String input, String fileName, ParseForest parseForest, Cache resultCache) {
        return implode(new JSGLR2Request(input, fileName), parseForest, resultCache);
    }

    default Result implode(JSGLR2Request request, ParseForest parseForest) {
        return implode(request, parseForest, null);
    }

    default Result implode(String input, String fileName, ParseForest parseForest) {
        return implode(new JSGLR2Request(input, fileName), parseForest);
    }

}
