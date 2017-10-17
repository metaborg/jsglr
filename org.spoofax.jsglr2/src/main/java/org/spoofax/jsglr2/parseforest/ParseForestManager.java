package org.spoofax.jsglr2.parseforest;

import org.spoofax.jsglr2.parser.Parse;
import org.spoofax.jsglr2.parser.Position;
import org.spoofax.jsglr2.parsetable.IProduction;
import org.spoofax.jsglr2.parsetable.ProductionType;

public abstract class ParseForestManager<ParseForest extends AbstractParseForest, ParseNode extends ParseForest, Derivation> {

    abstract public ParseNode createParseNode(Parse<?, ParseForest> parse, Position beginPosition, IProduction production, Derivation firstDerivation);
    
    abstract public Derivation createDerivation(Parse<?, ParseForest> parse, Position beginPosition, IProduction production, ProductionType productionType, ParseForest[] parseForests);
    
    abstract public void addDerivation(Parse<?, ParseForest> parse, ParseNode parseNode, Derivation derivation);
    
    abstract public ParseForest createCharacterNode(Parse<?, ParseForest> parse);

    abstract public ParseForest[] parseForestsArray(int length);
    
    abstract public ParseForest filterStartSymbol(ParseForest parseForest, String startSymbol);
    
}
