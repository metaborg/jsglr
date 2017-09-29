package org.spoofax.jsglr2.parseforest.symbolrule;

import java.util.List;

import org.spoofax.jsglr2.parseforest.ParseForestManager;
import org.spoofax.jsglr2.parser.Parse;
import org.spoofax.jsglr2.parsetable.IProduction;
import org.spoofax.jsglr2.parsetable.ProductionType;

public class SRParseForestManager extends ParseForestManager<SRParseForest, SymbolNode, RuleNode> {

    public SymbolNode createParseNode(Parse<?, SRParseForest> parse, IProduction production, RuleNode firstDerivation) {
        SymbolNode symbolNode = new SymbolNode(parse.parseNodeCount++, parse, firstDerivation.startPosition, firstDerivation.endPosition, production);
        
        parse.notify(observer -> observer.createParseNode(symbolNode, production));
        
        addDerivation(parse, symbolNode, firstDerivation);
                
        return symbolNode;
    }
    
    public RuleNode createDerivation(Parse<?, SRParseForest> parse, IProduction production, ProductionType productionType, List<SRParseForest> parseForests) {
        SRParseForest[] parseForestArray = parseForests.toArray(new SRParseForest[parseForests.size()]);
        
        Cover cover = getCover(parse, parseForestArray);
        
        RuleNode ruleNode = new RuleNode(parse.parseNodeCount++, parse, cover.startPosition, cover.endPosition, production, productionType, parseForestArray);
        
        parse.notify(observer -> observer.createDerivation(parseForestArray));
                
        return ruleNode;
    }
    
    public void addDerivation(Parse<?, SRParseForest> parse, SymbolNode symbolNode, RuleNode ruleNode) {
        parse.notify(observer -> observer.addDerivation(symbolNode));
        
        symbolNode.addDerivation(ruleNode);
    }
    
    public TermNode createCharacterNode(Parse<?, SRParseForest> parse) {
        TermNode termNode = new TermNode(parse.parseNodeCount++, parse, parse.currentPosition(), parse.currentChar);
        
        parse.notify(observer -> observer.createCharacterNode(termNode, termNode.character));
        
        return termNode;
    }
   
}
