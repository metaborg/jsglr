package org.spoofax.jsglr2;

import org.spoofax.jsglr2.imploder.IImploder;
import org.spoofax.jsglr2.imploder.ImplodeResult;
import org.spoofax.jsglr2.parseforest.IParseForest;
import org.spoofax.jsglr2.parser.IParser;
import org.spoofax.jsglr2.parser.ParseException;
import org.spoofax.jsglr2.parser.result.ParseFailure;
import org.spoofax.jsglr2.parser.result.ParseResult;
import org.spoofax.jsglr2.parser.result.ParseSuccess;

class JSGLR2Implementation<ParseForest extends IParseForest, AbstractSyntaxTree> implements JSGLR2<AbstractSyntaxTree> {

    private final IParser<ParseForest> parser;
    private final IImploder<ParseForest, AbstractSyntaxTree> imploder;

    JSGLR2Implementation(IParser<ParseForest> parser, IImploder<ParseForest, AbstractSyntaxTree> imploder) {
        this.parser = parser;
        this.imploder = imploder;
    }

    @Override public JSGLR2Result<AbstractSyntaxTree> parseUnsafeResult(String input, String filename,
        String startSymbol) throws ParseException {

        ParseResult<ParseForest> parseResult = parser.parse(input, filename, startSymbol);

        if(parseResult.isSuccess) {
            ParseSuccess<ParseForest> success = (ParseSuccess<ParseForest>) parseResult;

            ImplodeResult<AbstractSyntaxTree> implodeResult = imploder.implode(input, filename, success.parseResult);

            return new JSGLR2Result<>(implodeResult);
        } else {
            ParseFailure<ParseForest> failure = (ParseFailure<ParseForest>) parseResult;

            throw failure.exception();
        }
    }

}
