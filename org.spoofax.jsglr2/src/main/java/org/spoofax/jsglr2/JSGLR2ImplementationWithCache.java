package org.spoofax.jsglr2;

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

import java.util.HashMap;
import java.util.List;

public class JSGLR2ImplementationWithCache
// @formatter:off
   <ParseForest extends IParseForest,
    IntermediateResult,
    ImploderCache,
    AbstractSyntaxTree,
    ImplodeResult extends IImplodeResult<IntermediateResult, ImploderCache, AbstractSyntaxTree>,
    TokensResult extends ITokens>
// @formatter:on
        extends JSGLR2Implementation<ParseForest, IntermediateResult, ImploderCache, AbstractSyntaxTree, ImplodeResult, TokensResult> {
    JSGLR2ImplementationWithCache(IObservableParser<ParseForest, ?, ?, ?, ?> parser, IImploder<ParseForest, IntermediateResult, ImploderCache, AbstractSyntaxTree, ImplodeResult> imploder, ITokenizer<IntermediateResult, TokensResult> tokenizer) {
        super(parser, imploder, tokenizer);
    }

    public final HashMap<JSGLR2Request.CachingKey, String> inputCache = new HashMap<>();
    public final HashMap<JSGLR2Request.CachingKey, ParseForest> parseForestCache = new HashMap<>();
    public final HashMap<JSGLR2Request.CachingKey, ImploderCache> imploderCacheCache = new HashMap<>();
    public final HashMap<JSGLR2Request.CachingKey, TokensResult> tokensCache = new HashMap<>();

    @Override public JSGLR2Result<AbstractSyntaxTree> parseResult(JSGLR2Request request) {
        String previousInput = request.isCacheable() ? inputCache.get(request.cachingKey()) : null;
        ParseForest previousParseForest = request.isCacheable() ? parseForestCache.get(request.cachingKey()) : null;
        ImploderCache previousImploderCache =
                request.isCacheable() ? imploderCacheCache.get(request.cachingKey()) : null;
        TokensResult previousTokens = request.isCacheable() ? tokensCache.get(request.cachingKey()) : null;

        ParseResult<ParseForest> parseResult = parser.parse(request, previousInput, previousParseForest);

        if(parseResult.isSuccess()) {
            ParseForest parseForest = ((ParseSuccess<ParseForest>) parseResult).parseResult;

            ImplodeResult implodeResult = imploder.implode(request, parseForest, previousImploderCache);

            TokensResult tokens =
                    tokenizer.tokenize(request, implodeResult.intermediateResult(), previousTokens).tokens;

            List<Message> messages = postProcessMessages(parseResult.messages, tokens);

            if(request.isCacheable()) {
                inputCache.put(request.cachingKey(), request.input);
                parseForestCache.put(request.cachingKey(), parseForest);
                imploderCacheCache.put(request.cachingKey(), implodeResult.resultCache());
                tokensCache.put(request.cachingKey(), tokens);
            }

            return new JSGLR2Success<>(request, implodeResult.ast(), tokens, implodeResult.isAmbiguous(), messages);
        } else {
            ParseFailure<ParseForest> failure = (ParseFailure<ParseForest>) parseResult;

            return new JSGLR2Failure<>(request, failure, parseResult.messages);
        }
    }
}
