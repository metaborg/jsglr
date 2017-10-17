package org.spoofax.jsglr2.parseforest;

import org.spoofax.jsglr2.parser.Position;

public class Cover {
	
    public Position startPosition, endPosition;
    
    public Cover(Position startPosition, Position endPosition) {
        this.startPosition = startPosition;
        this.endPosition = endPosition;
    }
    
}
