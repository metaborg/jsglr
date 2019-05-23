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
import org.spoofax.jsglr2.parseforest.*;
import org.spoofax.jsglr2.parseforest.basic.BasicParseForestManager;
import org.spoofax.jsglr2.parseforest.empty.NullParseForestManager;
import org.spoofax.jsglr2.parseforest.hybrid.HybridParseForestManager;
import org.spoofax.jsglr2.parser.IParser;
import org.spoofax.jsglr2.parser.Parse;
import org.spoofax.jsglr2.parser.Parser;
import org.spoofax.jsglr2.reducing.ReduceManagerFactory;
import org.spoofax.jsglr2.reducing.Reducing;
import org.spoofax.jsglr2.stack.StackRepresentation;
import org.spoofax.jsglr2.stack.basic.BasicStackManager;
import org.spoofax.jsglr2.stack.collections.ActiveStacksRepresentation;
import org.spoofax.jsglr2.stack.collections.ForActorStacksRepresentation;
import org.spoofax.jsglr2.stack.hybrid.HybridStackManager;
import org.spoofax.jsglr2.tokens.NullTokenizer;
import org.spoofax.jsglr2.tokens.TokenizerVariant;

public class JSGLR2Variants {

    public static class Variant {
        public final ParserVariant parser;
        public final ImploderVariant imploder;
        public final TokenizerVariant tokenizer;

        public Variant(ParserVariant parserVariant, ImploderVariant imploderVariant, TokenizerVariant tokenizer) {
            this.parser = parserVariant;
            this.imploder = imploderVariant;
            this.tokenizer = tokenizer;
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
                && (imploder == ImploderVariant.TokenizedRecursive && tokenizer == TokenizerVariant.Null
                    || imploder != ImploderVariant.TokenizedRecursive && tokenizer != TokenizerVariant.Null);
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
            boolean validElkhound =
                reducing != Reducing.Elkhound || (stackRepresentation == StackRepresentation.BasicElkhound
                    || stackRepresentation == StackRepresentation.HybridElkhound);
            boolean validParseForest = parseForestRepresentation != ParseForestRepresentation.Null
                || parseForestConstruction == ParseForestConstruction.Full;
            // Incremental parsing requires a full parse forest
            boolean validIncremental = parseForestRepresentation != ParseForestRepresentation.Incremental
                || parseForestConstruction == ParseForestConstruction.Full;

            return validElkhound && validParseForest && validIncremental;
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

        switch(variant.parseForestRepresentation) {
            default:
            case Basic:
                return getParser(parseTable, variant, new BasicParseForestManager());
            case Null:
                return getParser(parseTable, variant, new NullParseForestManager());
            case Hybrid:
                return getParser(parseTable, variant, new HybridParseForestManager());

            case DataDependent:
                DataDependentParseForestManager dataDependentParseForestManager = new DataDependentParseForestManager();

                switch(variant.stackRepresentation) {
                    case Basic:
                        return new Parser<>(Parse.factory(variant), parseTable, new BasicStackManager<>(),
                            dataDependentParseForestManager,
                            ReduceManagerFactory.dataDependentReduceManagerFactory(variant));
                    case Hybrid:
                        return new Parser<>(Parse.factory(variant), parseTable, new HybridStackManager<>(),
                            dataDependentParseForestManager,
                            ReduceManagerFactory.dataDependentReduceManagerFactory(variant));
                    default:
                        throw new IllegalStateException();
                }

            case LayoutSensitive:
                LayoutSensitiveParseForestManager layoutSensitiveParseForestManager =
                    new LayoutSensitiveParseForestManager();

                switch(variant.stackRepresentation) {
                    case Basic:
                        return new Parser<>(Parse.factory(variant), parseTable, new BasicStackManager<>(),
                            layoutSensitiveParseForestManager,
                            ReduceManagerFactory.layoutSensitiveReduceManagerFactory(variant));
                    case Hybrid:
                        return new Parser<>(Parse.factory(variant), parseTable, new HybridStackManager<>(),
                            layoutSensitiveParseForestManager,
                            ReduceManagerFactory.layoutSensitiveReduceManagerFactory(variant));
                    default:
                        throw new IllegalStateException();
                }

            case Incremental:
                IncrementalParseForestManager parseForestManager = new IncrementalParseForestManager();

                switch(variant.stackRepresentation) {
                    case Basic:
                        return new IncrementalParser<>(IncrementalParse.factory(variant),
                            IncrementalParse.incrementalFactory(variant), parseTable, new BasicStackManager<>(),
                            parseForestManager, ReduceManagerFactory.reduceManagerFactory(variant));
                    case Hybrid:
                        return new IncrementalParser<>(IncrementalParse.factory(variant),
                            IncrementalParse.incrementalFactory(variant), parseTable, new HybridStackManager<>(),
                            parseForestManager, ReduceManagerFactory.reduceManagerFactory(variant));
                    default:
                        // TODO add Elkhound
                        throw new IllegalStateException();
                }
        }
    }

    private static <ParseForest extends IParseForest, ParseNode extends ParseForest, Derivation extends IDerivation<ParseForest>, PFM extends ParseForestManager<ParseForest, ParseNode, Derivation>>
        IParser<ParseForest> getParser(IParseTable parseTable, ParserVariant variant, PFM parseForestManager) {
        switch(variant.reducing) {
            case Elkhound:
                switch(variant.stackRepresentation) {
                    case BasicElkhound:
                        return new ElkhoundParser<>(Parse.factory(variant), parseTable,
                            new BasicElkhoundStackManager<>(), parseForestManager,
                            ReduceManagerFactory.elkhoundReduceManagerFactory(variant));
                    case HybridElkhound:
                        return new ElkhoundParser<>(Parse.factory(variant), parseTable,
                            new HybridElkhoundStackManager<>(), parseForestManager,
                            ReduceManagerFactory.elkhoundReduceManagerFactory(variant));
                    default:
                        throw new IllegalStateException("Elkhound reducing requires Elkhound stack");
                }
            case Basic:
                switch(variant.stackRepresentation) {
                    case Basic:
                        return new Parser<>(Parse.factory(variant), parseTable, new BasicStackManager<>(),
                            parseForestManager, ReduceManagerFactory.reduceManagerFactory(variant));
                    case Hybrid:
                        return new Parser<>(Parse.factory(variant), parseTable, new HybridStackManager<>(),
                            parseForestManager, ReduceManagerFactory.reduceManagerFactory(variant));
                    case BasicElkhound:
                        return new Parser<>(Parse.factory(variant), parseTable, new BasicElkhoundStackManager<>(),
                            parseForestManager, ReduceManagerFactory.reduceManagerFactory(variant));
                    case HybridElkhound:
                        return new Parser<>(Parse.factory(variant), parseTable, new HybridElkhoundStackManager<>(),
                            parseForestManager, ReduceManagerFactory.reduceManagerFactory(variant));
                    default:
                        throw new IllegalStateException();
                }
            default:
                throw new IllegalStateException(
                    "Only Elkhound or basic reducing possible with basic parse forest representation");
        }
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
