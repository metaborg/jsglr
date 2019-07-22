package org.spoofax.jsglr2;

import org.spoofax.jsglr2.imploder.IImploder;
import org.spoofax.jsglr2.imploder.ITokenizer;
import org.spoofax.jsglr2.imploder.TokenizeResult;
import org.spoofax.jsglr2.parseforest.IParseForest;
import org.spoofax.jsglr2.parser.IParser;
import org.spoofax.jsglr2.parser.result.ParseFailure;
import org.spoofax.jsglr2.parser.result.ParseResult;
import org.spoofax.jsglr2.parser.result.ParseSuccess;

public class JSGLR2Implementation<ParseForest extends IParseForest, ImplodeResult, AbstractSyntaxTree>
    implements JSGLR2<AbstractSyntaxTree> {

    public final IParser<ParseForest> parser;
    public final IImploder<ParseForest, ImplodeResult> imploder;
    public final ITokenizer<ImplodeResult, AbstractSyntaxTree> tokenizer;

    JSGLR2Implementation(IParser<ParseForest> parser, IImploder<ParseForest, ImplodeResult> imploder,
        ITokenizer<ImplodeResult, AbstractSyntaxTree> tokenizer) {
        this.parser = parser;
        this.imploder = imploder;
        this.tokenizer = tokenizer;
    }

    @Override public JSGLR2Result<AbstractSyntaxTree> parseResult(String input, String filename, String startSymbol) {
        ParseResult<ParseForest> parseResult = parser.parse(input, filename, startSymbol);

        if(parseResult.isSuccess()) {
            ParseSuccess<ParseForest> success = (ParseSuccess<ParseForest>) parseResult;

            ImplodeResult implodeResult = imploder.implode(input, filename, success.parseResult);

            TokenizeResult<AbstractSyntaxTree> tokenizeResult = tokenizer.tokenize(input, filename, implodeResult);

            return new JSGLR2Success<>(tokenizeResult);
        } else {
            ParseFailure<ParseForest> failure = (ParseFailure<ParseForest>) parseResult;

            return new JSGLR2Failure<>(failure);
        }
    }

}
