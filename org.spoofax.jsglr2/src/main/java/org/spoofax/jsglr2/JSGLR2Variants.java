package org.spoofax.jsglr2;

import java.util.ArrayList;
import java.util.List;

import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.jsglr2.imploder.BasicParseForestStrategoImploder;
import org.spoofax.jsglr2.imploder.HybridParseForestStrategoImploder;
import org.spoofax.jsglr2.imploder.IImploder;
import org.spoofax.jsglr2.imploder.NullParseForestStrategoImploder;
import org.spoofax.jsglr2.parseforest.ParseForestManager;
import org.spoofax.jsglr2.parseforest.basic.BasicParseForest;
import org.spoofax.jsglr2.parseforest.basic.BasicParseForestManager;
import org.spoofax.jsglr2.parseforest.basic.RuleNode;
import org.spoofax.jsglr2.parseforest.basic.SymbolNode;
import org.spoofax.jsglr2.parseforest.empty.NullParseForestManager;
import org.spoofax.jsglr2.parseforest.hybrid.Derivation;
import org.spoofax.jsglr2.parseforest.hybrid.HybridParseForest;
import org.spoofax.jsglr2.parseforest.hybrid.HybridParseForestManager;
import org.spoofax.jsglr2.parseforest.hybrid.ParseNode;
import org.spoofax.jsglr2.parser.IParser;
import org.spoofax.jsglr2.parser.Parser;
import org.spoofax.jsglr2.parsetable.IParseTable;
import org.spoofax.jsglr2.reducing.ReduceManager;
import org.spoofax.jsglr2.reducing.ReduceManagerElkhound;
import org.spoofax.jsglr2.stack.StackManager;
import org.spoofax.jsglr2.stack.basic.AbstractBasicStackNode;
import org.spoofax.jsglr2.stack.basic.BasicStackManager;
import org.spoofax.jsglr2.stack.basic.HybridStackManager;
import org.spoofax.jsglr2.stack.collections.ActiveStacksFactory;
import org.spoofax.jsglr2.stack.collections.ForActorStacksFactory;
import org.spoofax.jsglr2.stack.collections.IActiveStacksFactory;
import org.spoofax.jsglr2.stack.collections.IForActorStacksFactory;
import org.spoofax.jsglr2.stack.elkhound.AbstractElkhoundStackManager;
import org.spoofax.jsglr2.stack.elkhound.AbstractElkhoundStackNode;
import org.spoofax.jsglr2.stack.elkhound.BasicElkhoundStackManager;
import org.spoofax.jsglr2.stack.elkhound.HybridElkhoundStackManager;

public class JSGLR2Variants {

    public enum ActiveStacksRepresentation {
        Array
    }

    public enum ForActorStacksRepresentation {
        Array
    }

    public enum ParseForestRepresentation {
        Null, Basic, Hybrid
    }

    public enum ParseForestConstruction {
        Full, Optimized
    }

    public enum StackRepresentation {
        Basic, Hybrid, BasicElkhound, HybridElkhound
    }

    public enum Reducing {
        Basic, Elkhound
    }

    public static class Variant {
        public ActiveStacksRepresentation activeStacksRepresentation;
        public ForActorStacksRepresentation forActorStacksRepresentation;
        public ParseForestRepresentation parseForestRepresentation;
        public ParseForestConstruction parseForestConstruction;
        public StackRepresentation stackRepresentation;
        public Reducing reducing;

        public Variant(ActiveStacksRepresentation activeStacksRepresentation,
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
    }

    public static List<Variant> allVariants() {
        List<Variant> variants = new ArrayList<Variant>();

        for(ActiveStacksRepresentation activeStacksRepresentation : ActiveStacksRepresentation.values()) {
            for(ForActorStacksRepresentation forActorStacksRepresentation : ForActorStacksRepresentation.values()) {
                for(ParseForestRepresentation parseForestRepresentation : ParseForestRepresentation.values()) {
                    if(parseForestRepresentation != ParseForestRepresentation.Null)
                        for(ParseForestConstruction parseForestConstruction : ParseForestConstruction.values()) {
                            for(StackRepresentation stackRepresentation : StackRepresentation.values()) {
                                for(Reducing reducing : Reducing.values()) {
                                    Variant variant = new Variant(activeStacksRepresentation,
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

    public static Parser<?, ?, ?, ?> getParser(IParseTable parseTable, Variant variant) {
        if(!variant.isValid())
            throw new IllegalStateException("Invalid parser variant");

        IActiveStacksFactory activeStacksFactory;

        switch(variant.activeStacksRepresentation) {
            case Array:
                activeStacksFactory = new ActiveStacksFactory();
                break;
            default:
                activeStacksFactory = null;
                break;
        }

        IForActorStacksFactory forActorStacksFactory;

        switch(variant.activeStacksRepresentation) {
            case Array:
                forActorStacksFactory = new ForActorStacksFactory();
                break;
            default:
                forActorStacksFactory = null;
                break;
        }

        switch(variant.parseForestRepresentation) {
            default:
            case Basic:
                BasicParseForestManager basicParseForestManager = new BasicParseForestManager();

                if(variant.reducing == Reducing.Elkhound) {
                    AbstractElkhoundStackManager<BasicParseForest, AbstractElkhoundStackNode<BasicParseForest>> elkhoundStackManager;

                    if(variant.stackRepresentation == StackRepresentation.HybridElkhound)
                        elkhoundStackManager = new HybridElkhoundStackManager<BasicParseForest>();
                    else
                        elkhoundStackManager = new BasicElkhoundStackManager<BasicParseForest>();

                    ReduceManager<BasicParseForest, SymbolNode, RuleNode, AbstractElkhoundStackNode<BasicParseForest>> elkhoundReducer =
                        new ReduceManagerElkhound<BasicParseForest, SymbolNode, RuleNode>(parseTable,
                            elkhoundStackManager, basicParseForestManager, variant.parseForestConstruction);

                    return new Parser<BasicParseForest, SymbolNode, RuleNode, AbstractElkhoundStackNode<BasicParseForest>>(
                        parseTable, activeStacksFactory, forActorStacksFactory, elkhoundStackManager,
                        basicParseForestManager, elkhoundReducer);
                } else {
                    if(variant.stackRepresentation == StackRepresentation.Basic
                        || variant.stackRepresentation == StackRepresentation.Hybrid) {
                        StackManager<BasicParseForest, AbstractBasicStackNode<BasicParseForest>> basicStackManager;

                        if(variant.stackRepresentation == StackRepresentation.Basic)
                            basicStackManager = new BasicStackManager<BasicParseForest>();
                        else
                            basicStackManager = new HybridStackManager<BasicParseForest>();

                        ReduceManager<BasicParseForest, SymbolNode, RuleNode, AbstractBasicStackNode<BasicParseForest>> basicReducer =
                            new ReduceManager<BasicParseForest, SymbolNode, RuleNode, AbstractBasicStackNode<BasicParseForest>>(
                                parseTable, basicStackManager, basicParseForestManager,
                                variant.parseForestConstruction);

                        return new Parser<BasicParseForest, SymbolNode, RuleNode, AbstractBasicStackNode<BasicParseForest>>(
                            parseTable, activeStacksFactory, forActorStacksFactory, basicStackManager,
                            basicParseForestManager, basicReducer);
                    } else {
                        StackManager<BasicParseForest, AbstractElkhoundStackNode<BasicParseForest>> elkhoundStackManager;

                        if(variant.stackRepresentation == StackRepresentation.HybridElkhound)
                            elkhoundStackManager = new HybridElkhoundStackManager<BasicParseForest>();
                        else
                            elkhoundStackManager = new BasicElkhoundStackManager<BasicParseForest>();

                        ReduceManager<BasicParseForest, SymbolNode, RuleNode, AbstractElkhoundStackNode<BasicParseForest>> basicReducer =
                            new ReduceManager<BasicParseForest, SymbolNode, RuleNode, AbstractElkhoundStackNode<BasicParseForest>>(
                                parseTable, elkhoundStackManager, basicParseForestManager,
                                variant.parseForestConstruction);

                        return new Parser<BasicParseForest, SymbolNode, RuleNode, AbstractElkhoundStackNode<BasicParseForest>>(
                            parseTable, activeStacksFactory, forActorStacksFactory, elkhoundStackManager,
                            basicParseForestManager, basicReducer);
                    }
                }
            case Null:
            case Hybrid:
                ParseForestManager<HybridParseForest, ParseNode, Derivation> hybridParseForestManager;

                if(variant.parseForestRepresentation == ParseForestRepresentation.Null)
                    hybridParseForestManager = new NullParseForestManager();
                else
                    hybridParseForestManager = new HybridParseForestManager();

                if(variant.reducing == Reducing.Elkhound) {
                    AbstractElkhoundStackManager<HybridParseForest, AbstractElkhoundStackNode<HybridParseForest>> elkhoundStackManager;

                    if(variant.stackRepresentation == StackRepresentation.HybridElkhound)
                        elkhoundStackManager = new HybridElkhoundStackManager<HybridParseForest>();
                    else
                        elkhoundStackManager = new BasicElkhoundStackManager<HybridParseForest>();

                    ReduceManager<HybridParseForest, ParseNode, Derivation, AbstractElkhoundStackNode<HybridParseForest>> elkhoundReducer =
                        new ReduceManagerElkhound<HybridParseForest, ParseNode, Derivation>(parseTable,
                            elkhoundStackManager, hybridParseForestManager, variant.parseForestConstruction);

                    return new Parser<HybridParseForest, ParseNode, Derivation, AbstractElkhoundStackNode<HybridParseForest>>(
                        parseTable, activeStacksFactory, forActorStacksFactory, elkhoundStackManager,
                        hybridParseForestManager, elkhoundReducer);
                } else {
                    if(variant.stackRepresentation == StackRepresentation.Basic
                        || variant.stackRepresentation == StackRepresentation.Hybrid) {
                        StackManager<HybridParseForest, AbstractBasicStackNode<HybridParseForest>> basicStackManager;

                        if(variant.stackRepresentation == StackRepresentation.Basic)
                            basicStackManager = new BasicStackManager<HybridParseForest>();
                        else
                            basicStackManager = new HybridStackManager<HybridParseForest>();

                        ReduceManager<HybridParseForest, ParseNode, Derivation, AbstractBasicStackNode<HybridParseForest>> hybridReducer =
                            new ReduceManager<HybridParseForest, ParseNode, Derivation, AbstractBasicStackNode<HybridParseForest>>(
                                parseTable, basicStackManager, hybridParseForestManager,
                                variant.parseForestConstruction);

                        return new Parser<HybridParseForest, ParseNode, Derivation, AbstractBasicStackNode<HybridParseForest>>(
                            parseTable, activeStacksFactory, forActorStacksFactory, basicStackManager,
                            hybridParseForestManager, hybridReducer);
                    } else {
                        StackManager<HybridParseForest, AbstractElkhoundStackNode<HybridParseForest>> elkhoundStackManager;

                        if(variant.stackRepresentation == StackRepresentation.HybridElkhound)
                            elkhoundStackManager = new HybridElkhoundStackManager<HybridParseForest>();
                        else
                            elkhoundStackManager = new BasicElkhoundStackManager<HybridParseForest>();

                        ReduceManager<HybridParseForest, ParseNode, Derivation, AbstractElkhoundStackNode<HybridParseForest>> hybridReducer =
                            new ReduceManager<HybridParseForest, ParseNode, Derivation, AbstractElkhoundStackNode<HybridParseForest>>(
                                parseTable, elkhoundStackManager, hybridParseForestManager,
                                variant.parseForestConstruction);

                        return new Parser<HybridParseForest, ParseNode, Derivation, AbstractElkhoundStackNode<HybridParseForest>>(
                            parseTable, activeStacksFactory, forActorStacksFactory, elkhoundStackManager,
                            hybridParseForestManager, hybridReducer);
                    }
                }
        }
    }

    public static Parser<?, ?, ?, ?> getParser(IParseTable parseTable,
        ActiveStacksRepresentation activeStacksRepresentation,
        ForActorStacksRepresentation forActorStacksRepresentation, ParseForestRepresentation parseForestRepresentation,
        ParseForestConstruction parseForestConstruction, StackRepresentation stackRepresentation, Reducing reducing) {
        return getParser(parseTable, new Variant(activeStacksRepresentation, forActorStacksRepresentation,
            parseForestRepresentation, parseForestConstruction, stackRepresentation, reducing));
    }

    public static List<Parser<?, ?, ?, ?>> allParsers(IParseTable parseTable) {
        List<Parser<?, ?, ?, ?>> parsers = new ArrayList<Parser<?, ?, ?, ?>>();

        for(Variant variant : allVariants()) {
            Parser<?, ?, ?, ?> parser = getParser(parseTable, variant.activeStacksRepresentation,
                variant.forActorStacksRepresentation, variant.parseForestRepresentation,
                variant.parseForestConstruction, variant.stackRepresentation, variant.reducing);

            parsers.add(parser);
        }

        return parsers;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static JSGLR2<?, IStrategoTerm> getJSGLR2(IParseTable parseTable, Variant variant) {
        IParser<?, ?> parser = getParser(parseTable, variant);
        IImploder<?, IStrategoTerm> imploder;

        switch(variant.parseForestRepresentation) {
            default:
            case Basic:
                imploder = new BasicParseForestStrategoImploder();

                break;
            case Hybrid:
                imploder = new HybridParseForestStrategoImploder();

                break;
            case Null:
                imploder = new NullParseForestStrategoImploder();

                break;
        }

        return new JSGLR2(parser, imploder);
    }

    public static JSGLR2<?, IStrategoTerm> getJSGLR2(IParseTable parseTable,
        ActiveStacksRepresentation activeStacksRepresentation,
        ForActorStacksRepresentation forActorStacksRepresentation, ParseForestRepresentation parseForestRepresentation,
        ParseForestConstruction parseForestConstruction, StackRepresentation stackRepresentation, Reducing reducing) {
        return getJSGLR2(parseTable, new Variant(activeStacksRepresentation, forActorStacksRepresentation,
            parseForestRepresentation, parseForestConstruction, stackRepresentation, reducing));
    }

    public static List<JSGLR2<?, IStrategoTerm>> allJSGLR2(IParseTable parseTable) {
        List<JSGLR2<?, IStrategoTerm>> jsglr2s = new ArrayList<JSGLR2<?, IStrategoTerm>>();

        for(Variant variant : allVariants()) {
            JSGLR2<?, IStrategoTerm> jsglr2 = getJSGLR2(parseTable, variant);

            jsglr2s.add(jsglr2);
        }

        return jsglr2s;
    }

}
