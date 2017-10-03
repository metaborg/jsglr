package org.spoofax.jsglr2.parseforest.hybrid;

import org.spoofax.jsglr2.parseforest.ParseForestManager;
import org.spoofax.jsglr2.parser.Parse;
import org.spoofax.jsglr2.parsetable.IProduction;
import org.spoofax.jsglr2.parsetable.ProductionType;

public class HybridParseForestManager extends ParseForestManager<HybridParseForest, ParseNode, Derivation> {

    public ParseNode createParseNode(Parse<?, HybridParseForest> parse, IProduction production, Derivation firstDerivation) {
        Cover cover = getCover(parse, firstDerivation.parseForests);

        ParseNode parseNode = new ParseNode(parse.parseNodeCount++, parse, cover.startPosition, cover.endPosition, production, firstDerivation);
        
        parse.notify(observer -> observer.createParseNode(parseNode, production));
        parse.notify(observer -> observer.addDerivation(parseNode));
                
        return parseNode;
    }
    
    public Derivation createDerivation(Parse<?, HybridParseForest> parse, IProduction production, ProductionType productionType, HybridParseForest[] parseForests) {
        Derivation derivation = new Derivation(production, productionType, parseForests);
        
        parse.notify(observer -> observer.createDerivation(parseForests));
                
        return derivation;
    }
    
    public void addDerivation(Parse<?, HybridParseForest> parse, ParseNode parseNode, Derivation derivation) {
        parse.notify(observer -> observer.addDerivation(parseNode));
        
        parseNode.addDerivation(derivation);
    }
    
    public CharacterNode createCharacterNode(Parse<?, HybridParseForest> parse) {
        CharacterNode characterNode = new CharacterNode(parse.parseNodeCount++, parse, parse.currentPosition(), parse.currentChar);
        
        parse.notify(observer -> observer.createCharacterNode(characterNode, characterNode.character));
        
        return characterNode;
    }
    
    public HybridParseForest[] parseForestsArray(int length) {
    		return new HybridParseForest[length];
    }
   
}
