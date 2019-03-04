package org.spoofax.jsglr2;

import org.spoofax.jsglr2.imploder.ImplodeResult;
import org.spoofax.jsglr2.parseforest.IParseForest;
import org.spoofax.jsglr2.parser.AbstractParse;
import org.spoofax.jsglr2.parser.result.ParseResult;
import org.spoofax.jsglr2.tokens.Tokens;

public final class JSGLR2Result<ParseForest extends IParseForest, AbstractSyntaxTree> {

    public final AbstractParse<ParseForest, ?> parse;
    public final boolean isSuccess;
    public final Tokens tokens;
    public final AbstractSyntaxTree ast;

    /**
     * Constructs a result in the case that the parse failed. There is no ImplodeResult, so the fields `tokens` and
     * `ast` are `null`.
     * 
     * @param parseResult
     *            The parse failure result.
     */
    protected JSGLR2Result(ParseResult<ParseForest> parseResult) {
        // Implementation note: it would be cleaner to only accept `ParseFailure` as argument to this constructor.
        // However, this would always require casting at the call site (after checking for `isSuccess`.
        // If we ever change to a language that has class matching (e.g. Scala), we can still change this.
        // (The same goes for the success constructor)
        this.parse = parseResult.parse;
        this.isSuccess = parseResult.isSuccess;
        this.tokens = null;
        this.ast = null;
    }

    /**
     * Constructs a result in the case that the parse succeeded.
     * 
     * @param parseResult
     *            The parse success result.
     * @param implodeResult
     *            The implode result.
     */
    protected JSGLR2Result(ParseResult<ParseForest> parseResult, ImplodeResult<AbstractSyntaxTree> implodeResult) {
        this.parse = parseResult.parse;
        this.isSuccess = parseResult.isSuccess;
        this.tokens = implodeResult.tokens;
        this.ast = implodeResult.ast;
    }

}
