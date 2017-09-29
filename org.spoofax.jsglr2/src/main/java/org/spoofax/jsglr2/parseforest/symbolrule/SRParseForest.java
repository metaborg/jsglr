package org.spoofax.jsglr2.parseforest.symbolrule;

import org.spoofax.jsglr2.parseforest.AbstractParseForest;
import org.spoofax.jsglr2.parser.Parse;
import org.spoofax.jsglr2.parser.Position;

public abstract class SRParseForest extends AbstractParseForest {
	
	protected SRParseForest(int nodeNumber, Parse<?, AbstractParseForest> parse, Position startPosition, Position endPosition) {
		super(nodeNumber, parse, startPosition, endPosition);
	}
	
}
