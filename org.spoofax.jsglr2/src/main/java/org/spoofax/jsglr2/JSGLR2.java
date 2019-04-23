package org.spoofax.jsglr2;

import org.metaborg.characterclasses.CharacterClassFactory;
import org.metaborg.parsetable.IParseTable;
import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.jsglr2.JSGLR2Variants.ParserVariant;
import org.spoofax.jsglr2.JSGLR2Variants.Variant;
import org.spoofax.jsglr2.actions.ActionsFactory;
import org.spoofax.jsglr2.imploder.ImploderVariant;
import org.spoofax.jsglr2.parseforest.ParseForestConstruction;
import org.spoofax.jsglr2.parseforest.ParseForestRepresentation;
import org.spoofax.jsglr2.parser.ParseException;
import org.spoofax.jsglr2.parsetable.ParseTableReadException;
import org.spoofax.jsglr2.parsetable.ParseTableReader;
import org.spoofax.jsglr2.reducing.Reducing;
import org.spoofax.jsglr2.stack.StackRepresentation;
import org.spoofax.jsglr2.stack.collections.ActiveStacksRepresentation;
import org.spoofax.jsglr2.stack.collections.ForActorStacksRepresentation;
import org.spoofax.jsglr2.states.StateFactory;

public interface JSGLR2<AbstractSyntaxTree> {
    static JSGLR2<IStrategoTerm> standard(IParseTable parseTable) {
        return JSGLR2Variants.getJSGLR2(parseTable,
            new Variant(new ParserVariant(ActiveStacksRepresentation.ArrayList, ForActorStacksRepresentation.ArrayDeque,
                ParseForestRepresentation.Hybrid, ParseForestConstruction.Full, StackRepresentation.HybridElkhound,
                Reducing.Elkhound), ImploderVariant.CombinedRecursive));
    }

    static JSGLR2<IStrategoTerm> dataDependent(IParseTable parseTable) {
        return JSGLR2Variants.getJSGLR2(parseTable,
            new Variant(new ParserVariant(ActiveStacksRepresentation.ArrayList, ForActorStacksRepresentation.ArrayDeque,
                ParseForestRepresentation.DataDependent, ParseForestConstruction.Full, StackRepresentation.Basic,
                Reducing.DataDependent), ImploderVariant.CombinedRecursive));
    }

    static JSGLR2<IStrategoTerm> layoutSensitive(IParseTable parseTable) {
        return JSGLR2Variants.getJSGLR2(parseTable,
            new Variant(new ParserVariant(ActiveStacksRepresentation.ArrayList, ForActorStacksRepresentation.ArrayDeque,
                ParseForestRepresentation.LayoutSensitive, ParseForestConstruction.Full, StackRepresentation.Basic,
                Reducing.DataDependent), ImploderVariant.CombinedRecursive));
    }

    static JSGLR2<IStrategoTerm> incremental(IParseTable parseTable) {
        return JSGLR2Variants.getJSGLR2(parseTable,
            new Variant(new ParserVariant(ActiveStacksRepresentation.ArrayList, ForActorStacksRepresentation.ArrayDeque,
                ParseForestRepresentation.Incremental, ParseForestConstruction.Full, StackRepresentation.Basic,
                Reducing.Basic), ImploderVariant.CombinedRecursive));
    }

    static JSGLR2<IStrategoTerm> standard(IStrategoTerm parseTableTerm) throws ParseTableReadException {
        IParseTable parseTable =
            new ParseTableReader(new CharacterClassFactory(true, true), new ActionsFactory(true), new StateFactory())
                .read(parseTableTerm);

        return standard(parseTable);
    }

    default AbstractSyntaxTree parse(String input) {
        return parse(input, "", null);
    }

    default AbstractSyntaxTree parse(String input, String filename, String startSymbol) {
        return parseResult(input, filename, startSymbol).ast;
    }

    default JSGLR2Result<AbstractSyntaxTree> parseResult(String input, String filename, String startSymbol) {
        try {
            return parseUnsafeResult(input, filename, startSymbol);
        } catch(ParseException e) {
            return new JSGLR2Result<>();
        }
    }

    default AbstractSyntaxTree parseUnsafe(String input, String filename, String startSymbol) throws ParseException {
        return parseUnsafeResult(input, filename, startSymbol).ast;
    }

    JSGLR2Result<AbstractSyntaxTree> parseUnsafeResult(String input, String filename, String startSymbol)
        throws ParseException;
}
