package org.spoofax.jsglr2;

import java.util.Objects;

import org.metaborg.parsetable.IParseTable;
import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.jsglr2.imploder.*;
import org.spoofax.jsglr2.imploder.incremental.IncrementalStrategoTermImploder;
import org.spoofax.jsglr2.parseforest.IParseForest;
import org.spoofax.jsglr2.parseforest.ParseForestConstruction;
import org.spoofax.jsglr2.parseforest.ParseForestRepresentation;
import org.spoofax.jsglr2.parser.IParser;
import org.spoofax.jsglr2.parser.ParserVariant;
import org.spoofax.jsglr2.reducing.Reducing;
import org.spoofax.jsglr2.stack.StackRepresentation;
import org.spoofax.jsglr2.stack.collections.ActiveStacksRepresentation;
import org.spoofax.jsglr2.stack.collections.ForActorStacksRepresentation;
import org.spoofax.jsglr2.tokens.NullTokenizer;
import org.spoofax.jsglr2.tokens.TokenizerVariant;

public class JSGLR2Variant {
    public final ParserVariant parser;
    public final ImploderVariant imploder;
    public final TokenizerVariant tokenizer;

    public JSGLR2Variant(ParserVariant parserVariant, ImploderVariant imploderVariant,
        TokenizerVariant tokenizerVariant) {
        this.parser = parserVariant;
        this.imploder = imploderVariant;
        this.tokenizer = tokenizerVariant;
    }

    private <ParseForest extends IParseForest> IImploder<ParseForest, TreeImploder.SubTree<IStrategoTerm>>
        getImploder() {
        switch(this.imploder) {
            default:
            case Recursive:
                return new StrategoTermImploder<>();
            case RecursiveIncremental:
                return new IncrementalStrategoTermImploder<>();
            case Iterative:
                return new IterativeStrategoTermImploder<>();
        }
    }

    private ITokenizer<TreeImploder.SubTree<IStrategoTerm>, IStrategoTerm> getTokenizer() {
        switch(this.tokenizer) {
            default:
            case Null:
                return (input, resource, implodeResult) -> new TokenizeResult<>(resource, null, implodeResult.tree);
            case Recursive:
                return new StrategoTermTokenizer();
            case Iterative:
                return new IterativeStrategoTermTokenizer();
        }
    }

    public JSGLR2<IStrategoTerm> getJSGLR2(IParseTable parseTable) {
        if(!this.isValid())
            throw new IllegalStateException("Invalid JSGLR2 variant");

        @SuppressWarnings("unchecked") final IParser<IParseForest> parser =
            (IParser<IParseForest>) this.parser.getParser(parseTable);

        if(this.parser.parseForestRepresentation == ParseForestRepresentation.Null)
            return new JSGLR2Implementation<>(parser, new NullStrategoImploder<>(), new NullTokenizer<>());

        if(this.imploder == ImploderVariant.TokenizedRecursive)
            return new JSGLR2Implementation<>(parser, new TokenizedStrategoTermImploder<>(), new NullTokenizer<>());

        return new JSGLR2Implementation<>(parser, getImploder(), getTokenizer());
    }

    public String name() {
        return parser.name() + "//Imploder:" + imploder.name() + "//Tokenizer:" + tokenizer.name();
    }

    @Override public boolean equals(Object o) {
        if(this == o)
            return true;
        if(o == null || getClass() != o.getClass())
            return false;

        JSGLR2Variant variant = (JSGLR2Variant) o;

        return Objects.equals(parser, variant.parser) && imploder == variant.imploder && tokenizer == variant.tokenizer;
    }

    public boolean isValid() {
        return parser.isValid()
            && (imploder == ImploderVariant.TokenizedRecursive) == (tokenizer == TokenizerVariant.Null);
    }

    public enum Preset {
        // @formatter:off
        standard(
            new JSGLR2Variant(
                new ParserVariant(
                    ActiveStacksRepresentation.standard(),
                    ForActorStacksRepresentation.standard(),
                    ParseForestRepresentation.standard(),
                    ParseForestConstruction.standard(),
                    StackRepresentation.Hybrid,
                    Reducing.Basic,
                    false),
                ImploderVariant.standard(),
                TokenizerVariant.standard())),
    
        elkhound(
            new JSGLR2Variant(
                new ParserVariant(
                    ActiveStacksRepresentation.standard(),
                    ForActorStacksRepresentation.standard(),
                    ParseForestRepresentation.standard(),
                    ParseForestConstruction.standard(),
                    StackRepresentation.standard(),
                    Reducing.standard(),
                    false),
                ImploderVariant.standard(),
                TokenizerVariant.standard())),
    
        recovery(
            new JSGLR2Variant(
                new ParserVariant(
                    ActiveStacksRepresentation.standard(),
                    ForActorStacksRepresentation.standard(),
                    ParseForestRepresentation.standard(),
                    ParseForestConstruction.standard(),
                    StackRepresentation.Hybrid,
                    Reducing.Basic,
                    true),
                ImploderVariant.standard(),
                TokenizerVariant.standard())),

        recoveryElkhound(
            new JSGLR2Variant(
                new ParserVariant(
                    ActiveStacksRepresentation.standard(),
                    ForActorStacksRepresentation.standard(),
                    ParseForestRepresentation.standard(),
                    ParseForestConstruction.standard(),
                    StackRepresentation.HybridElkhound,
                    Reducing.Elkhound,
                    true),
                ImploderVariant.standard(),
                TokenizerVariant.standard())),

        dataDependent(
            new JSGLR2Variant(
                new ParserVariant(
                    ActiveStacksRepresentation.standard(),
                    ForActorStacksRepresentation.standard(),
                    ParseForestRepresentation.DataDependent,
                    ParseForestConstruction.standard(),
                    StackRepresentation.Hybrid,
                    Reducing.DataDependent,
                    false),
                ImploderVariant.standard(),
                TokenizerVariant.standard())),
    
        layoutSensitive(
            new JSGLR2Variant(
                new ParserVariant(
                    ActiveStacksRepresentation.standard(),
                    ForActorStacksRepresentation.standard(),
                    ParseForestRepresentation.LayoutSensitive,
                    ParseForestConstruction.standard(),
                    StackRepresentation.Hybrid,
                    Reducing.LayoutSensitive,
                    false),
                ImploderVariant.standard(),
                TokenizerVariant.standard())),

        composite(
            new JSGLR2Variant(
                new ParserVariant(
                    ActiveStacksRepresentation.standard(),
                    ForActorStacksRepresentation.standard(),
                    ParseForestRepresentation.Composite,
                    ParseForestConstruction.standard(),
                    StackRepresentation.Hybrid,
                    Reducing.Composite,
                    false),
                ImploderVariant.standard(),
                TokenizerVariant.standard())),
    
        incremental(
            new JSGLR2Variant(
                new ParserVariant(
                    ActiveStacksRepresentation.standard(),
                    ForActorStacksRepresentation.standard(),
                    ParseForestRepresentation.Incremental,
                    ParseForestConstruction.Full,
                    StackRepresentation.Hybrid,
                    Reducing.Incremental,
                    false),
                ImploderVariant.RecursiveIncremental,
                TokenizerVariant.Recursive)),

        recoveryIncremental(
            new JSGLR2Variant(
                new ParserVariant(
                    ActiveStacksRepresentation.standard(),
                    ForActorStacksRepresentation.standard(),
                    ParseForestRepresentation.Incremental,
                    ParseForestConstruction.Full,
                    StackRepresentation.Hybrid,
                    Reducing.Incremental,
                    true),
                ImploderVariant.RecursiveIncremental,
                TokenizerVariant.Recursive));
        // @formatter:on

        public final JSGLR2Variant variant;

        Preset(JSGLR2Variant variant) {
            this.variant = variant;
        }

        public JSGLR2<IStrategoTerm> getJSGLR2(IParseTable parseTable) {
            return variant.getJSGLR2(parseTable);
        }
    }

}
