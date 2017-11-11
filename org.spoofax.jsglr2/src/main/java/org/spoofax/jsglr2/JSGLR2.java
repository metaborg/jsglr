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

public class JSGLR2<StackNode extends AbstractStackNode<ParseForest>, ParseForest extends AbstractParseForest, AbstractSyntaxTree> {

    public IParser<StackNode, ParseForest> parser;
    public IImploder<StackNode, ParseForest, AbstractSyntaxTree> imploder;

    public static JSGLR2<AbstractElkhoundStackNode<HybridParseForest>, HybridParseForest, IStrategoTerm>
        standard(IParseTable parseTable) throws ParseTableReadException {
        return (JSGLR2<AbstractElkhoundStackNode<HybridParseForest>, HybridParseForest, IStrategoTerm>) JSGLR2Variants
            .getJSGLR2(parseTable, ParseForestRepresentation.Hybrid, ParseForestConstruction.Optimized,
                StackRepresentation.HybridElkhound, Reducing.Elkhound);
    }

    public static JSGLR2<AbstractElkhoundStackNode<HybridParseForest>, HybridParseForest, IStrategoTerm>
        standard(IStrategoTerm parseTableTerm) throws ParseTableReadException {
        IParseTable parseTable = new ParseTableReader().read(parseTableTerm);

        return standard(parseTable);
    }

    public JSGLR2(IParser<StackNode, ParseForest> parser,
        IImploder<StackNode, ParseForest, AbstractSyntaxTree> imploder) {
        this.parser = parser;
        this.imploder = imploder;
    }

    public AbstractSyntaxTree parse(String input, String filename, String startSymbol) {
        ParseResult<StackNode, ParseForest, ?> parseResult = parser.parse(input, filename, startSymbol);

        if(!parseResult.isSuccess)
            return null;

        ParseSuccess<StackNode, ParseForest, ?> parseSuccess = (ParseSuccess<StackNode, ParseForest, ?>) parseResult;

        ImplodeResult<StackNode, ParseForest, AbstractSyntaxTree> implodeResult =
            imploder.implode(parseSuccess.parse, parseSuccess.parseResult);

        return implodeResult.ast;
    }

    public JSGLR2Result<StackNode, ParseForest, AbstractSyntaxTree> parseResult(String input, String filename,
        String startSymbol) {
        ParseResult<StackNode, ParseForest, ?> parseResult = parser.parse(input, filename, startSymbol);

        if(!parseResult.isSuccess)
            return (JSGLR2Result<StackNode, ParseForest, AbstractSyntaxTree>) parseResult;

        ParseSuccess<StackNode, ParseForest, ?> parseSuccess = (ParseSuccess<StackNode, ParseForest, ?>) parseResult;

        ImplodeResult<StackNode, ParseForest, AbstractSyntaxTree> implodeResult =
            imploder.implode(parseSuccess.parse, parseSuccess.parseResult);

        return implodeResult;
    }

    public AbstractSyntaxTree parse(String input) {
        return parse(input, "", null);
    }

    public AbstractSyntaxTree parseUnsafe(String input, String filename, String startSymbol) throws ParseException {
        ParseResult<StackNode, ParseForest, ?> result = parser.parse(input, filename, startSymbol);

        if(result.isSuccess) {
            ParseSuccess<StackNode, ParseForest, ?> success = (ParseSuccess<StackNode, ParseForest, ?>) result;

            ImplodeResult<StackNode, ParseForest, AbstractSyntaxTree> implodeResult =
                imploder.implode(success.parse, success.parseResult);

            return implodeResult.ast;
        } else {
            ParseFailure<StackNode, ParseForest, ?> failure = (ParseFailure<StackNode, ParseForest, ?>) result;

            throw failure.parseException;
        }
    }

    public AbstractSyntaxTree parseUnsafe(String input) throws ParseException {
        return parseUnsafe(input, "", null);
    }

}
