package org.spoofax.jsglr2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.spoofax.jsglr2.imploder.IImplodeResult;
import org.spoofax.jsglr2.imploder.IImploder;
import org.spoofax.jsglr2.imploder.ITokenizer;
import org.spoofax.jsglr2.imploder.TokenizeResult;
import org.spoofax.jsglr2.messages.Message;
import org.spoofax.jsglr2.parseforest.IParseForest;
import org.spoofax.jsglr2.parser.IParser;
import org.spoofax.jsglr2.parser.result.ParseFailure;
import org.spoofax.jsglr2.parser.result.ParseResult;
import org.spoofax.jsglr2.parser.result.ParseSuccess;

public class JSGLR2Implementation<ParseForest extends IParseForest, IntermediateResult, ImploderCache, AbstractSyntaxTree, ImplodeResult extends IImplodeResult<IntermediateResult, ImploderCache, AbstractSyntaxTree>>
    implements JSGLR2<AbstractSyntaxTree> {

    public final IParser<ParseForest> parser;
    public final IImploder<ParseForest, IntermediateResult, ImploderCache, AbstractSyntaxTree, ImplodeResult> imploder;
    public final ITokenizer<IntermediateResult> tokenizer;

    public final HashMap<String, String> inputCache = new HashMap<>();
    public final HashMap<String, ParseForest> parseForestCache = new HashMap<>();
    public final HashMap<String, ImploderCache> imploderCacheCache = new HashMap<>();

    JSGLR2Implementation(IParser<ParseForest> parser,
        IImploder<ParseForest, IntermediateResult, ImploderCache, AbstractSyntaxTree, ImplodeResult> imploder,
        ITokenizer<IntermediateResult> tokenizer) {
        this.parser = parser;
        this.imploder = imploder;
        this.tokenizer = tokenizer;
    }

    @Override public JSGLR2Result<AbstractSyntaxTree> parseResult(String input, String fileName, String startSymbol) {
        String previousInput =
            !"".equals(fileName) && inputCache.containsKey(fileName) ? inputCache.get(fileName) : null;
        ParseForest previousParseForest =
            !"".equals(fileName) && parseForestCache.containsKey(fileName) ? parseForestCache.get(fileName) : null;
        ImploderCache previousImploderCache =
            !"".equals(fileName) && imploderCacheCache.containsKey(fileName) ? imploderCacheCache.get(fileName) : null;

        ParseResult<ParseForest> parseResult = parser.parse(input, startSymbol, previousInput, previousParseForest);

        if(parseResult.isSuccess()) {
            ParseSuccess<ParseForest> success = (ParseSuccess<ParseForest>) parseResult;

            ImplodeResult implodeResult = imploder.implode(input, fileName, success.parseResult, previousImploderCache);

            TokenizeResult tokenizeResult = tokenizer.tokenize(input, fileName, implodeResult.intermediateResult());

            List<Message> messages = new ArrayList<>();
            messages.addAll(parseResult.messages);
            messages.addAll(implodeResult.messages());
            messages.addAll(tokenizeResult.messages);

            if (!"".equals(fileName)) {
                inputCache.put(fileName, input);
                parseForestCache.put(fileName, success.parseResult);
                imploderCacheCache.put(fileName, implodeResult.resultCache());
            }

            return new JSGLR2Success<>(implodeResult.ast(), tokenizeResult.tokens, messages);
        } else {
            ParseFailure<ParseForest> failure = (ParseFailure<ParseForest>) parseResult;

            return new JSGLR2Failure<>(failure, parseResult.messages);
        }
    }

}
