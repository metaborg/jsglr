package org.spoofax.jsglr2;

import java.util.HashMap;
import java.util.List;

import org.spoofax.jsglr.client.imploder.ITokens;
import org.spoofax.jsglr2.imploder.IImplodeResult;
import org.spoofax.jsglr2.imploder.IImploder;
import org.spoofax.jsglr2.imploder.ITokenizer;
import org.spoofax.jsglr2.messages.Message;
import org.spoofax.jsglr2.parseforest.IParseForest;
import org.spoofax.jsglr2.parser.IObservableParser;
import org.spoofax.jsglr2.parser.result.ParseFailure;
import org.spoofax.jsglr2.parser.result.ParseResult;
import org.spoofax.jsglr2.parser.result.ParseSuccess;

public class JSGLR2ImplementationWithCache
// @formatter:off
   <ParseForest extends IParseForest,
    IntermediateResult,
    ImploderCache,
    AbstractSyntaxTree,
    ImplodeResult extends IImplodeResult<IntermediateResult, ImploderCache, AbstractSyntaxTree>,
    TokensResult extends ITokens>
// @formatter:on
    extends
    JSGLR2Implementation<ParseForest, IntermediateResult, ImploderCache, AbstractSyntaxTree, ImplodeResult, TokensResult> {
    JSGLR2ImplementationWithCache(IObservableParser<ParseForest, ?, ?, ?, ?> parser,
        IImploder<ParseForest, IntermediateResult, ImploderCache, AbstractSyntaxTree, ImplodeResult> imploder,
        ITokenizer<IntermediateResult, TokensResult> tokenizer) {
        super(parser, imploder, tokenizer);
    }

    public final HashMap<JSGLR2Request.CachingKey, String> inputCache = new HashMap<>();
    public final HashMap<JSGLR2Request.CachingKey, ParseForest> parseForestCache = new HashMap<>();
    public final HashMap<JSGLR2Request.CachingKey, ImploderCache> imploderCacheCache = new HashMap<>();
    public final HashMap<JSGLR2Request.CachingKey, TokensResult> tokensCache = new HashMap<>();

    @Override public JSGLR2Result<AbstractSyntaxTree> parseResult(JSGLR2Request request) {
        JSGLR2Request.CachingKey cachingKey = request.isCacheable() ? request.cachingKey() : null;
        // The "previous" values will be `null` if `cachingKey == null`
        String previousInput = inputCache.get(cachingKey);
        ParseForest previousParseForest = parseForestCache.get(cachingKey);
        ImploderCache previousImploderCache = imploderCacheCache.get(cachingKey);
        TokensResult previousTokens = tokensCache.get(cachingKey);

        ParseResult<ParseForest> parseResult = parser.parse(request, previousInput, previousParseForest);

        if(parseResult.isSuccess()) {
            ParseForest parseForest = ((ParseSuccess<ParseForest>) parseResult).parseResult;

            ImplodeResult implodeResult = imploder.implode(request, parseForest, previousImploderCache);

            TokensResult tokens =
                tokenizer.tokenize(request, implodeResult.intermediateResult(), previousTokens).tokens;

            List<Message> messages = postProcessMessages(request.input, parseResult.messages, tokens);

            if(cachingKey != null) {
                inputCache.put(cachingKey, request.input);
                parseForestCache.put(cachingKey, parseForest);
                imploderCacheCache.put(cachingKey, implodeResult.resultCache());
                tokensCache.put(cachingKey, tokens);
            }

            return new JSGLR2Success<>(request, implodeResult.ast(), tokens, implodeResult.isAmbiguous(), messages);
        } else {
            ParseFailure<ParseForest> failure = (ParseFailure<ParseForest>) parseResult;

            return new JSGLR2Failure<>(request, failure, parseResult.messages);
        }
    }

    public void clearCache() {
        inputCache.clear();
        parseForestCache.clear();
        imploderCacheCache.clear();
        tokensCache.clear();
    }
}
