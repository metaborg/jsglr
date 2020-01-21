package org.spoofax.jsglr2;

import java.util.ArrayList;
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

    JSGLR2Implementation(IParser<ParseForest> parser,
        IImploder<ParseForest, IntermediateResult, ImploderCache, AbstractSyntaxTree, ImplodeResult> imploder,
        ITokenizer<IntermediateResult> tokenizer) {
        this.parser = parser;
        this.imploder = imploder;
        this.tokenizer = tokenizer;
    }

    @Override public JSGLR2Result<AbstractSyntaxTree> parseResult(String input, String fileName, String startSymbol) {
        ParseResult<ParseForest> parseResult = parser.parse(input, fileName, startSymbol);

        if(parseResult.isSuccess()) {
            ParseSuccess<ParseForest> success = (ParseSuccess<ParseForest>) parseResult;

            ImplodeResult implodeResult = imploder.implode(input, fileName, success.parseResult);

            TokenizeResult tokenizeResult = tokenizer.tokenize(input, fileName, implodeResult.intermediateResult());

            List<Message> messages = new ArrayList<>();

            messages.addAll(parseResult.messages);
            messages.addAll(implodeResult.messages());
            messages.addAll(tokenizeResult.messages);

            return new JSGLR2Success<>(implodeResult.ast(), tokenizeResult.tokens, messages);
        } else {
            ParseFailure<ParseForest> failure = (ParseFailure<ParseForest>) parseResult;

            return new JSGLR2Failure<>(failure, parseResult.messages);
        }
    }

}
