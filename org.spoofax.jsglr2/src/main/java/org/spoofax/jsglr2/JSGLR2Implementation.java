package org.spoofax.jsglr2;

import org.spoofax.jsglr.client.imploder.ITokens;
import org.spoofax.jsglr2.imploder.IImplodeResult;
import org.spoofax.jsglr2.imploder.IImploder;
import org.spoofax.jsglr2.imploder.ITokenizer;
import org.spoofax.jsglr2.parseforest.IParseForest;
import org.spoofax.jsglr2.parser.IObservableParser;
import org.spoofax.jsglr2.parser.observing.IParserObserver;
import org.spoofax.jsglr2.parser.result.ParseFailure;
import org.spoofax.jsglr2.parser.result.ParseResult;
import org.spoofax.jsglr2.parser.result.ParseSuccess;

public class JSGLR2Implementation
// @formatter:off
   <ParseForest extends IParseForest,
    IntermediateResult,
    ImploderCache,
    AbstractSyntaxTree,
    ImplodeResult extends IImplodeResult<IntermediateResult, ImploderCache, AbstractSyntaxTree>,
    TokensResult extends ITokens>
// @formatter:on
    implements JSGLR2<AbstractSyntaxTree> {

    public final IObservableParser<ParseForest, ?, ?, ?, ?> parser;
    public final IImploder<ParseForest, IntermediateResult, ImploderCache, AbstractSyntaxTree, ImplodeResult> imploder;
    public final ITokenizer<IntermediateResult, TokensResult> tokenizer;

    JSGLR2Implementation(IObservableParser<ParseForest, ?, ?, ?, ?> parser,
        IImploder<ParseForest, IntermediateResult, ImploderCache, AbstractSyntaxTree, ImplodeResult> imploder,
        ITokenizer<IntermediateResult, TokensResult> tokenizer) {
        this.parser = parser;
        this.imploder = imploder;
        this.tokenizer = tokenizer;
    }

    @Override public void attachObserver(IParserObserver observer) {
        parser.observing().attachObserver(observer);
    }

    @Override public JSGLR2Result<AbstractSyntaxTree> parseResult(JSGLR2Request request) {

        ParseResult<ParseForest> parseResult = parser.parse(request);

        if(parseResult.isSuccess()) {
            ParseForest parseForest = ((ParseSuccess<ParseForest>) parseResult).parseResult;

            ImplodeResult implodeResult = imploder.implode(request, parseForest);

            TokensResult tokens = tokenizer.tokenize(request, implodeResult.intermediateResult()).tokens;

            parseResult.postProcessMessages(tokens);

            return new JSGLR2Success<>(request, implodeResult.ast(), tokens, implodeResult.isAmbiguous(),
                parseResult.messages);
        } else {
            ParseFailure<ParseForest> failure = (ParseFailure<ParseForest>) parseResult;

            return new JSGLR2Failure<>(request, failure, parseResult.messages);
        }
    }

}
