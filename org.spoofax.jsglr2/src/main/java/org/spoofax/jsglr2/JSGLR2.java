package org.spoofax.jsglr2;

import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.jsglr2.JSGLR2Variants.ParseForestConstruction;
import org.spoofax.jsglr2.JSGLR2Variants.ParseForestRepresentation;
import org.spoofax.jsglr2.JSGLR2Variants.Reducing;
import org.spoofax.jsglr2.JSGLR2Variants.StackRepresentation;
import org.spoofax.jsglr2.imploder.IImploder;
import org.spoofax.jsglr2.imploder.ImplodeResult;
import org.spoofax.jsglr2.parseforest.AbstractParseForest;
import org.spoofax.jsglr2.parseforest.hybrid.HybridParseForest;
import org.spoofax.jsglr2.parser.IParser;
import org.spoofax.jsglr2.parser.ParseException;
import org.spoofax.jsglr2.parser.ParseFailure;
import org.spoofax.jsglr2.parser.ParseResult;
import org.spoofax.jsglr2.parser.ParseSuccess;
import org.spoofax.jsglr2.parsetable.IParseTable;
import org.spoofax.jsglr2.parsetable.ParseTableReadException;
import org.spoofax.jsglr2.parsetable.ParseTableReader;
import org.spoofax.jsglr2.stack.AbstractStackNode;
import org.spoofax.jsglr2.stack.elkhound.AbstractElkhoundStackNode;

public class JSGLR2<ParseForest extends AbstractParseForest, StackNode extends AbstractStackNode<ParseForest>, AbstractSyntaxTree> {

    public IParser<ParseForest, StackNode> parser;
    public IImploder<ParseForest, StackNode, AbstractSyntaxTree> imploder;

    @SuppressWarnings("unchecked")
    public static JSGLR2<HybridParseForest, AbstractElkhoundStackNode<HybridParseForest>, IStrategoTerm>
        standard(IParseTable parseTable) throws ParseTableReadException {
        return (JSGLR2<HybridParseForest, AbstractElkhoundStackNode<HybridParseForest>, IStrategoTerm>) JSGLR2Variants
            .getJSGLR2(parseTable, ParseForestRepresentation.Hybrid, ParseForestConstruction.Optimized,
                StackRepresentation.HybridElkhound, Reducing.Elkhound);
    }

    public static JSGLR2<HybridParseForest, AbstractElkhoundStackNode<HybridParseForest>, IStrategoTerm>
        standard(IStrategoTerm parseTableTerm) throws ParseTableReadException {
        IParseTable parseTable = new ParseTableReader().read(parseTableTerm);

        return standard(parseTable);
    }

    public JSGLR2(IParser<ParseForest, StackNode> parser,
        IImploder<ParseForest, StackNode, AbstractSyntaxTree> imploder) {
        this.parser = parser;
        this.imploder = imploder;
    }

    public AbstractSyntaxTree parse(String input, String filename, String startSymbol) {
        ParseResult<ParseForest, StackNode, ?> parseResult = parser.parse(input, filename, startSymbol);

        if(!parseResult.isSuccess)
            return null;

        ParseSuccess<ParseForest, StackNode, ?> parseSuccess = (ParseSuccess<ParseForest, StackNode, ?>) parseResult;

        ImplodeResult<ParseForest, StackNode, AbstractSyntaxTree> implodeResult =
            imploder.implode(parseSuccess.parse, parseSuccess.parseResult);

        return implodeResult.ast;
    }

    @SuppressWarnings("unchecked")
    public JSGLR2Result<ParseForest, StackNode, AbstractSyntaxTree> parseResult(String input, String filename,
        String startSymbol) {
        ParseResult<ParseForest, StackNode, ?> parseResult = parser.parse(input, filename, startSymbol);

        if(!parseResult.isSuccess)
            return (JSGLR2Result<ParseForest, StackNode, AbstractSyntaxTree>) parseResult;

        ParseSuccess<ParseForest, StackNode, ?> parseSuccess = (ParseSuccess<ParseForest, StackNode, ?>) parseResult;

        ImplodeResult<ParseForest, StackNode, AbstractSyntaxTree> implodeResult =
            imploder.implode(parseSuccess.parse, parseSuccess.parseResult);

        return implodeResult;
    }

    public AbstractSyntaxTree parse(String input) {
        return parse(input, "", null);
    }

    public AbstractSyntaxTree parseUnsafe(String input, String filename, String startSymbol) throws ParseException {
        ParseResult<ParseForest, StackNode, ?> result = parser.parse(input, filename, startSymbol);

        if(result.isSuccess) {
            ParseSuccess<ParseForest, StackNode, ?> success = (ParseSuccess<ParseForest, StackNode, ?>) result;

            ImplodeResult<ParseForest, StackNode, AbstractSyntaxTree> implodeResult =
                imploder.implode(success.parse, success.parseResult);

            return implodeResult.ast;
        } else {
            ParseFailure<ParseForest, StackNode, ?> failure = (ParseFailure<ParseForest, StackNode, ?>) result;

            throw failure.parseException;
        }
    }

    public AbstractSyntaxTree parseUnsafe(String input) throws ParseException {
        return parseUnsafe(input, "", null);
    }

}
