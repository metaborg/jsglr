/*
 * Created on 05.des.2005
 *
 * Copyright (c) 2005, Karl Trygve Kalleberg <karltk@ii.uib.no>
 * 
 * Licensed under the GNU Lesser General Public License, v2.1
 */
package org.spoofax.jsglr;

public class Goto {

    // FIXME: Curiously, goto is on labels, not tokens.
    // private List<Range> ranges;
    private int[] productionRefs;
    public final int nextState;
    
    public Goto(Range[] ranges, int[] productionRefs, int nextState) {
        this.nextState = nextState;
        // this.ranges = ranges;
        this.productionRefs = productionRefs;
    }

    public boolean hasProd(int label) {
        for(int i=0; i < productionRefs.length; i++)
            if(productionRefs[i] == label)
                return true;
        return false;
    }
    
    @Override
    public String toString() {
        return "goto(" + productionRefs + "," + nextState + ")"; 
    }
}
