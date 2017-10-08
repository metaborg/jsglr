package org.spoofax.jsglr2.parseforest.basic;

import org.spoofax.jsglr2.parseforest.ParseForestManager;
import org.spoofax.jsglr2.parser.Parse;
import org.spoofax.jsglr2.parsetable.IProduction;
import org.spoofax.jsglr2.parsetable.ProductionType;

public class BasicParseForestManager extends ParseForestManager<BasicParseForest, SymbolNode, RuleNode> {

    public SymbolNode createParseNode(Parse<?, BasicParseForest> parse, IProduction production, RuleNode firstDerivation) {
        SymbolNode symbolNode = new SymbolNode(parse.parseNodeCount++, parse, firstDerivation.startPosition, firstDerivation.endPosition, production);
        
        parse.notify(observer -> observer.createParseNode(symbolNode, production));
        
        addDerivation(parse, symbolNode, firstDerivation);
                
        return symbolNode;
    }
    
    public RuleNode createDerivation(Parse<?, BasicParseForest> parse, IProduction production, ProductionType productionType, BasicParseForest[] parseForests) {
        Cover cover = getCover(parse, parseForests);
        
        RuleNode ruleNode = new RuleNode(parse.parseNodeCount++, parse, cover.startPosition, cover.endPosition, production, productionType, parseForests);
        
        parse.notify(observer -> observer.createDerivation(ruleNode.nodeNumber, production, parseForests));
                
        return ruleNode;
    }
    
    public void addDerivation(Parse<?, BasicParseForest> parse, SymbolNode symbolNode, RuleNode ruleNode) {
        parse.notify(observer -> observer.addDerivation(symbolNode));
        
        symbolNode.addDerivation(ruleNode);
    }
    
    public TermNode createCharacterNode(Parse<?, BasicParseForest> parse) {
        TermNode termNode = new TermNode(parse.parseNodeCount++, parse, parse.currentPosition(), parse.currentChar);
        
        parse.notify(observer -> observer.createCharacterNode(termNode, termNode.character));
        
        return termNode;
    }
    
    public BasicParseForest[] parseForestsArray(int length) {
    		return new BasicParseForest[length];
    }
   
}
