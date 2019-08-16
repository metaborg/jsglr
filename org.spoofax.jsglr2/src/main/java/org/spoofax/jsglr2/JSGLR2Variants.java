package org.spoofax.jsglr2;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.metaborg.parsetable.IParseTable;
import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.jsglr2.datadependent.DataDependentParseForestManager;
import org.spoofax.jsglr2.elkhound.BasicElkhoundStackManager;
import org.spoofax.jsglr2.elkhound.ElkhoundParser;
import org.spoofax.jsglr2.elkhound.HybridElkhoundStackManager;
import org.spoofax.jsglr2.imploder.*;
import org.spoofax.jsglr2.imploder.incremental.IncrementalStrategoTermImploder;
import org.spoofax.jsglr2.incremental.IncrementalParse;
import org.spoofax.jsglr2.incremental.IncrementalParser;
import org.spoofax.jsglr2.incremental.parseforest.IncrementalParseForestManager;
import org.spoofax.jsglr2.layoutsensitive.LayoutSensitiveParseForestManager;
import org.spoofax.jsglr2.parseforest.IParseForest;
import org.spoofax.jsglr2.parseforest.ParseForestConstruction;
import org.spoofax.jsglr2.parseforest.ParseForestRepresentation;
import org.spoofax.jsglr2.parseforest.basic.BasicParseForestManager;
import org.spoofax.jsglr2.parseforest.empty.NullParseForestManager;
import org.spoofax.jsglr2.parseforest.hybrid.HybridParseForestManager;
import org.spoofax.jsglr2.parser.IParser;
import org.spoofax.jsglr2.parser.Parse;
import org.spoofax.jsglr2.parser.Parser;
import org.spoofax.jsglr2.parser.failure.DefaultParseFailureHandler;
import org.spoofax.jsglr2.parser.observing.ParserObserving;
import org.spoofax.jsglr2.reducing.ReduceManagerFactory;
import org.spoofax.jsglr2.reducing.Reducing;
import org.spoofax.jsglr2.stack.StackRepresentation;
import org.spoofax.jsglr2.stack.basic.BasicStackManager;
import org.spoofax.jsglr2.stack.collections.ActiveStacksRepresentation;
import org.spoofax.jsglr2.stack.collections.ForActorStacksRepresentation;
import org.spoofax.jsglr2.stack.hybrid.HybridStackManager;
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
    }

    public static class ParserVariant {
        public final ActiveStacksRepresentation activeStacksRepresentation;
        public final ForActorStacksRepresentation forActorStacksRepresentation;
        public final ParseForestRepresentation parseForestRepresentation;
        public final ParseForestConstruction parseForestConstruction;
        public final StackRepresentation stackRepresentation;
        public final Reducing reducing;

        public ParserVariant(ActiveStacksRepresentation activeStacksRepresentation,
            ForActorStacksRepresentation forActorStacksRepresentation,
            ParseForestRepresentation parseForestRepresentation, ParseForestConstruction parseForestConstruction,
            StackRepresentation stackRepresentation, Reducing reducing) {
            this.activeStacksRepresentation = activeStacksRepresentation;
            this.forActorStacksRepresentation = forActorStacksRepresentation;
            this.parseForestRepresentation = parseForestRepresentation;
            this.parseForestConstruction = parseForestConstruction;
            this.stackRepresentation = stackRepresentation;
            this.reducing = reducing;
        }

        public boolean isValid() {
            // Elkhound reducing requires Elkhound stack, and the other way around (bi-implication)
            boolean validElkhound =
                (reducing == Reducing.Elkhound) == (stackRepresentation == StackRepresentation.BasicElkhound
                    || stackRepresentation == StackRepresentation.HybridElkhound);
            // PFR Null requires PFC Full (the implication N -> F is written as !N || F)
            boolean validParseForest = parseForestRepresentation != ParseForestRepresentation.Null
                || parseForestConstruction == ParseForestConstruction.Full;
            boolean validIncremental =
                (parseForestRepresentation == ParseForestRepresentation.Incremental) == (reducing == Reducing.Incremental)
                    // Incremental parsing requires a full parse forest
                    && (reducing != Reducing.Incremental || parseForestConstruction == ParseForestConstruction.Full);
            boolean validLayoutSensitive =
                (parseForestRepresentation == ParseForestRepresentation.LayoutSensitive) == (reducing == Reducing.LayoutSensitive);
            boolean validDataDependent =
                (parseForestRepresentation == ParseForestRepresentation.DataDependent) == (reducing == Reducing.DataDependent);

            return validElkhound && validParseForest && validIncremental && validLayoutSensitive && validDataDependent;
        }

        public String name() {
            return "ActiveStacksRepresentation:" + activeStacksRepresentation + "/ForActorStacksRepresentation:"
                + forActorStacksRepresentation + "/ParseForestRepresentation:" + parseForestRepresentation
                + "/ParseForestConstruction:" + parseForestConstruction + "/StackRepresentation:" + stackRepresentation
                + "/Reducing:" + reducing;
        }

        @Override public boolean equals(Object o) {
            if(this == o)
                return true;
            if(o == null || getClass() != o.getClass())
                return false;

            ParserVariant that = (ParserVariant) o;

            return activeStacksRepresentation == that.activeStacksRepresentation
                && forActorStacksRepresentation == that.forActorStacksRepresentation
                && parseForestRepresentation == that.parseForestRepresentation
                && parseForestConstruction == that.parseForestConstruction
                && stackRepresentation == that.stackRepresentation && reducing == that.reducing;
        }
    }

    public static List<Variant> allVariants() {
        List<Variant> variants = new ArrayList<>();

        for(ActiveStacksRepresentation activeStacksRepresentation : ActiveStacksRepresentation.values()) {
            for(ForActorStacksRepresentation forActorStacksRepresentation : ForActorStacksRepresentation.values()) {
                for(ParseForestRepresentation parseForestRepresentation : ParseForestRepresentation.values()) {
                    if(parseForestRepresentation != ParseForestRepresentation.Null)
                        for(ParseForestConstruction parseForestConstruction : ParseForestConstruction.values()) {
                            for(StackRepresentation stackRepresentation : StackRepresentation.values()) {
                                for(Reducing reducing : Reducing.values()) {
                                    ParserVariant parserVariant = new ParserVariant(activeStacksRepresentation,
                                        forActorStacksRepresentation, parseForestRepresentation,
                                        parseForestConstruction, stackRepresentation, reducing);

                                    if(parserVariant.isValid())
                                        for(ImploderVariant imploderVariant : ImploderVariant.values()) {
                                            for(TokenizerVariant tokenizerVariant : TokenizerVariant.values()) {
                                                Variant variant =
                                                    new Variant(parserVariant, imploderVariant, tokenizerVariant);
                                                if(variant.isValid())
                                                    variants.add(variant);
                                            }
                                        }
                                }
                            }
                        }
                }
            }
        }

        return variants;
    }

    public static IParser<? extends IParseForest> getParser(IParseTable parseTable, ParserVariant variant) {
        if(!variant.isValid())
            throw new IllegalStateException("Invalid parser variant");

        // @formatter:off
        switch(variant.parseForestRepresentation) {
            default:
            case Basic: switch(variant.reducing) {
                case Elkhound: switch(variant.stackRepresentation) {
                    case BasicElkhound:  return new ElkhoundParser<>(Parse.factory(variant), parseTable, new BasicElkhoundStackManager<>(),  new BasicParseForestManager<>(), ReduceManagerFactory.elkhoundReduceManagerFactory(variant), new DefaultParseFailureHandler<>(), new ParserObserving<>());
                    case HybridElkhound: return new ElkhoundParser<>(Parse.factory(variant), parseTable, new HybridElkhoundStackManager<>(), new BasicParseForestManager<>(), ReduceManagerFactory.elkhoundReduceManagerFactory(variant), new DefaultParseFailureHandler<>(), new ParserObserving<>());
                    default: throw new IllegalStateException("Elkhound reducing requires Elkhound stack");
                }
                case Basic: switch(variant.stackRepresentation) {
                    case Basic:  return new Parser<>(Parse.factory(variant), parseTable, new BasicStackManager<>(),  new BasicParseForestManager<>(), ReduceManagerFactory.reduceManagerFactory(variant), new DefaultParseFailureHandler<>(), new ParserObserving<>());
                    case Hybrid: return new Parser<>(Parse.factory(variant), parseTable, new HybridStackManager<>(), new BasicParseForestManager<>(), ReduceManagerFactory.reduceManagerFactory(variant), new DefaultParseFailureHandler<>(), new ParserObserving<>());
                    default: throw new IllegalStateException("Basic reducing requires Basic or Hybrid stack");
                }
                default: throw new IllegalStateException("Only Elkhound or basic reducing possible with basic parse forest representation");
            }

            case Null: switch(variant.reducing) {
                case Elkhound: switch(variant.stackRepresentation) {
                    case BasicElkhound:  return new ElkhoundParser<>(Parse.factory(variant), parseTable, new BasicElkhoundStackManager<>(),  new NullParseForestManager<>(), ReduceManagerFactory.elkhoundReduceManagerFactory(variant), new DefaultParseFailureHandler<>(), new ParserObserving<>());
                    case HybridElkhound: return new ElkhoundParser<>(Parse.factory(variant), parseTable, new HybridElkhoundStackManager<>(), new NullParseForestManager<>(), ReduceManagerFactory.elkhoundReduceManagerFactory(variant), new DefaultParseFailureHandler<>(), new ParserObserving<>());
                    default: throw new IllegalStateException("Elkhound reducing requires Elkhound stack");
                }
                case Basic: switch(variant.stackRepresentation) {
                    case Basic:  return new Parser<>(Parse.factory(variant), parseTable, new BasicStackManager<>(),  new NullParseForestManager<>(), ReduceManagerFactory.reduceManagerFactory(variant), new DefaultParseFailureHandler<>(), new ParserObserving<>());
                    case Hybrid: return new Parser<>(Parse.factory(variant), parseTable, new HybridStackManager<>(), new NullParseForestManager<>(), ReduceManagerFactory.reduceManagerFactory(variant), new DefaultParseFailureHandler<>(), new ParserObserving<>());
                    default: throw new IllegalStateException("Basic reducing requires Basic or Hybrid stack");
                }
                default: throw new IllegalStateException("Only Elkhound or basic reducing possible with empty parse forest representation");
            }

            case Hybrid: switch(variant.reducing) {
                case Elkhound: switch(variant.stackRepresentation) {
                    case BasicElkhound:  return new ElkhoundParser<>(Parse.factory(variant), parseTable, new BasicElkhoundStackManager<>(),  new HybridParseForestManager<>(), ReduceManagerFactory.elkhoundReduceManagerFactory(variant), new DefaultParseFailureHandler<>(), new ParserObserving<>());
                    case HybridElkhound: return new ElkhoundParser<>(Parse.factory(variant), parseTable, new HybridElkhoundStackManager<>(), new HybridParseForestManager<>(), ReduceManagerFactory.elkhoundReduceManagerFactory(variant), new DefaultParseFailureHandler<>(), new ParserObserving<>());
                    default: throw new IllegalStateException("Elkhound reducing requires Elkhound stack");
                }
                case Basic: switch(variant.stackRepresentation) {
                    case Basic:  return new Parser<>(Parse.factory(variant), parseTable, new BasicStackManager<>(),  new HybridParseForestManager<>(), ReduceManagerFactory.reduceManagerFactory(variant), new DefaultParseFailureHandler<>(), new ParserObserving<>());
                    case Hybrid: return new Parser<>(Parse.factory(variant), parseTable, new HybridStackManager<>(), new HybridParseForestManager<>(), ReduceManagerFactory.reduceManagerFactory(variant), new DefaultParseFailureHandler<>(), new ParserObserving<>());
                    default: throw new IllegalStateException("Basic reducing requires Basic or Hybrid stack");
                }
                default: throw new IllegalStateException("Only Elkhound or basic reducing possible with hybrid parse forest representation");
            }

            case DataDependent:
                if(variant.reducing != Reducing.DataDependent)
                    throw new IllegalStateException();

                switch(variant.stackRepresentation) {
                    case Basic:  return new Parser<>(Parse.factory(variant), parseTable, new BasicStackManager<>(),  new DataDependentParseForestManager<>(), ReduceManagerFactory.dataDependentReduceManagerFactory(variant), new DefaultParseFailureHandler<>(), new ParserObserving<>());
                    case Hybrid: return new Parser<>(Parse.factory(variant), parseTable, new HybridStackManager<>(), new DataDependentParseForestManager<>(), ReduceManagerFactory.dataDependentReduceManagerFactory(variant), new DefaultParseFailureHandler<>(), new ParserObserving<>());
                    default: throw new IllegalStateException();
                }

            case LayoutSensitive:
                if(variant.reducing != Reducing.LayoutSensitive)
                    throw new IllegalStateException();

                switch(variant.stackRepresentation) {
                    case Basic:  return new Parser<>(Parse.factory(variant), parseTable, new BasicStackManager<>(),  new LayoutSensitiveParseForestManager<>(), ReduceManagerFactory.layoutSensitiveReduceManagerFactory(variant), new DefaultParseFailureHandler<>(), new ParserObserving<>());
                    case Hybrid: return new Parser<>(Parse.factory(variant), parseTable, new HybridStackManager<>(), new LayoutSensitiveParseForestManager<>(), ReduceManagerFactory.layoutSensitiveReduceManagerFactory(variant), new DefaultParseFailureHandler<>(), new ParserObserving<>());
                    default: throw new IllegalStateException();
                }

            case Incremental:
                if(variant.reducing != Reducing.Incremental)
                    throw new IllegalStateException();

                switch(variant.stackRepresentation) {
                    case Basic:  return new IncrementalParser<>(IncrementalParse.factory(variant), IncrementalParse.incrementalFactory(variant), parseTable, new BasicStackManager<>(),  new IncrementalParseForestManager<>(), ReduceManagerFactory.incrementalReduceManagerFactory(variant), new DefaultParseFailureHandler<>(), new ParserObserving<>());
                    case Hybrid: return new IncrementalParser<>(IncrementalParse.factory(variant), IncrementalParse.incrementalFactory(variant), parseTable, new HybridStackManager<>(), new IncrementalParseForestManager<>(), ReduceManagerFactory.incrementalReduceManagerFactory(variant), new DefaultParseFailureHandler<>(), new ParserObserving<>());
                    default: throw new IllegalStateException();
                }
        }
        // @formatter:on
    }

    public static List<IParser<?>> allParsers(IParseTable parseTable) {
        List<IParser<?>> parsers = new ArrayList<>();

        for(Variant variant : allVariants()) {
            parsers.add(getParser(parseTable, variant.parser));
        }

        return parsers;
    }

    private static <ParseForest extends IParseForest> IImploder<ParseForest, TreeImploder.SubTree<IStrategoTerm>>
        getImploder(Variant variant) {
        switch(variant.imploder) {
            default:
            case Recursive:
                return new StrategoTermImploder<>();
            case RecursiveIncremental:
                return new IncrementalStrategoTermImploder<>();
            case Iterative:
                return new IterativeStrategoTermImploder<>();
        }
    }

    private static ITokenizer<TreeImploder.SubTree<IStrategoTerm>, IStrategoTerm> getTokenizer(Variant variant) {
        switch(variant.tokenizer) {
            default:
            case Null:
                return (input, filename, implodeResult) -> new TokenizeResult<>(null, implodeResult.tree);
            case Recursive:
                return new StrategoTermTokenizer();
            case Iterative:
                return new IterativeStrategoTermTokenizer();
        }
    }

    public static JSGLR2<IStrategoTerm> getJSGLR2(IParseTable parseTable, Variant variant) {
        @SuppressWarnings("unchecked") final IParser<IParseForest> parser =
            (IParser<IParseForest>) getParser(parseTable, variant.parser);

        if(variant.parser.parseForestRepresentation == ParseForestRepresentation.Null)
            return new JSGLR2Implementation<>(parser, new NullStrategoImploder<>(), new NullTokenizer<>());

        if(variant.imploder == ImploderVariant.TokenizedRecursive)
            return new JSGLR2Implementation<>(parser, new TokenizedStrategoTermImploder<>(), new NullTokenizer<>());

        return new JSGLR2Implementation<>(parser, getImploder(variant), getTokenizer(variant));
    }

    public static List<JSGLR2<IStrategoTerm>> allJSGLR2(IParseTable parseTable) {
        List<JSGLR2<IStrategoTerm>> jsglr2s = new ArrayList<>();

        for(Variant variant : allVariants()) {
            JSGLR2<IStrategoTerm> jsglr2 = getJSGLR2(parseTable, variant);

            jsglr2s.add(jsglr2);
        }

        return jsglr2s;
    }

}
