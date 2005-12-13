/*
 * Created on 05.des.2005
 *
 * Copyright (c) 2005, Karl Trygve Kalleberg <karltk@ii.uib.no>
 * 
 * Licensed under the GNU Lesser General Public License, v2.1
 */
package org.spoofax.jsglr;

import java.util.List;

public class Goto {

    // FIXME: Curiously, goto is on labels, not tokens.
    // private List<Range> ranges;
    private List<Integer> productionRefs;
    public final int nextState;
    
    public Goto(List<Range> ranges, List<Integer> productionRefs, int nextState) {
        this.nextState = nextState;
        // this.ranges = ranges;
        this.productionRefs = productionRefs;
    }

    public boolean hasProd(int label) {
        for(Integer i : productionRefs)
            if(i == label)
                return true;
        return false;
    }
}
