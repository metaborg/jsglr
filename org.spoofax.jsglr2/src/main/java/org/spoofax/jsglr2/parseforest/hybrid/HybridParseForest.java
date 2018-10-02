package org.spoofax.jsglr2.parseforest.hybrid;

import org.spoofax.jsglr2.parseforest.AbstractParseForest;
import org.spoofax.jsglr2.parser.Parse;
import org.spoofax.jsglr2.parser.Position;
import org.spoofax.jsglr2.util.TreePrettyPrinter;

public abstract class HybridParseForest extends AbstractParseForest {

    protected HybridParseForest(int nodeNumber, Parse<?, ?> parse, Position startPosition, Position endPosition) {
        super(nodeNumber, parse, startPosition, endPosition);
    }
    
    @Override
    public String toString() {
    	TreePrettyPrinter printer = new TreePrettyPrinter();
    	
    	prettyPrint(printer);
    	
    	return printer.get();
    }
    
    abstract protected void prettyPrint(TreePrettyPrinter printer);

}
