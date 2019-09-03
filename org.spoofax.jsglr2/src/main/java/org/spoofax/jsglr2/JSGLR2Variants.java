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

public enum JSGLR2Variants {
    // @formatter:off
    standard(
        new Variant(
            new ParserVariant(
                ActiveStacksRepresentation.standard(),
                ForActorStacksRepresentation.standard(),
                ParseForestRepresentation.standard(),
                ParseForestConstruction.standard(),
                StackRepresentation.standard(),
                Reducing.standard()),
            ImploderVariant.standard(),
            TokenizerVariant.standard())),

    dataDependent(
        new Variant(
            new ParserVariant(
                ActiveStacksRepresentation.standard(),
                ForActorStacksRepresentation.standard(),
                ParseForestRepresentation.DataDependent,
                ParseForestConstruction.standard(),
                StackRepresentation.Basic,
                Reducing.DataDependent),
            ImploderVariant.standard(),
            TokenizerVariant.standard())),

    layoutSensitive(
        new Variant(
            new ParserVariant(
                ActiveStacksRepresentation.standard(),
                ForActorStacksRepresentation.standard(),
                ParseForestRepresentation.LayoutSensitive,
                ParseForestConstruction.standard(),
                StackRepresentation.Basic,
                Reducing.LayoutSensitive),
            ImploderVariant.standard(),
            TokenizerVariant.standard())),

    incremental(
        new Variant(
            new ParserVariant(
                ActiveStacksRepresentation.standard(),
                ForActorStacksRepresentation.standard(),
                ParseForestRepresentation.Incremental,
                ParseForestConstruction.Full,
                StackRepresentation.Basic,
                Reducing.Incremental),
            ImploderVariant.RecursiveIncremental,
            TokenizerVariant.Recursive));
    // @formatter:on

    public Variant variant;

    JSGLR2Variants(Variant variant) {
        this.variant = variant;
    }

    public static class Variant {
        public final ParserVariant parser;
        public final ImploderVariant imploder;
        public final TokenizerVariant tokenizer;

        public Variant(ParserVariant parserVariant, ImploderVariant imploderVariant,
            TokenizerVariant tokenizerVariant) {
            this.parser = parserVariant;
            this.imploder = imploderVariant;
            this.tokenizer = tokenizerVariant;
        }

        public String name() {
            return parser.name() + "//Imploder:" + imploder.name() + "//Tokenizer:" + tokenizer.name();
        }

        @Override public boolean equals(Object o) {
            if(this == o)
                return true;
            if(o == null || getClass() != o.getClass())
                return false;

            Variant variant = (Variant) o;

            return Objects.equals(parser, variant.parser) && imploder == variant.imploder
                && tokenizer == variant.tokenizer;
        }

        public boolean isValid() {
            return parser.isValid()
                && (imploder == ImploderVariant.TokenizedRecursive) == (tokenizer == TokenizerVariant.Null);
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
                    return (input, filename, implodeResult) -> new TokenizeResult<>(null, implodeResult.tree);
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
    }

}
