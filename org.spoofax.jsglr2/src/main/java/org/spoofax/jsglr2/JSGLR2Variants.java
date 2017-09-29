package org.spoofax.jsglr2;

import java.util.ArrayList;
import java.util.List;

import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.jsglr2.imploder.IImploder;
import org.spoofax.jsglr2.imploder.hybrid.HStrategoImploder;
import org.spoofax.jsglr2.imploder.symbolrule.SRStrategoImploder;
import org.spoofax.jsglr2.parseforest.hybrid.Derivation;
import org.spoofax.jsglr2.parseforest.hybrid.HParseForest;
import org.spoofax.jsglr2.parseforest.hybrid.HParseForestManager;
import org.spoofax.jsglr2.parseforest.hybrid.ParseNode;
import org.spoofax.jsglr2.parseforest.symbolrule.RuleNode;
import org.spoofax.jsglr2.parseforest.symbolrule.SRParseForest;
import org.spoofax.jsglr2.parseforest.symbolrule.SRParseForestManager;
import org.spoofax.jsglr2.parseforest.symbolrule.SymbolNode;
import org.spoofax.jsglr2.parser.IParser;
import org.spoofax.jsglr2.parser.Parser;
import org.spoofax.jsglr2.parser.Reducer;
import org.spoofax.jsglr2.parser.ReducerElkhound;
import org.spoofax.jsglr2.parsetable.IParseTable;
import org.spoofax.jsglr2.parsetable.ParseTableReadException;
import org.spoofax.jsglr2.stack.StackManager;
import org.spoofax.jsglr2.stack.elkhound.StandardElkhoundStackManager;
import org.spoofax.jsglr2.stack.elkhound.ElkhoundStackNode;
import org.spoofax.jsglr2.stack.standard.StandardStackManager;
import org.spoofax.jsglr2.stack.standard.StandardStackNode;

public class JSGLR2Variants {
    
    public enum ParseForestRepresentation {
        SymbolRule, Hybrid
    }

    public static ParseForestRepresentation[] parseForestRepresentationVariants = new ParseForestRepresentation[]{
        ParseForestRepresentation.SymbolRule,
        ParseForestRepresentation.Hybrid
    };

    public static boolean[] elkhoundStackVariants = new boolean[]{
        false,
        true
    };

    public static boolean[] elkhoundReducingVariants = new boolean[]{
        false,
        true
    };
    
    public static class Variant {
        public ParseForestRepresentation parseForestRepresentation;
        public boolean elkhoundStack;
        public boolean elkhoundReducing;
        
        public Variant(ParseForestRepresentation parseForestRepresentation, boolean elkhoundStack, boolean elkhoundReducing) {
            this.parseForestRepresentation = parseForestRepresentation;
            this.elkhoundStack = elkhoundStack;
            this.elkhoundReducing = elkhoundReducing;
        }
    }
    
    public static List<Variant> allVariants() {
        List<Variant> variants = new ArrayList<Variant>();
        
        for (ParseForestRepresentation parseForestRepresentation : parseForestRepresentationVariants) {
            for (boolean elkhoundStack : elkhoundStackVariants) {
                for (boolean elkhoundReducing : elkhoundReducingVariants) {
                    Variant variant = new Variant(parseForestRepresentation, elkhoundStack, elkhoundReducing);
                    
                    variants.add(variant);
                }
            }
        }
        
        return variants;
    }
    
    public static Parser<?, ?, ?, ?> getParser(IParseTable parseTable, ParseForestRepresentation parseForestRepresentation, boolean elkhoundStack, boolean elkhoundReducing) {
        if (elkhoundReducing && !elkhoundStack)
            throw new IllegalStateException("Elkhound reducing requires Elkhound stack");
        
        switch (parseForestRepresentation) {
            default:
            case SymbolRule:
                SRParseForestManager srParseForestManager = new SRParseForestManager();
                
                if (elkhoundReducing) {
                    StackManager<ElkhoundStackNode<SRParseForest>, SRParseForest> srElkhoundStackManager = new StandardElkhoundStackManager<SRParseForest>();
                    Reducer<ElkhoundStackNode<SRParseForest>, SRParseForest, SymbolNode, RuleNode> srElkhoundReducer = new ReducerElkhound<SRParseForest, SymbolNode, RuleNode>(parseTable, srElkhoundStackManager, srParseForestManager);
                    
                    return new Parser<ElkhoundStackNode<SRParseForest>, SRParseForest, SymbolNode, RuleNode>(parseTable, srElkhoundStackManager, srParseForestManager, srElkhoundReducer);
                } else {
                    if (elkhoundStack) {
                        StackManager<ElkhoundStackNode<SRParseForest>, SRParseForest> srElkhoundStackManager = new StandardElkhoundStackManager<SRParseForest>();
                        Reducer<ElkhoundStackNode<SRParseForest>, SRParseForest, SymbolNode, RuleNode> srReducer = new Reducer<ElkhoundStackNode<SRParseForest>, SRParseForest, SymbolNode, RuleNode>(parseTable, srElkhoundStackManager, srParseForestManager);
                        
                        return new Parser<ElkhoundStackNode<SRParseForest>, SRParseForest, SymbolNode, RuleNode>(parseTable, srElkhoundStackManager, srParseForestManager, srReducer);
                    } else {
                        StackManager<StandardStackNode<SRParseForest>, SRParseForest> srStandardStackManager = new StandardStackManager<SRParseForest>();
                        Reducer<StandardStackNode<SRParseForest>, SRParseForest, SymbolNode, RuleNode> srReducer = new Reducer<StandardStackNode<SRParseForest>, SRParseForest, SymbolNode, RuleNode>(parseTable, srStandardStackManager, srParseForestManager);
                        
                        return new Parser<StandardStackNode<SRParseForest>, SRParseForest, SymbolNode, RuleNode>(parseTable, srStandardStackManager, srParseForestManager, srReducer);
                    }
                }
            case Hybrid:
                HParseForestManager hParseForestManager = new HParseForestManager();
                
                if (elkhoundReducing) {
                    StackManager<ElkhoundStackNode<HParseForest>, HParseForest> hElkhoundStackManager = new StandardElkhoundStackManager<HParseForest>();
                    Reducer<ElkhoundStackNode<HParseForest>, HParseForest, ParseNode, Derivation> hElkhoundReducer = new ReducerElkhound<HParseForest, ParseNode, Derivation>(parseTable, hElkhoundStackManager, hParseForestManager);
                    
                    return new Parser<ElkhoundStackNode<HParseForest>, HParseForest, ParseNode, Derivation>(parseTable, hElkhoundStackManager, hParseForestManager, hElkhoundReducer);
                } else {
                    if (elkhoundStack) {
                        StackManager<ElkhoundStackNode<HParseForest>, HParseForest> hElkhoundStackManager = new StandardElkhoundStackManager<HParseForest>();
                        Reducer<ElkhoundStackNode<HParseForest>, HParseForest, ParseNode, Derivation> hReducer = new Reducer<ElkhoundStackNode<HParseForest>, HParseForest, ParseNode, Derivation>(parseTable, hElkhoundStackManager, hParseForestManager);
                        
                        return new Parser<ElkhoundStackNode<HParseForest>, HParseForest, ParseNode, Derivation>(parseTable, hElkhoundStackManager, hParseForestManager, hReducer);
                    } else {
                        StackManager<StandardStackNode<HParseForest>, HParseForest> hStandardStackManager = new StandardStackManager<HParseForest>();
                        Reducer<StandardStackNode<HParseForest>, HParseForest, ParseNode, Derivation> hReducer = new Reducer<StandardStackNode<HParseForest>, HParseForest, ParseNode, Derivation>(parseTable, hStandardStackManager, hParseForestManager);
                        
                        return new Parser<StandardStackNode<HParseForest>, HParseForest, ParseNode, Derivation>(parseTable, hStandardStackManager, hParseForestManager, hReducer);
                    }
                }
        }
    }
    
    public static List<Parser<?, ?, ?, ?>> allParsers(IParseTable parseTable) {
        List<Parser<?, ?, ?, ?>> parsers = new ArrayList<Parser<?, ?, ?, ?>>();
        
        for (Variant variant : allVariants()) {
            Parser<?, ?, ?, ?> parser = getParser(parseTable, variant.parseForestRepresentation, variant.elkhoundStack, variant.elkhoundReducing);
            
            parsers.add(parser);
        }
        
        return parsers;
    }
    
    public static JSGLR2<?, ?, IStrategoTerm> getJSGLR2(IParseTable parseTable, ParseForestRepresentation parseForestRepresentation, boolean elkhoundStack, boolean elkhoundReducing) throws ParseTableReadException {
        IParser<?, ?> parser = getParser(parseTable, parseForestRepresentation, elkhoundStack, elkhoundReducing);
        IImploder<?, IStrategoTerm> imploder;
        
        switch (parseForestRepresentation) {
            default:
            case SymbolRule:
                imploder = new SRStrategoImploder();
                
                break;
            case Hybrid:
                imploder = new HStrategoImploder();
                
                break;
        }
        
        return new JSGLR2(parser, imploder);
    }
    
    public static List<JSGLR2<?, ?, IStrategoTerm>> allJSGLR2(IParseTable parseTable) throws ParseTableReadException {
        List<JSGLR2<?, ?, IStrategoTerm>> jsglr2s = new ArrayList<JSGLR2<?, ?, IStrategoTerm>>();
        
        for (Variant variant : allVariants()) {
            JSGLR2<?, ?, IStrategoTerm> jsglr2 = getJSGLR2(parseTable, variant.parseForestRepresentation, variant.elkhoundStack, variant.elkhoundReducing);
            
            jsglr2s.add(jsglr2);
        }
        
        return jsglr2s;
    }
    
}
