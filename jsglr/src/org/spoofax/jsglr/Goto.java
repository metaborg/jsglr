/*
 * Created on 05.des.2005
 *
 * Copyright (c) 2005, Karl Trygve Kalleberg <karltk@ii.uib.no>
 * 
 * Licensed under the GNU General Public License, v2
 */
package org.spoofax.jsglr;

import java.util.List;

public class Goto {

    private List<Range> ranges;
    private int nextState;
    
    public Goto(List<Range> ranges, int nextState) {
        this.nextState = nextState;
        this.ranges = ranges;
    }

}
