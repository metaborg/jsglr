package org.spoofax.jsglr2;

import org.metaborg.parsetable.IParseTable;
import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.jsglr2.JSGLR2Variants.ParserVariant;
import org.spoofax.jsglr2.JSGLR2Variants.Variant;
import org.spoofax.jsglr2.imploder.ImploderVariant;
import org.spoofax.jsglr2.parseforest.ParseForestConstruction;
import org.spoofax.jsglr2.parseforest.ParseForestRepresentation;
import org.spoofax.jsglr2.parser.ParseException;
import org.metaborg.parsetable.ParseTableReadException;
import org.metaborg.parsetable.ParseTableReader;
import org.spoofax.jsglr2.reducing.Reducing;
import org.spoofax.jsglr2.stack.StackRepresentation;
import org.spoofax.jsglr2.stack.collections.ActiveStacksRepresentation;
import org.spoofax.jsglr2.stack.collections.ForActorStacksRepresentation;
import org.spoofax.jsglr2.tokens.TokenizerVariant;

public interface JSGLR2<AbstractSyntaxTree> {
    static JSGLR2<IStrategoTerm> standard(IParseTable parseTable) {
        //@formatter:off
        Variant variant = new Variant(
            new ParserVariant(
                ActiveStacksRepresentation.standard(),
                ForActorStacksRepresentation.standard(),
                ParseForestRepresentation.standard(),
                ParseForestConstruction.standard(),
                StackRepresentation.standard(),
                Reducing.standard()
            ),
            ImploderVariant.standard(),
            TokenizerVariant.standard()
        );
        //@formatter:off

        return JSGLR2Variants.getJSGLR2(parseTable, variant);
    }

    static JSGLR2<IStrategoTerm> dataDependent(IParseTable parseTable) {
        //@formatter:off
        Variant variant = new Variant(
            new ParserVariant(
                ActiveStacksRepresentation.standard(),
                ForActorStacksRepresentation.standard(),
                ParseForestRepresentation.DataDependent,
                ParseForestConstruction.standard(),
                StackRepresentation.Basic,
                Reducing.DataDependent
            ),
            ImploderVariant.standard(),
            TokenizerVariant.standard()
        );
        //@formatter:off

        return JSGLR2Variants.getJSGLR2(parseTable, variant);
    }

    static JSGLR2<IStrategoTerm> layoutSensitive(IParseTable parseTable) {
        //@formatter:off
        Variant variant = new Variant(
            new ParserVariant(
                ActiveStacksRepresentation.standard(),
                ForActorStacksRepresentation.standard(),
                ParseForestRepresentation.LayoutSensitive,
                ParseForestConstruction.standard(),
                StackRepresentation.Basic,
                Reducing.LayoutSensitive
            ),
            ImploderVariant.standard(),
            TokenizerVariant.standard()
        );
        //@formatter:off

        return JSGLR2Variants.getJSGLR2(parseTable, variant);
    }

    static JSGLR2<IStrategoTerm> incremental(IParseTable parseTable) {
        //@formatter:off
        Variant variant = new Variant(
            new ParserVariant(
                ActiveStacksRepresentation.standard(),
                ForActorStacksRepresentation.standard(),
                ParseForestRepresentation.Incremental,
                ParseForestConstruction.standard(),
                StackRepresentation.Basic,
                Reducing.Basic
            ),
            ImploderVariant.RecursiveIncremental,
            TokenizerVariant.Recursive
        );
        //@formatter:off

        return JSGLR2Variants.getJSGLR2(parseTable, variant);
    }

    static JSGLR2<IStrategoTerm> standard(IStrategoTerm parseTableTerm) throws ParseTableReadException {
        IParseTable parseTable = new ParseTableReader().read(parseTableTerm);

        return standard(parseTable);
    }

    JSGLR2Result<AbstractSyntaxTree> parseResult(String input, String filename, String startSymbol);

    default JSGLR2Result<AbstractSyntaxTree> parseResult(String input) {
        return parseResult(input, "", null);
    }

    default AbstractSyntaxTree parse(String input, String filename, String startSymbol) {
        JSGLR2Result<AbstractSyntaxTree> result = parseResult(input, filename, startSymbol);

        if (result.isSuccess())
            return ((JSGLR2Success<AbstractSyntaxTree>) result).ast;
        else
            return null;
    }

    default AbstractSyntaxTree parse(String input) {
        return parse(input, "", null);
    }

    default AbstractSyntaxTree parseUnsafe(String input, String filename, String startSymbol) throws ParseException {
        JSGLR2Result<AbstractSyntaxTree> result = parseResult(input, filename, startSymbol);

        if (result.isSuccess())
            return ((JSGLR2Success<AbstractSyntaxTree>) result).ast;
        else
            throw ((JSGLR2Failure<AbstractSyntaxTree>) result).parseFailure.exception();
    }

    default AbstractSyntaxTree parseUnsafe(String input) throws ParseException {
        return parseUnsafe(input, "", null);
    }
}
