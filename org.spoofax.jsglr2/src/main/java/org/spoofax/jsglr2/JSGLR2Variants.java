package org.spoofax.jsglr2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.metaborg.parsetable.IParseTable;
import org.metaborg.sdf2table.parsetable.query.ActionsForCharacterRepresentation;
import org.metaborg.sdf2table.parsetable.query.ProductionToGotoRepresentation;
import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.jsglr2.datadependent.*;
import org.spoofax.jsglr2.elkhound.*;
import org.spoofax.jsglr2.imploder.IImploder;
import org.spoofax.jsglr2.imploder.NullParseForestStrategoImploder;
import org.spoofax.jsglr2.imploder.StrategoTermImploder;
import org.spoofax.jsglr2.layoutsensitive.*;
import org.spoofax.jsglr2.parseforest.ParseForestConstruction;
import org.spoofax.jsglr2.parseforest.ParseForestManager;
import org.spoofax.jsglr2.parseforest.ParseForestRepresentation;
import org.spoofax.jsglr2.parseforest.basic.BasicDerivation;
import org.spoofax.jsglr2.parseforest.basic.BasicParseForest;
import org.spoofax.jsglr2.parseforest.basic.BasicParseForestManager;
import org.spoofax.jsglr2.parseforest.basic.BasicParseNode;
import org.spoofax.jsglr2.parseforest.empty.NullParseForestManager;
import org.spoofax.jsglr2.parseforest.hybrid.HybridDerivation;
import org.spoofax.jsglr2.parseforest.hybrid.HybridParseForest;
import org.spoofax.jsglr2.parseforest.hybrid.HybridParseForestManager;
import org.spoofax.jsglr2.parseforest.hybrid.HybridParseNode;
import org.spoofax.jsglr2.parser.AbstractParse;
import org.spoofax.jsglr2.parser.IParser;
import org.spoofax.jsglr2.parser.Parse;
import org.spoofax.jsglr2.parser.Parser;
import org.spoofax.jsglr2.reducing.ReduceManager;
import org.spoofax.jsglr2.reducing.Reducing;
import org.spoofax.jsglr2.stack.AbstractStackManager;
import org.spoofax.jsglr2.stack.StackRepresentation;
import org.spoofax.jsglr2.stack.basic.BasicStackManager;
import org.spoofax.jsglr2.stack.basic.BasicStackNode;
import org.spoofax.jsglr2.stack.collections.*;
import org.spoofax.jsglr2.stack.hybrid.HybridStackManager;
import org.spoofax.jsglr2.stack.hybrid.HybridStackNode;

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
            new Variant(new ParseTableVariant(ActionsForCharacterRepresentation.Separated,      ProductionToGotoRepresentation.ForLoop),     new ParserVariant(ActiveStacksRepresentation.ArrayList,     ForActorStacksRepresentation.ArrayDeque,    ParseForestRepresentation.Basic,  ParseForestConstruction.Full,      StackRepresentation.Basic,          Reducing.Basic)),
            new Variant(new ParseTableVariant(ActionsForCharacterRepresentation.DisjointSorted, ProductionToGotoRepresentation.JavaHashMap), new ParserVariant(ActiveStacksRepresentation.ArrayList,     ForActorStacksRepresentation.ArrayDeque,    ParseForestRepresentation.Basic,  ParseForestConstruction.Full,      StackRepresentation.Basic,          Reducing.Basic))/*,
            new Variant(new ParseTableVariant(ActionsForCharacterRepresentation.Separated,      ProductionToGotoRepresentation.ForLoop),     new ParserVariant(ActiveStacksRepresentation.LinkedHashMap, ForActorStacksRepresentation.LinkedHashMap, ParseForestRepresentation.Hybrid, ParseForestConstruction.Optimized, StackRepresentation.Hybrid,         Reducing.Basic)),
            new Variant(new ParseTableVariant(ActionsForCharacterRepresentation.Separated,      ProductionToGotoRepresentation.ForLoop),     new ParserVariant(ActiveStacksRepresentation.LinkedHashMap, ForActorStacksRepresentation.LinkedHashMap, ParseForestRepresentation.Hybrid, ParseForestConstruction.Optimized, StackRepresentation.HybridElkhound, Reducing.Elkhound))*/
        );
        //@formatter:on
    }

    public static Parser<?, ?, ?, ?, ?> getParser(IParseTable parseTable, ParserVariant variant) {
        IActiveStacksFactory activeStacksFactory = new ActiveStacksFactory(variant.activeStacksRepresentation);
        IForActorStacksFactory forActorStacksFactory = new ForActorStacksFactory(variant.forActorStacksRepresentation);

        return getParser(parseTable, activeStacksFactory, forActorStacksFactory, variant);
    }


    public static Parser<?, ?, ?, ?, ?> getParser(IParseTable parseTable, IActiveStacksFactory activeStacksFactory,
        IForActorStacksFactory forActorStacksFactory, ParserVariant variant) {
        if(!variant.isValid())
            throw new IllegalStateException("Invalid parser variant");

        switch(variant.parseForestRepresentation) {
            default:
            case Basic:
                ParseForestManager<BasicParseForest, BasicParseNode, BasicDerivation> basicParseForestManager =
                    new BasicParseForestManager();

                switch(variant.reducing) {
                    case Elkhound:
                        switch(variant.stackRepresentation) {
                            case BasicElkhound:
                                ElkhoundStackManager<BasicParseForest, BasicElkhoundStackNode<BasicParseForest>, AbstractParse<BasicParseForest, BasicElkhoundStackNode<BasicParseForest>>> elkhoundBasicStackManager =
                                    new BasicElkhoundStackManager<>();

                                ElkhoundReduceManager<BasicParseForest, BasicParseNode, BasicDerivation, BasicElkhoundStackNode<BasicParseForest>, AbstractParse<BasicParseForest, BasicElkhoundStackNode<BasicParseForest>>> basicElkhoundReducer =
                                    new ElkhoundReduceManager<>(parseTable, elkhoundBasicStackManager,
                                        basicParseForestManager, variant.parseForestConstruction);

                                return new ElkhoundParser<>(Parse.factory(), parseTable, activeStacksFactory,
                                    forActorStacksFactory, elkhoundBasicStackManager, basicParseForestManager,
                                    basicElkhoundReducer);
                            case HybridElkhound:
                                ElkhoundStackManager<BasicParseForest, HybridElkhoundStackNode<BasicParseForest>, AbstractParse<BasicParseForest, HybridElkhoundStackNode<BasicParseForest>>> elkhoundHybridStackManager =
                                    new HybridElkhoundStackManager<>();

                                ElkhoundReduceManager<BasicParseForest, BasicParseNode, BasicDerivation, HybridElkhoundStackNode<BasicParseForest>, AbstractParse<BasicParseForest, HybridElkhoundStackNode<BasicParseForest>>> hybridElkhoundReducer =
                                    new ElkhoundReduceManager<>(parseTable, elkhoundHybridStackManager,
                                        basicParseForestManager, variant.parseForestConstruction);

                                return new ElkhoundParser<>(Parse.factory(), parseTable, activeStacksFactory,
                                    forActorStacksFactory, elkhoundHybridStackManager, basicParseForestManager,
                                    hybridElkhoundReducer);
                            default:
                                throw new IllegalStateException("Elkhound reducing requires Elkhound stack");
                        }
                    case Basic:
                        switch(variant.stackRepresentation) {
                            case Basic:
                                AbstractStackManager<BasicParseForest, BasicStackNode<BasicParseForest>, AbstractParse<BasicParseForest, BasicStackNode<BasicParseForest>>> basicStackManager =
                                    new BasicStackManager<>();

                                ReduceManager<BasicParseForest, BasicParseNode, BasicDerivation, BasicStackNode<BasicParseForest>, AbstractParse<BasicParseForest, BasicStackNode<BasicParseForest>>> basicReduceManager =
                                    new ReduceManager<>(parseTable, basicStackManager, basicParseForestManager,
                                        variant.parseForestConstruction);

                                return new Parser<>(Parse.factory(), parseTable, activeStacksFactory,
                                    forActorStacksFactory, basicStackManager, basicParseForestManager,
                                    basicReduceManager);
                            case Hybrid:
                                AbstractStackManager<BasicParseForest, HybridStackNode<BasicParseForest>, AbstractParse<BasicParseForest, HybridStackNode<BasicParseForest>>> hybridStackManager =
                                    new HybridStackManager<>();

                                ReduceManager<BasicParseForest, BasicParseNode, BasicDerivation, HybridStackNode<BasicParseForest>, AbstractParse<BasicParseForest, HybridStackNode<BasicParseForest>>> hybridReduceManager =
                                    new ReduceManager<>(parseTable, hybridStackManager, basicParseForestManager,
                                        variant.parseForestConstruction);

                                return new Parser<>(Parse.factory(), parseTable, activeStacksFactory,
                                    forActorStacksFactory, hybridStackManager, basicParseForestManager,
                                    hybridReduceManager);
                            case BasicElkhound:
                                ElkhoundStackManager<BasicParseForest, BasicElkhoundStackNode<BasicParseForest>, AbstractParse<BasicParseForest, BasicElkhoundStackNode<BasicParseForest>>> elkhoundStackManager =
                                    new BasicElkhoundStackManager<>();

                                ReduceManager<BasicParseForest, BasicParseNode, BasicDerivation, BasicElkhoundStackNode<BasicParseForest>, AbstractParse<BasicParseForest, BasicElkhoundStackNode<BasicParseForest>>> basicElkhoundReduceManager =
                                    new ReduceManager<>(parseTable, elkhoundStackManager, basicParseForestManager,
                                        variant.parseForestConstruction);

                                return new Parser<>(Parse.factory(), parseTable, activeStacksFactory,
                                    forActorStacksFactory, elkhoundStackManager, basicParseForestManager,
                                    basicElkhoundReduceManager);
                            case HybridElkhound:
                                ElkhoundStackManager<BasicParseForest, HybridElkhoundStackNode<BasicParseForest>, AbstractParse<BasicParseForest, HybridElkhoundStackNode<BasicParseForest>>> elkhoundHybridStackManager =
                                    new HybridElkhoundStackManager<>();

                                ReduceManager<BasicParseForest, BasicParseNode, BasicDerivation, HybridElkhoundStackNode<BasicParseForest>, AbstractParse<BasicParseForest, HybridElkhoundStackNode<BasicParseForest>>> hybridElkhoundReduceManager =
                                    new ReduceManager<>(parseTable, elkhoundHybridStackManager, basicParseForestManager,
                                        variant.parseForestConstruction);

                                return new Parser<>(Parse.factory(), parseTable, activeStacksFactory,
                                    forActorStacksFactory, elkhoundHybridStackManager, basicParseForestManager,
                                    hybridElkhoundReduceManager);
                            default:
                                throw new IllegalStateException();
                        }
                    default:
                        throw new IllegalStateException(
                            "Only Elkhound or basic reducing possible with basic parse forest representation");
                }
            case Null:
            case Hybrid:
                ParseForestManager<HybridParseForest, HybridParseNode, HybridDerivation> hybridParseForestManager;

                if(variant.parseForestRepresentation == ParseForestRepresentation.Null)
                    hybridParseForestManager = new NullParseForestManager();
                else
                    hybridParseForestManager = new HybridParseForestManager();

                switch(variant.reducing) {
                    case Elkhound:
                        switch(variant.stackRepresentation) {
                            case BasicElkhound:
                                ElkhoundStackManager<HybridParseForest, BasicElkhoundStackNode<HybridParseForest>, AbstractParse<HybridParseForest, BasicElkhoundStackNode<HybridParseForest>>> basicElkhoundStackManager =
                                    new BasicElkhoundStackManager<>();

                                ElkhoundReduceManager<HybridParseForest, HybridParseNode, HybridDerivation, BasicElkhoundStackNode<HybridParseForest>, AbstractParse<HybridParseForest, BasicElkhoundStackNode<HybridParseForest>>> basicElkhoundReducer =
                                    new ElkhoundReduceManager<>(parseTable, basicElkhoundStackManager,
                                        hybridParseForestManager, variant.parseForestConstruction);

                                return new ElkhoundParser<>(Parse.factory(), parseTable, activeStacksFactory,
                                    forActorStacksFactory, basicElkhoundStackManager, hybridParseForestManager,
                                    basicElkhoundReducer);
                            case HybridElkhound:
                                ElkhoundStackManager<HybridParseForest, HybridElkhoundStackNode<HybridParseForest>, AbstractParse<HybridParseForest, HybridElkhoundStackNode<HybridParseForest>>> hybridElkhoundStackManager =
                                    new HybridElkhoundStackManager<>();

                                ElkhoundReduceManager<HybridParseForest, HybridParseNode, HybridDerivation, HybridElkhoundStackNode<HybridParseForest>, AbstractParse<HybridParseForest, HybridElkhoundStackNode<HybridParseForest>>> hybridElkhoundReducer =
                                    new ElkhoundReduceManager<>(parseTable, hybridElkhoundStackManager,
                                        hybridParseForestManager, variant.parseForestConstruction);

                                return new ElkhoundParser<>(Parse.factory(), parseTable, activeStacksFactory,
                                    forActorStacksFactory, hybridElkhoundStackManager, hybridParseForestManager,
                                    hybridElkhoundReducer);
                            default:
                                throw new IllegalStateException("Elkhound reducing requires Elkhound stack");
                        }
                    case Basic:
                        switch(variant.stackRepresentation) {
                            case Basic:
                                AbstractStackManager<HybridParseForest, BasicStackNode<HybridParseForest>, AbstractParse<HybridParseForest, BasicStackNode<HybridParseForest>>> basicStackManager =
                                    new BasicStackManager<>();

                                ReduceManager<HybridParseForest, HybridParseNode, HybridDerivation, BasicStackNode<HybridParseForest>, AbstractParse<HybridParseForest, BasicStackNode<HybridParseForest>>> basicReducer =
                                    new ReduceManager<>(parseTable, basicStackManager, hybridParseForestManager,
                                        variant.parseForestConstruction);

                                return new Parser<>(Parse.factory(), parseTable, activeStacksFactory,
                                    forActorStacksFactory, basicStackManager, hybridParseForestManager, basicReducer);
                            case Hybrid:
                                AbstractStackManager<HybridParseForest, HybridStackNode<HybridParseForest>, AbstractParse<HybridParseForest, HybridStackNode<HybridParseForest>>> hybridStackManager =
                                    new HybridStackManager<>();

                                ReduceManager<HybridParseForest, HybridParseNode, HybridDerivation, HybridStackNode<HybridParseForest>, AbstractParse<HybridParseForest, HybridStackNode<HybridParseForest>>> hybridReducer =
                                    new ReduceManager<>(parseTable, hybridStackManager, hybridParseForestManager,
                                        variant.parseForestConstruction);

                                return new Parser<>(Parse.factory(), parseTable, activeStacksFactory,
                                    forActorStacksFactory, hybridStackManager, hybridParseForestManager, hybridReducer);
                            case BasicElkhound:
                                ElkhoundStackManager<HybridParseForest, BasicElkhoundStackNode<HybridParseForest>, AbstractParse<HybridParseForest, BasicElkhoundStackNode<HybridParseForest>>> basicElkhoundStackManager =
                                    new BasicElkhoundStackManager<>();

                                ReduceManager<HybridParseForest, HybridParseNode, HybridDerivation, BasicElkhoundStackNode<HybridParseForest>, AbstractParse<HybridParseForest, BasicElkhoundStackNode<HybridParseForest>>> basicElkhoundReducer =
                                    new ReduceManager<>(parseTable, basicElkhoundStackManager, hybridParseForestManager,
                                        variant.parseForestConstruction);

                                return new Parser<>(Parse.factory(), parseTable, activeStacksFactory,
                                    forActorStacksFactory, basicElkhoundStackManager, hybridParseForestManager,
                                    basicElkhoundReducer);
                            case HybridElkhound:
                                ElkhoundStackManager<HybridParseForest, HybridElkhoundStackNode<HybridParseForest>, AbstractParse<HybridParseForest, HybridElkhoundStackNode<HybridParseForest>>> hybridElkhoundStackManager =
                                    new HybridElkhoundStackManager<>();

                                ReduceManager<HybridParseForest, HybridParseNode, HybridDerivation, HybridElkhoundStackNode<HybridParseForest>, AbstractParse<HybridParseForest, HybridElkhoundStackNode<HybridParseForest>>> hybridElkhoundReducer =
                                    new ReduceManager<>(parseTable, hybridElkhoundStackManager,
                                        hybridParseForestManager, variant.parseForestConstruction);

                                return new Parser<>(Parse.factory(), parseTable, activeStacksFactory,
                                    forActorStacksFactory, hybridElkhoundStackManager, hybridParseForestManager,
                                    hybridElkhoundReducer);
                            default:
                                throw new IllegalStateException();
                        }
                    default:
                        throw new IllegalStateException(
                            "Only Elkhound or basic reducing possible with basic parse forest representation");
                }
            case DataDependent:
                DataDependentParseForestManager dataDependentParseForestManager = new DataDependentParseForestManager();

                switch(variant.stackRepresentation) {
                    case Basic:
                        AbstractStackManager<DataDependentParseForest, BasicStackNode<DataDependentParseForest>, AbstractParse<DataDependentParseForest, BasicStackNode<DataDependentParseForest>>> basicStackManager =
                            new BasicStackManager<>();

                        DataDependentReduceManager<DataDependentParseForest, DataDependentParseNode, DataDependentDerivation, BasicStackNode<DataDependentParseForest>, AbstractParse<DataDependentParseForest, BasicStackNode<DataDependentParseForest>>> basicDataDependentReducer =
                            new DataDependentReduceManager<>(parseTable, basicStackManager,
                                dataDependentParseForestManager, variant.parseForestConstruction);

                        return new Parser<>(Parse.factory(), parseTable, activeStacksFactory, forActorStacksFactory,
                            basicStackManager, dataDependentParseForestManager, basicDataDependentReducer);
                    case Hybrid:
                        AbstractStackManager<DataDependentParseForest, HybridStackNode<DataDependentParseForest>, AbstractParse<DataDependentParseForest, HybridStackNode<DataDependentParseForest>>> hybridStackManager =
                            new HybridStackManager<>();

                        DataDependentReduceManager<DataDependentParseForest, DataDependentParseNode, DataDependentDerivation, HybridStackNode<DataDependentParseForest>, AbstractParse<DataDependentParseForest, HybridStackNode<DataDependentParseForest>>> hybridDataDependentReducer =
                            new DataDependentReduceManager<>(parseTable, hybridStackManager,
                                dataDependentParseForestManager, variant.parseForestConstruction);

                        return new Parser<>(Parse.factory(), parseTable, activeStacksFactory, forActorStacksFactory,
                            hybridStackManager, dataDependentParseForestManager, hybridDataDependentReducer);
                    default:
                        throw new IllegalStateException();
                }
            case LayoutSensitive:
                LayoutSensitiveParseForestManager layoutSensitiveParseForestManager =
                    new LayoutSensitiveParseForestManager();

                switch(variant.stackRepresentation) {
                    case Basic:
                        AbstractStackManager<LayoutSensitiveParseForest, BasicStackNode<LayoutSensitiveParseForest>, AbstractParse<LayoutSensitiveParseForest, BasicStackNode<LayoutSensitiveParseForest>>> basicStackManager =
                            new BasicStackManager<>();

                        LayoutSensitiveReduceManager<LayoutSensitiveParseForest, LayoutSensitiveParseNode, LayoutSensitiveDerivation, BasicStackNode<LayoutSensitiveParseForest>, AbstractParse<LayoutSensitiveParseForest, BasicStackNode<LayoutSensitiveParseForest>>> basicLayoutSensitiveReducer =
                            new LayoutSensitiveReduceManager<>(parseTable, basicStackManager,
                                layoutSensitiveParseForestManager, variant.parseForestConstruction);

                        return new Parser<>(Parse.factory(), parseTable, activeStacksFactory, forActorStacksFactory,
                            basicStackManager, layoutSensitiveParseForestManager, basicLayoutSensitiveReducer);
                    case Hybrid:
                        AbstractStackManager<LayoutSensitiveParseForest, HybridStackNode<LayoutSensitiveParseForest>, AbstractParse<LayoutSensitiveParseForest, HybridStackNode<LayoutSensitiveParseForest>>> hybridStackManager =
                            new HybridStackManager<>();

                        LayoutSensitiveReduceManager<LayoutSensitiveParseForest, LayoutSensitiveParseNode, LayoutSensitiveDerivation, HybridStackNode<LayoutSensitiveParseForest>, AbstractParse<LayoutSensitiveParseForest, HybridStackNode<LayoutSensitiveParseForest>>> hybridLayoutSensitiveReducer =
                            new LayoutSensitiveReduceManager<>(parseTable, hybridStackManager,
                                layoutSensitiveParseForestManager, variant.parseForestConstruction);

                        return new Parser<>(Parse.factory(), parseTable, activeStacksFactory, forActorStacksFactory,
                            hybridStackManager, layoutSensitiveParseForestManager, hybridLayoutSensitiveReducer);
                    default:
                        throw new IllegalStateException();
                }
        }
    }

    public static List<Parser<?, ?, ?, ?, ?>> allParsers(IParseTable parseTable) {
        List<Parser<?, ?, ?, ?, ?>> parsers = new ArrayList<>();

        for(ParserVariant variant : allVariants()) {
            Parser<?, ?, ?, ?, ?> parser = getParser(parseTable,
                new ParserVariant(variant.activeStacksRepresentation, variant.forActorStacksRepresentation,
                    variant.parseForestRepresentation, variant.parseForestConstruction, variant.stackRepresentation,
                    variant.reducing));

            parsers.add(parser);
        }

        return parsers;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" }) public static JSGLR2<?, IStrategoTerm>
        getJSGLR2(IParseTable parseTable, ParserVariant variant) {
        IParser<?, ?> parser = getParser(parseTable, variant);
        IImploder<?, IStrategoTerm> imploder;

        if(variant.parseForestRepresentation == ParseForestRepresentation.Null)
            imploder = new NullParseForestStrategoImploder();
        else
            imploder = new StrategoTermImploder();

        return new JSGLR2(parser, imploder);
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
