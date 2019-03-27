package org.spoofax.jsglr2;

import org.metaborg.characterclasses.CharacterClassFactory;
import org.metaborg.parsetable.IParseTable;
import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.jsglr2.JSGLR2Variants.ParserVariant;
import org.spoofax.jsglr2.actions.ActionsFactory;
import org.spoofax.jsglr2.imploder.IImploder;
import org.spoofax.jsglr2.imploder.ImplodeResult;
import org.spoofax.jsglr2.parseforest.IParseForest;
import org.spoofax.jsglr2.parseforest.ParseForestConstruction;
import org.spoofax.jsglr2.parseforest.ParseForestRepresentation;
import org.spoofax.jsglr2.parser.IParser;
import org.spoofax.jsglr2.parser.ParseException;
import org.spoofax.jsglr2.parser.result.ParseFailure;
import org.spoofax.jsglr2.parser.result.ParseResult;
import org.spoofax.jsglr2.parser.result.ParseSuccess;
import org.spoofax.jsglr2.parsetable.ParseTableReadException;
import org.spoofax.jsglr2.parsetable.ParseTableReader;
import org.spoofax.jsglr2.reducing.Reducing;
import org.spoofax.jsglr2.stack.StackRepresentation;
import org.spoofax.jsglr2.stack.collections.ActiveStacksRepresentation;
import org.spoofax.jsglr2.stack.collections.ForActorStacksRepresentation;
import org.spoofax.jsglr2.states.StateFactory;

public class JSGLR2<ParseForest extends IParseForest, AbstractSyntaxTree> {

    public final IParser<ParseForest, ?> parser;
    public final IImploder<ParseForest, AbstractSyntaxTree> imploder;

    public static JSGLR2<?, IStrategoTerm> standard(IParseTable parseTable) {
        return JSGLR2Variants.getJSGLR2(parseTable,
            new ParserVariant(ActiveStacksRepresentation.ArrayList, ForActorStacksRepresentation.ArrayDeque,
                ParseForestRepresentation.Hybrid, ParseForestConstruction.Full, StackRepresentation.HybridElkhound,
                Reducing.Elkhound));
    }

    public static JSGLR2<?, IStrategoTerm> dataDependent(IParseTable parseTable) {
        return JSGLR2Variants.getJSGLR2(parseTable,
            new ParserVariant(ActiveStacksRepresentation.ArrayList, ForActorStacksRepresentation.ArrayDeque,
                ParseForestRepresentation.DataDependent, ParseForestConstruction.Full, StackRepresentation.Basic,
                Reducing.DataDependent));
    }

    public static JSGLR2<?, IStrategoTerm> layoutSensitive(IParseTable parseTable) {
        return JSGLR2Variants.getJSGLR2(parseTable,
            new ParserVariant(ActiveStacksRepresentation.ArrayList, ForActorStacksRepresentation.ArrayDeque,
                ParseForestRepresentation.LayoutSensitive, ParseForestConstruction.Full, StackRepresentation.Basic,
                Reducing.DataDependent));
    }

    public static JSGLR2<?, IStrategoTerm> standard(IStrategoTerm parseTableTerm) throws ParseTableReadException {
        IParseTable parseTable =
            new ParseTableReader(new CharacterClassFactory(true, true), new ActionsFactory(true), new StateFactory())
                .read(parseTableTerm);

        return standard(parseTable);
    }

    public JSGLR2(IParser<ParseForest, ?> parser, IImploder<ParseForest, AbstractSyntaxTree> imploder) {
        this.parser = parser;
        this.imploder = imploder;
    }

    public AbstractSyntaxTree parse(String input, String filename, String startSymbol) {
        ParseResult<ParseForest> parseResult = parser.parse(input, filename, startSymbol);

        if(!parseResult.isSuccess)
            return null;

        ParseSuccess<ParseForest> parseSuccess = (ParseSuccess<ParseForest>) parseResult;

        ImplodeResult<AbstractSyntaxTree> implodeResult = imploder.implode(input, filename, parseSuccess.parseResult);

        return implodeResult.ast;
    }

    public JSGLR2Result<ParseForest, AbstractSyntaxTree> parseResult(String input, String filename,
        String startSymbol) {
        ParseResult<ParseForest> parseResult = parser.parse(input, filename, startSymbol);

        if(!parseResult.isSuccess)
            return new JSGLR2Result<>(parseResult);

        ParseSuccess<ParseForest> parseSuccess = (ParseSuccess<ParseForest>) parseResult;

        ImplodeResult<AbstractSyntaxTree> implodeResult = imploder.implode(input, filename, parseSuccess.parseResult);

        return new JSGLR2Result<>(parseResult, implodeResult);
    }

    public AbstractSyntaxTree parse(String input) {
        return parse(input, "", null);
    }

    public AbstractSyntaxTree parseUnsafe(String input, String filename, String startSymbol) throws ParseException {
        ParseResult<ParseForest> result = parser.parse(input, filename, startSymbol);

        if(result.isSuccess) {
            ParseSuccess<ParseForest> success = (ParseSuccess<ParseForest>) result;

            ImplodeResult<AbstractSyntaxTree> implodeResult = imploder.implode(input, filename, success.parseResult);

            return implodeResult.ast;
        } else {
            ParseFailure<ParseForest> failure = (ParseFailure<ParseForest>) result;

            throw failure.exception();
        }
    }

    public AbstractSyntaxTree parseUnsafe(String input) throws ParseException {
        return parseUnsafe(input, "", null);
    }

}
