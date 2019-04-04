package org.spoofax.jsglr2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.metaborg.parsetable.IParseTable;
import org.metaborg.sdf2table.parsetable.query.ActionsForCharacterRepresentation;
import org.metaborg.sdf2table.parsetable.query.ProductionToGotoRepresentation;
import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.jsglr2.datadependent.DataDependentParseForestManager;
import org.spoofax.jsglr2.elkhound.BasicElkhoundStackManager;
import org.spoofax.jsglr2.elkhound.ElkhoundParser;
import org.spoofax.jsglr2.elkhound.HybridElkhoundStackManager;
import org.spoofax.jsglr2.imploder.IImploder;
import org.spoofax.jsglr2.imploder.NullStrategoImploder;
import org.spoofax.jsglr2.imploder.StrategoTermImploder;
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
import org.spoofax.jsglr2.stack.IStackNode;
import org.spoofax.jsglr2.stack.StackRepresentation;
import org.spoofax.jsglr2.stack.basic.BasicStackManager;
import org.spoofax.jsglr2.stack.collections.ActiveStacksRepresentation;
import org.spoofax.jsglr2.stack.collections.ForActorStacksRepresentation;
import org.spoofax.jsglr2.stack.hybrid.HybridStackManager;

public class JSGLR2Variants {

    public static class Variant {
        public ParseTableVariant parseTable;
        public ParserVariant parser;

        public Variant(ParseTableVariant parseTableVariant, ParserVariant parserVariant) {
            this.parseTable = parseTableVariant;
            this.parser = parserVariant;
        }

        public String name() {
            return parseTable.name() + "/" + parser.name();
        }

        @Override public boolean equals(Object o) {
            if(this == o) {
                return true;
            }
            if(o == null || getClass() != o.getClass()) {
                return false;
            }

            Variant that = (Variant) o;

            return parseTable.equals(that.parseTable) && parser.equals(that.parser);
        }
    }

    public static class ParseTableVariant {
        public ActionsForCharacterRepresentation actionsForCharacterRepresentation;
        public ProductionToGotoRepresentation productionToGotoRepresentation;

        public ParseTableVariant(ActionsForCharacterRepresentation actionsForCharacterRepresentation,
            ProductionToGotoRepresentation productionToGotoRepresentation) {
            this.actionsForCharacterRepresentation = actionsForCharacterRepresentation;
            this.productionToGotoRepresentation = productionToGotoRepresentation;
        }

        public String name() {
            return "ActionsForCharacterRepresentation:" + actionsForCharacterRepresentation
                + "/ProductionToGotoRepresentation:" + productionToGotoRepresentation;
        }

        @Override public boolean equals(Object o) {
            if(this == o) {
                return true;
            }
            if(o == null || getClass() != o.getClass()) {
                return false;
            }

            ParseTableVariant that = (ParseTableVariant) o;

            return actionsForCharacterRepresentation == that.actionsForCharacterRepresentation
                && productionToGotoRepresentation == that.productionToGotoRepresentation;
        }
    }

    public static class ParserVariant {
        public ActiveStacksRepresentation activeStacksRepresentation;
        public ForActorStacksRepresentation forActorStacksRepresentation;
        public ParseForestRepresentation parseForestRepresentation;
        public ParseForestConstruction parseForestConstruction;
        public StackRepresentation stackRepresentation;
        public Reducing reducing;

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

            return validElkhound && validParseForest;
        }

        public String name() {
            return "ActiveStacksRepresentation:" + activeStacksRepresentation + "/ForActorStacksRepresentation:"
                + forActorStacksRepresentation + "/ParseForestRepresentation:" + parseForestRepresentation
                + "/ParseForestConstruction:" + parseForestConstruction + "/StackRepresentation:" + stackRepresentation
                + "/Reducing:" + reducing;
        }

        @Override public boolean equals(Object o) {
            if(this == o) {
                return true;
            }
            if(o == null || getClass() != o.getClass()) {
                return false;
            }

            ParserVariant that = (ParserVariant) o;

            return activeStacksRepresentation == that.activeStacksRepresentation
                && forActorStacksRepresentation == that.forActorStacksRepresentation
                && parseForestRepresentation == that.parseForestRepresentation
                && parseForestConstruction == that.parseForestConstruction
                && stackRepresentation == that.stackRepresentation && reducing == that.reducing;
        }
    }

    public static List<ParserVariant> allVariants() {
        List<ParserVariant> variants = new ArrayList<>();

        for(ActiveStacksRepresentation activeStacksRepresentation : ActiveStacksRepresentation.values()) {
            for(ForActorStacksRepresentation forActorStacksRepresentation : ForActorStacksRepresentation.values()) {
                for(ParseForestRepresentation parseForestRepresentation : ParseForestRepresentation.values()) {
                    if(parseForestRepresentation != ParseForestRepresentation.Null)
                        for(ParseForestConstruction parseForestConstruction : ParseForestConstruction.values()) {
                            for(StackRepresentation stackRepresentation : StackRepresentation.values()) {
                                for(Reducing reducing : Reducing.values()) {
                                    ParserVariant variant = new ParserVariant(activeStacksRepresentation,
                                        forActorStacksRepresentation, parseForestRepresentation,
                                        parseForestConstruction, stackRepresentation, reducing);

                                    if(variant.isValid())
                                        variants.add(variant);
                                }
                            }
                        }
                }
            }
        }

        return variants;
    }

    public static List<Variant> testVariants() {
        //@formatter:off
        return Arrays.asList(
            new Variant(new ParseTableVariant(ActionsForCharacterRepresentation.Separated,      ProductionToGotoRepresentation.ForLoop),     new ParserVariant(ActiveStacksRepresentation.ArrayList,     ForActorStacksRepresentation.ArrayDeque,    ParseForestRepresentation.Basic,  ParseForestConstruction.Full, StackRepresentation.Basic,          Reducing.Basic)),
            new Variant(new ParseTableVariant(ActionsForCharacterRepresentation.DisjointSorted, ProductionToGotoRepresentation.JavaHashMap), new ParserVariant(ActiveStacksRepresentation.ArrayList,     ForActorStacksRepresentation.ArrayDeque,    ParseForestRepresentation.Basic,  ParseForestConstruction.Full, StackRepresentation.Basic,          Reducing.Basic)),
            new Variant(new ParseTableVariant(ActionsForCharacterRepresentation.Separated,      ProductionToGotoRepresentation.ForLoop),     new ParserVariant(ActiveStacksRepresentation.LinkedHashMap, ForActorStacksRepresentation.LinkedHashMap, ParseForestRepresentation.Hybrid, ParseForestConstruction.Full, StackRepresentation.Hybrid,         Reducing.Basic)),
            new Variant(new ParseTableVariant(ActionsForCharacterRepresentation.Separated,      ProductionToGotoRepresentation.ForLoop),     new ParserVariant(ActiveStacksRepresentation.LinkedHashMap, ForActorStacksRepresentation.LinkedHashMap, ParseForestRepresentation.Hybrid, ParseForestConstruction.Full, StackRepresentation.HybridElkhound, Reducing.Elkhound))/*,
            new Variant(new ParseTableVariant(ActionsForCharacterRepresentation.Separated,      ProductionToGotoRepresentation.ForLoop),     new ParserVariant(ActiveStacksRepresentation.LinkedHashMap, ForActorStacksRepresentation.LinkedHashMap, ParseForestRepresentation.Hybrid, ParseForestConstruction.Optimized, StackRepresentation.Hybrid,         Reducing.Basic)),
            new Variant(new ParseTableVariant(ActionsForCharacterRepresentation.Separated,      ProductionToGotoRepresentation.ForLoop),     new ParserVariant(ActiveStacksRepresentation.LinkedHashMap, ForActorStacksRepresentation.LinkedHashMap, ParseForestRepresentation.Hybrid, ParseForestConstruction.Optimized, StackRepresentation.HybridElkhound, Reducing.Elkhound))*/
        );
        //@formatter:on
    }


    public static IParser<?, ?> getParser(IParseTable parseTable, ParserVariant variant) {
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
        }
    }

    private static <ParseForest extends IParseForest, ParseNode extends ParseForest, Derivation extends IDerivation<ParseForest>, PFM extends ParseForestManager<ParseForest, ParseNode, Derivation>>
        IParser<?, ?> getParser(IParseTable parseTable, ParserVariant variant, PFM parseForestManager) {
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

    public static List<IParser<?, ?>> allParsers(IParseTable parseTable) {
        List<IParser<?, ?>> parsers = new ArrayList<>();

        for(ParserVariant variant : allVariants()) {
            parsers.add(getParser(parseTable,
                new ParserVariant(variant.activeStacksRepresentation, variant.forActorStacksRepresentation,
                    variant.parseForestRepresentation, variant.parseForestConstruction, variant.stackRepresentation,
                    variant.reducing)));
        }

        return parsers;
    }

    public static JSGLR2<?, IStrategoTerm> getJSGLR2(IParseTable parseTable, ParserVariant variant) {
        @SuppressWarnings("unchecked") IParser<IParseForest, IStackNode> parser =
            (IParser<IParseForest, IStackNode>) getParser(parseTable, variant);

        IImploder<IParseForest, IStrategoTerm> imploder;
        if(variant.parseForestRepresentation == ParseForestRepresentation.Null)
            imploder = new NullStrategoImploder<>();
        else
            imploder = new StrategoTermImploder<>();

        return new JSGLR2<>(parser, imploder);
    }

    public static List<JSGLR2<?, IStrategoTerm>> allJSGLR2(IParseTable parseTable) {
        List<JSGLR2<?, IStrategoTerm>> jsglr2s = new ArrayList<>();

        for(ParserVariant variant : allVariants()) {
            JSGLR2<?, IStrategoTerm> jsglr2 = getJSGLR2(parseTable, variant);

            jsglr2s.add(jsglr2);
        }

        return jsglr2s;
    }

}
