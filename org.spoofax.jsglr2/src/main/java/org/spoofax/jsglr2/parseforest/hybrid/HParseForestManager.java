package org.spoofax.jsglr2.parseforest.hybrid;

import java.util.List;

import org.spoofax.jsglr2.parseforest.ParseForestManager;
import org.spoofax.jsglr2.parser.Parse;
import org.spoofax.jsglr2.parsetable.IProduction;
import org.spoofax.jsglr2.parsetable.ProductionType;

public class HParseForestManager extends ParseForestManager<HParseForest, ParseNode, Derivation> {

    public ParseNode createParseNode(Parse<?, HParseForest> parse, IProduction production, Derivation firstDerivation) {
        Cover cover = getCover(parse, firstDerivation.parseForests);

        ParseNode parseNode = new ParseNode(parse.parseNodeCount++, parse, cover.startPosition, cover.endPosition, production, firstDerivation);
        
        parse.notify(observer -> observer.createParseNode(parseNode, production));
        parse.notify(observer -> observer.addDerivation(parseNode));
                
        return parseNode;
    }
    
    public Derivation createDerivation(Parse<?, HParseForest> parse, IProduction production, ProductionType productionType, List<HParseForest> parseForests) {
        HParseForest[] parseForestsArray = parseForests.toArray(new HParseForest[parseForests.size()]);
        
        Derivation derivation = new Derivation(production, productionType, parseForestsArray);
        
        parse.notify(observer -> observer.createDerivation(parseForestsArray));
                
        return derivation;
    }
    
    public void addDerivation(Parse<?, HParseForest> parse, ParseNode parseNode, Derivation derivation) {
        parse.notify(observer -> observer.addDerivation(parseNode));
        
        parseNode.addDerivation(derivation);
    }
    
    public CharacterNode createCharacterNode(Parse<?, HParseForest> parse) {
        CharacterNode characterNode = new CharacterNode(parse.parseNodeCount++, parse, parse.currentPosition(), parse.currentChar);
        
        parse.notify(observer -> observer.createCharacterNode(characterNode, characterNode.character));
        
        return characterNode;
    }
   
}
