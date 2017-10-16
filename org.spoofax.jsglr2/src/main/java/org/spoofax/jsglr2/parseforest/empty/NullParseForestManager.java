package org.spoofax.jsglr2.parseforest.empty;

import org.spoofax.jsglr2.parseforest.ParseForestManager;
import org.spoofax.jsglr2.parseforest.hybrid.CharacterNode;
import org.spoofax.jsglr2.parseforest.hybrid.Derivation;
import org.spoofax.jsglr2.parseforest.hybrid.HybridParseForest;
import org.spoofax.jsglr2.parseforest.hybrid.ParseNode;
import org.spoofax.jsglr2.parser.Parse;
import org.spoofax.jsglr2.parsetable.IProduction;
import org.spoofax.jsglr2.parsetable.ProductionType;

public class NullParseForestManager extends ParseForestManager<HybridParseForest, ParseNode, Derivation> {

    public ParseNode createParseNode(Parse<?, HybridParseForest> parse, IProduction production, Derivation firstDerivation) {
        return null;
    }
    
    public Derivation createDerivation(Parse<?, HybridParseForest> parse, IProduction production, ProductionType productionType, HybridParseForest[] parseForests) {
        return null;
    }
    
    public void addDerivation(Parse<?, HybridParseForest> parse, ParseNode parseNode, Derivation derivation) {}
    
    public CharacterNode createCharacterNode(Parse<?, HybridParseForest> parse) {
        return null;
    }
    
    public HybridParseForest[] parseForestsArray(int length) {
    		return null;
    }
   
}
