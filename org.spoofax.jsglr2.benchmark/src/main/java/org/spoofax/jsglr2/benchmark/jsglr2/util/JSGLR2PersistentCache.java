package org.spoofax.jsglr2.benchmark.jsglr2.util;

import org.spoofax.jsglr.client.imploder.ITokens;
import org.spoofax.jsglr2.JSGLR2Implementation;
import org.spoofax.jsglr2.JSGLR2Request;
import org.spoofax.jsglr2.imploder.IImplodeResult;
import org.spoofax.jsglr2.parseforest.IParseForest;
import org.spoofax.jsglr2.parser.ParseException;

/**
 * This wrapper for JSGLR2 reuses the same cache for every call to parse. Useful for benchmarking the parsing of one
 * specific version of the input while reusing the cache for the previous version every time.
 */
public class JSGLR2PersistentCache
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

    public final String previousInput;
    public final ParseForest previousParseForest;
    public final ImploderCache previousImploderCache;
    public final TokensResult previousTokensResult;
    private final String fileName;
    private final JSGLR2Request.CachingKey cachingKey;

    public JSGLR2PersistentCache(
        JSGLR2Implementation<ParseForest, IntermediateResult, ImploderCache, AbstractSyntaxTree, ImplodeResult, TokensResult> jsglr2Implementation,
        String input) throws ParseException {
        this.jsglr2Implementation = jsglr2Implementation;

        fileName = "" + System.nanoTime();
        JSGLR2Request request = new JSGLR2Request(input, fileName);
        cachingKey = request.cachingKey();
        jsglr2Implementation.parseUnsafe(request);

        previousInput = jsglr2Implementation.inputCache.get(cachingKey);
        previousParseForest = jsglr2Implementation.parseForestCache.get(cachingKey);
        previousImploderCache = jsglr2Implementation.imploderCacheCache.get(cachingKey);
        previousTokensResult = jsglr2Implementation.tokensCache.get(cachingKey);
    }

    public AbstractSyntaxTree parse(String input) throws ParseException {
        JSGLR2Request request = new JSGLR2Request(input, fileName);
        AbstractSyntaxTree res = jsglr2Implementation.parseUnsafe(request);

        jsglr2Implementation.inputCache.put(cachingKey, previousInput);
        jsglr2Implementation.parseForestCache.put(cachingKey, previousParseForest);
        jsglr2Implementation.imploderCacheCache.put(cachingKey, previousImploderCache);
        jsglr2Implementation.tokensCache.put(cachingKey, previousTokensResult);

        return res;
    }

}
