/*
 * Created on 05.des.2005
 *
 * Copyright (c) 2005, Karl Trygve Kalleberg <karltk@ii.uib.no>
 * 
 * Licensed under the GNU General Public License, v2
 */
package org.spoofax.jsglr;

import aterm.ATermInt;

public class Range {

    public final int low;
    public final int hi;
    
    public Range(int low, int hi) {
        this.low = low;
        this.hi = hi;
    }
    
    public Range(int n) {
        low = hi = n;
    }

    boolean within(int c) {
        return c >= low && c <= hi;
    }
}
