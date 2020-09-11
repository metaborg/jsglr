package org.spoofax.jsglr2.benchmark.jsglr2.util;

import org.spoofax.jsglr.client.imploder.ITokens;
import org.spoofax.jsglr2.JSGLR2Implementation;
import org.spoofax.jsglr2.JSGLR2Request;
import org.spoofax.jsglr2.imploder.IImplodeResult;
import org.spoofax.jsglr2.parseforest.IParseForest;
import org.spoofax.jsglr2.parser.ParseException;

/**
 * This wrapper for JSGLR2 clears the cache after every call to parse. When parsing multiple inputs, the results are
 * cached between subsequent inputs as normal.
 */
public class JSGLR2MultiParser
// @formatter:off
   <ParseForest extends IParseForest,
    IntermediateResult,
    ImploderCache,
    AbstractSyntaxTree,
    ImplodeResult extends IImplodeResult<IntermediateResult, ImploderCache, AbstractSyntaxTree>,
    TokensResult extends ITokens>
// @formatter:on
{
    public final JSGLR2Implementation<ParseForest, IntermediateResult, ImploderCache, AbstractSyntaxTree, ImplodeResult, TokensResult> jsglr2Implementation;

    public JSGLR2MultiParser(
        JSGLR2Implementation<ParseForest, IntermediateResult, ImploderCache, AbstractSyntaxTree, ImplodeResult, TokensResult> jsglr2Implementation)
        throws ParseException {
        this.jsglr2Implementation = jsglr2Implementation;
    }

    public AbstractSyntaxTree parse(String... inputs) throws ParseException {
        String fileName = "" + System.nanoTime();

        AbstractSyntaxTree res = null;

        for(String input : inputs) {
            res = jsglr2Implementation.parseUnsafe(new JSGLR2Request(input, fileName));
        }

        jsglr2Implementation.inputCache.clear();
        jsglr2Implementation.parseForestCache.clear();
        jsglr2Implementation.imploderCacheCache.clear();
        jsglr2Implementation.tokensCache.clear();

        return res;
    }

}
