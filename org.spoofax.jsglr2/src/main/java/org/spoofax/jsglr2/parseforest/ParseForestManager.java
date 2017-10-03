package org.spoofax.jsglr2.parseforest;

import org.spoofax.jsglr2.parser.Parse;
import org.spoofax.jsglr2.parser.Position;
import org.spoofax.jsglr2.parsetable.IProduction;
import org.spoofax.jsglr2.parsetable.ProductionType;

public abstract class ParseForestManager<ParseForest extends AbstractParseForest, ParseNode extends ParseForest, Derivation> {

    abstract public ParseForest createParseNode(Parse<?, ParseForest> parse, IProduction production, Derivation firstDerivation);
    
    abstract public Derivation createDerivation(Parse<?, ParseForest> parse, IProduction production, ProductionType productionType, ParseForest[] parseForests);
    
    abstract public void addDerivation(Parse<?, ParseForest> parse, ParseNode parseNode, Derivation derivation);
    
    abstract public ParseForest createCharacterNode(Parse<?, ParseForest> parse);

    abstract public ParseForest[] parseForestsArray(int length);
    
    protected Cover getCover(Parse<?, ParseForest> parse, ParseForest[] parseNodes) {
        Position startPosition, endPosition;
        
        if (parseNodes.length == 0)
            startPosition = endPosition = parse.currentPosition();
        else {
            ParseForest firstParseNode = parseNodes[0];
            ParseForest lastParseNode = parseNodes[parseNodes.length - 1];

            startPosition = firstParseNode.startPosition;
            endPosition = lastParseNode.endPosition;
        }
        
        return new Cover(startPosition, endPosition);
    }
    
    // Represents the part of the input that a parse node covers
    protected class Cover {
        public Position startPosition, endPosition;
        
        public Cover(Position startPosition, Position endPosition) {
            this.startPosition = startPosition;
            this.endPosition = endPosition;
        }
    }
    
}
