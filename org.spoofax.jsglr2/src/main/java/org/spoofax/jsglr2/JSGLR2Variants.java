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
import org.spoofax.jsglr2.stack.StackManager;
import org.spoofax.jsglr2.stack.elkhound.StandardElkhoundStackManager;
import org.spoofax.jsglr2.stack.elkhound.ElkhoundStackNode;
import org.spoofax.jsglr2.stack.elkhound.HybridElkhoundStackManager;
import org.spoofax.jsglr2.stack.standard.StandardStackManager;
import org.spoofax.jsglr2.stack.standard.StandardStackNode;

public class JSGLR2Variants {
    
    public enum ParseForestRepresentation {
        SymbolRule, Hybrid
    }
    
    public enum StackRepresentation {
        Default, ElkhoundStandard, ElkhoundHybrid
    }
    
    public enum Reducing {
        Default, Elkhound
    }

    public static ParseForestRepresentation[] parseForestRepresentationVariants = new ParseForestRepresentation[]{
        ParseForestRepresentation.SymbolRule,
        ParseForestRepresentation.Hybrid
    };

    public static StackRepresentation[] stackRepresentationVariants = new StackRepresentation[]{
    		StackRepresentation.Default,
        StackRepresentation.ElkhoundStandard,
        StackRepresentation.ElkhoundHybrid
    };

    public static Reducing[] reducingVariants = new Reducing[]{
    		Reducing.Default,
    		Reducing.Elkhound
    };
    
    public static class Variant {
        public ParseForestRepresentation parseForestRepresentation;
        public StackRepresentation stackRepresentation;
        public Reducing reducing;
        
        public Variant(ParseForestRepresentation parseForestRepresentation, StackRepresentation stackRepresentation, Reducing reducing) {
            this.parseForestRepresentation = parseForestRepresentation;
            this.stackRepresentation = stackRepresentation;
            this.reducing = reducing;
        }
        
        public boolean isValid() {
        		return reducing != Reducing.Elkhound || stackRepresentation != StackRepresentation.Default;
        }
        
        public String name() {
        		return "ParseForestRepresentation:" + parseForestRepresentation + "/" + "StackRepresentation:" + stackRepresentation + "/" + "Reducing:" + reducing;
        }
    }
    
    public static List<Variant> allVariants() {
        List<Variant> variants = new ArrayList<Variant>();
        
        for (ParseForestRepresentation parseForestRepresentation : parseForestRepresentationVariants) {
            for (StackRepresentation stackRepresentation : stackRepresentationVariants) {
                for (Reducing reducing : reducingVariants) {
                    Variant variant = new Variant(parseForestRepresentation, stackRepresentation, reducing);
                    
                    if (variant.isValid())
                    		variants.add(variant);
                }
            }
        }
        
        return variants;
    }
    
    public static Parser<?, ?, ?, ?> getParser(IParseTable parseTable, Variant variant) {
        if (!variant.isValid())
            throw new IllegalStateException("Invalid parser variant (Elkhound reducing requires Elkhound stack)");
        
        switch (variant.parseForestRepresentation) {
            default:
            case SymbolRule:
                SRParseForestManager srParseForestManager = new SRParseForestManager();
                
                if (variant.reducing == Reducing.Elkhound) {
	                	StackManager<ElkhoundStackNode<SRParseForest>, SRParseForest> srElkhoundStackManager;
	                    
	            		if (variant.stackRepresentation == StackRepresentation.ElkhoundHybrid)
	            			srElkhoundStackManager = new HybridElkhoundStackManager<SRParseForest>();
	            		else
	            			srElkhoundStackManager = new StandardElkhoundStackManager<SRParseForest>();
	            		
                    Reducer<ElkhoundStackNode<SRParseForest>, SRParseForest, SymbolNode, RuleNode> srElkhoundReducer = new ReducerElkhound<SRParseForest, SymbolNode, RuleNode>(parseTable, srElkhoundStackManager, srParseForestManager);
                    
                    return new Parser<ElkhoundStackNode<SRParseForest>, SRParseForest, SymbolNode, RuleNode>(parseTable, srElkhoundStackManager, srParseForestManager, srElkhoundReducer);
                } else {
                    if (variant.stackRepresentation == StackRepresentation.Default) {
                        StackManager<StandardStackNode<SRParseForest>, SRParseForest> srStandardStackManager = new StandardStackManager<SRParseForest>();
                        Reducer<StandardStackNode<SRParseForest>, SRParseForest, SymbolNode, RuleNode> srReducer = new Reducer<StandardStackNode<SRParseForest>, SRParseForest, SymbolNode, RuleNode>(parseTable, srStandardStackManager, srParseForestManager);
                        
                        return new Parser<StandardStackNode<SRParseForest>, SRParseForest, SymbolNode, RuleNode>(parseTable, srStandardStackManager, srParseForestManager, srReducer);
                    	} else {
                    		StackManager<ElkhoundStackNode<SRParseForest>, SRParseForest> srElkhoundStackManager;
                        
                    		if (variant.stackRepresentation == StackRepresentation.ElkhoundHybrid)
                    			srElkhoundStackManager = new HybridElkhoundStackManager<SRParseForest>();
                    		else
                    			srElkhoundStackManager = new StandardElkhoundStackManager<SRParseForest>();
                    		
                        Reducer<ElkhoundStackNode<SRParseForest>, SRParseForest, SymbolNode, RuleNode> srReducer = new Reducer<ElkhoundStackNode<SRParseForest>, SRParseForest, SymbolNode, RuleNode>(parseTable, srElkhoundStackManager, srParseForestManager);
                        
                        return new Parser<ElkhoundStackNode<SRParseForest>, SRParseForest, SymbolNode, RuleNode>(parseTable, srElkhoundStackManager, srParseForestManager, srReducer);
                    }
                }
            case Hybrid:
                HParseForestManager hParseForestManager = new HParseForestManager();
                
                if (variant.reducing == Reducing.Elkhound) {
	                	StackManager<ElkhoundStackNode<HParseForest>, HParseForest> hElkhoundStackManager;
	                    
	            		if (variant.stackRepresentation == StackRepresentation.ElkhoundHybrid)
	            			hElkhoundStackManager = new HybridElkhoundStackManager<HParseForest>();
	            		else
	            			hElkhoundStackManager = new StandardElkhoundStackManager<HParseForest>();
	            		
                    Reducer<ElkhoundStackNode<HParseForest>, HParseForest, ParseNode, Derivation> hElkhoundReducer = new ReducerElkhound<HParseForest, ParseNode, Derivation>(parseTable, hElkhoundStackManager, hParseForestManager);
                    
                    return new Parser<ElkhoundStackNode<HParseForest>, HParseForest, ParseNode, Derivation>(parseTable, hElkhoundStackManager, hParseForestManager, hElkhoundReducer);
                } else {
                    if (variant.stackRepresentation == StackRepresentation.Default) {
                        StackManager<StandardStackNode<HParseForest>, HParseForest> hStandardStackManager = new StandardStackManager<HParseForest>();
                        Reducer<StandardStackNode<HParseForest>, HParseForest, ParseNode, Derivation> hReducer = new Reducer<StandardStackNode<HParseForest>, HParseForest, ParseNode, Derivation>(parseTable, hStandardStackManager, hParseForestManager);
                        
                        return new Parser<StandardStackNode<HParseForest>, HParseForest, ParseNode, Derivation>(parseTable, hStandardStackManager, hParseForestManager, hReducer);
                    } else {
                        	StackManager<ElkhoundStackNode<HParseForest>, HParseForest> hElkhoundStackManager;
	                        
	                		if (variant.stackRepresentation == StackRepresentation.ElkhoundHybrid)
	                			hElkhoundStackManager = new HybridElkhoundStackManager<HParseForest>();
	                		else
	                			hElkhoundStackManager = new StandardElkhoundStackManager<HParseForest>();
	                		
                        Reducer<ElkhoundStackNode<HParseForest>, HParseForest, ParseNode, Derivation> hReducer = new Reducer<ElkhoundStackNode<HParseForest>, HParseForest, ParseNode, Derivation>(parseTable, hElkhoundStackManager, hParseForestManager);
                        
                        return new Parser<ElkhoundStackNode<HParseForest>, HParseForest, ParseNode, Derivation>(parseTable, hElkhoundStackManager, hParseForestManager, hReducer);
                    }
                }
        }
    }
    
    public static Parser<?, ?, ?, ?> getParser(IParseTable parseTable, ParseForestRepresentation parseForestRepresentation, StackRepresentation stackRepresentation, Reducing reducing) {
    		return getParser(parseTable, new Variant(parseForestRepresentation, stackRepresentation, reducing));
    }
    
    public static List<Parser<?, ?, ?, ?>> allParsers(IParseTable parseTable) {
        List<Parser<?, ?, ?, ?>> parsers = new ArrayList<Parser<?, ?, ?, ?>>();
        
        for (Variant variant : allVariants()) {
            Parser<?, ?, ?, ?> parser = getParser(parseTable, variant.parseForestRepresentation, variant.stackRepresentation, variant.reducing);
            
            parsers.add(parser);
        }
        
        return parsers;
    }
    
    public static JSGLR2<?, ?, IStrategoTerm> getJSGLR2(IParseTable parseTable, Variant variant) {
        IParser<?, ?> parser = getParser(parseTable, variant.parseForestRepresentation, variant.stackRepresentation, variant.reducing);
        IImploder<?, IStrategoTerm> imploder;
        
        switch (variant.parseForestRepresentation) {
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
    
    public static JSGLR2<?, ?, IStrategoTerm> getJSGLR2(IParseTable parseTable, ParseForestRepresentation parseForestRepresentation, StackRepresentation stackRepresentation, Reducing reducing) {
    		return getJSGLR2(parseTable, new Variant(parseForestRepresentation, stackRepresentation, reducing));
    }
    
    public static List<JSGLR2<?, ?, IStrategoTerm>> allJSGLR2(IParseTable parseTable) {
        List<JSGLR2<?, ?, IStrategoTerm>> jsglr2s = new ArrayList<JSGLR2<?, ?, IStrategoTerm>>();
        
        for (Variant variant : allVariants()) {
            JSGLR2<?, ?, IStrategoTerm> jsglr2 = getJSGLR2(parseTable, variant.parseForestRepresentation, variant.stackRepresentation, variant.reducing);
            
            jsglr2s.add(jsglr2);
        }
        
        return jsglr2s;
    }
    
}
