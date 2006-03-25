/*
 * Created on 05.des.2005
 *
 * Copyright (c) 2005, Karl Trygve Kalleberg <karltk@ii.uib.no>
 * 
 * Licensed under the GNU Lesser General Public License, v2.1
 */
package org.spoofax.jsglr;


public class Range {

    public final int low;
    public final int high;
    
    public Range(int low, int high) throws InvalidParseTableException {
        
        if(low < 0 || high > 256) 
            throw new InvalidParseTableException("Invalid ranges ([" + low + " - " + high + "])");

        this.low = low;
        this.high = high;
    }
    
    public Range(int n) {
        low = high = n;
    }

    boolean within(int c) {
        return c >= low && c <= high;
    }
    
    @Override
    public String toString() {
        if(low == high)
            return "" +low;
        return "[" + low + "," + high + "]";
    }
}
