package org.spoofax.jsglr2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.spoofax.jsglr.client.imploder.IToken;
import org.spoofax.jsglr2.imploder.IImplodeResult;
import org.spoofax.jsglr2.imploder.IImploder;
import org.spoofax.jsglr2.imploder.ITokenizer;
import org.spoofax.jsglr2.imploder.TokenizeResult;
import org.spoofax.jsglr2.messages.Message;
import org.spoofax.jsglr2.messages.SourceRegion;
import org.spoofax.jsglr2.parseforest.IParseForest;
import org.spoofax.jsglr2.parser.IObservableParser;
import org.spoofax.jsglr2.parser.observing.IParserObserver;
import org.spoofax.jsglr2.parser.result.ParseFailure;
import org.spoofax.jsglr2.parser.result.ParseResult;
import org.spoofax.jsglr2.parser.result.ParseSuccess;
import org.spoofax.jsglr2.tokens.Tokens;

public class JSGLR2Implementation<ParseForest extends IParseForest, IntermediateResult, ImploderCache, AbstractSyntaxTree, ImplodeResult extends IImplodeResult<IntermediateResult, ImploderCache, AbstractSyntaxTree>>
    implements JSGLR2<AbstractSyntaxTree> {

    public final IObservableParser<ParseForest, ?, ?, ?, ?> parser;
    public final IImploder<ParseForest, IntermediateResult, ImploderCache, AbstractSyntaxTree, ImplodeResult> imploder;
    public final ITokenizer<IntermediateResult> tokenizer;

    public final HashMap<String, String> inputCache = new HashMap<>();
    public final HashMap<String, ParseForest> parseForestCache = new HashMap<>();
    public final HashMap<String, ImploderCache> imploderCacheCache = new HashMap<>();

    JSGLR2Implementation(IObservableParser<ParseForest, ?, ?, ?, ?> parser,
        IImploder<ParseForest, IntermediateResult, ImploderCache, AbstractSyntaxTree, ImplodeResult> imploder,
        ITokenizer<IntermediateResult> tokenizer) {
        this.parser = parser;
        this.imploder = imploder;
        this.tokenizer = tokenizer;
    }

    @Override public void attachObserver(IParserObserver observer) {
        parser.observing().attachObserver(observer);
    }

    @Override public JSGLR2Result<AbstractSyntaxTree> parseResult(JSGLR2Request request) {
        String previousInput = !"".equals(request.fileName) && inputCache.containsKey(request.fileName)
            ? inputCache.get(request.fileName) : null;
        ParseForest previousParseForest = !"".equals(request.fileName) && parseForestCache.containsKey(request.fileName)
            ? parseForestCache.get(request.fileName) : null;
        ImploderCache previousImploderCache =
            !"".equals(request.fileName) && imploderCacheCache.containsKey(request.fileName)
                ? imploderCacheCache.get(request.fileName) : null;

        ParseResult<ParseForest> parseResult = parser.parse(request, previousInput, previousParseForest);

        if(parseResult.isSuccess()) {
            ParseSuccess<ParseForest> success = (ParseSuccess<ParseForest>) parseResult;

            ImplodeResult implodeResult = imploder.implode(request, success.parseResult, previousImploderCache);

            TokenizeResult tokenizeResult = tokenizer.tokenize(request, implodeResult.intermediateResult());

            List<Message> messages = new ArrayList<>();
            messages.addAll(parseResult.messages);
            messages.addAll(implodeResult.messages());
            messages.addAll(tokenizeResult.messages);

            messages = postProcessMessages(messages, tokenizeResult.tokens);

            if(!"".equals(request.fileName)) {
                inputCache.put(request.fileName, request.input);
                parseForestCache.put(request.fileName, success.parseResult);
                imploderCacheCache.put(request.fileName, implodeResult.resultCache());
            }

            return new JSGLR2Success<>(implodeResult.ast(), tokenizeResult.tokens, messages);
        } else {
            ParseFailure<ParseForest> failure = (ParseFailure<ParseForest>) parseResult;

            return new JSGLR2Failure<>(failure, parseResult.messages);
        }
    }

    private List<Message> postProcessMessages(List<Message> originalMessages, Tokens tokens) {
        List<Message> messages = new ArrayList<>();

        for(Message originalMessage : originalMessages) {
            Message message = originalMessage;
            IToken token = tokens.getTokenAtOffset(originalMessage.region.startOffset);
            IToken precedingToken =
                (token != null && token.getIndex() > 0) ? tokens.getTokenAt(token.getIndex() - 1) : null;

            if(precedingToken != null && precedingToken.getKind() == IToken.TK_LAYOUT) {
                message = message.atRegion(SourceRegion.fromToken(precedingToken));
            }

            // TODO: prevent multiple/overlapping recovery messages on the same region

            messages.add(message);
        }

        return messages;
    }

}
