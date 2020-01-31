package org.spoofax.jsglr2;

import java.util.Objects;
import java.util.stream.Collectors;

import org.metaborg.parsetable.IParseTable;
import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.jsglr2.imploder.*;
import org.spoofax.jsglr2.imploder.incremental.IncrementalStrategoTermImploder;
import org.spoofax.jsglr2.imploder.incremental.IncrementalTreeImploder;
import org.spoofax.jsglr2.parseforest.*;
import org.spoofax.jsglr2.parser.IParser;
import org.spoofax.jsglr2.parser.ParserVariant;
import org.spoofax.jsglr2.reducing.Reducing;
import org.spoofax.jsglr2.stack.StackRepresentation;
import org.spoofax.jsglr2.stack.collections.ActiveStacksRepresentation;
import org.spoofax.jsglr2.stack.collections.ForActorStacksRepresentation;
import org.spoofax.jsglr2.tokens.StubTokenizer;
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

    private <ImploderCache extends IncrementalTreeImploder.ResultCache<IParseForest, IParseNode<IParseForest, IDerivation<IParseForest>>, IDerivation<IParseForest>, IStrategoTerm>>
        IImploder<IParseForest, TreeImploder.SubTree<IStrategoTerm>, ImploderCache, IStrategoTerm, ImplodeResult<TreeImploder.SubTree<IStrategoTerm>, ImploderCache, IStrategoTerm>>
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

    private ITokenizer<TreeImploder.SubTree<IStrategoTerm>> getTokenizer() {
        switch(this.tokenizer) {
            case Recursive:
                return new StrategoTermTokenizer();
            case Iterative:
                return new IterativeStrategoTermTokenizer();
            default:
            case Null:
                throw new IllegalStateException();
        }
    }

    public JSGLR2<IStrategoTerm> getJSGLR2(IParseTable parseTable) {
        if(!this.isValid())
            if(!parser.isValid())
                throw new IllegalStateException(
                    "Invalid JSGLR2 parser variant: " + parser.validate().collect(Collectors.joining(", ")));
            else
                throw new IllegalStateException("Invalid JSGLR2 variant");

        @SuppressWarnings("unchecked") final IParser<IParseForest> parser =
            (IParser<IParseForest>) this.parser.getParser(parseTable);

        if(this.parser.parseForestRepresentation == ParseForestRepresentation.Null)
            return new JSGLR2Implementation<>(parser, new NullStrategoImploder<>(), (input, fileName, tree) -> null);
        else if(this.imploder == ImploderVariant.TokenizedRecursive)
            return new JSGLR2Implementation<>(parser, new TokenizedStrategoTermImploder<>(), new StubTokenizer());
        else
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
                    ParseForestConstruction.Full,
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
                    ParseForestConstruction.Full,
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
                    ParseForestConstruction.standard(),
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
                    ParseForestConstruction.standard(),
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
