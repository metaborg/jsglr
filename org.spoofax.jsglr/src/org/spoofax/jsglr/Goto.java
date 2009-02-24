/*
 * Created on 05.des.2005
 *
 * Copyright (c) 2005, Karl Trygve Kalleberg <karltk near strategoxt.org>
 * 
 * Licensed under the GNU Lesser General Public License, v2.1
 */
package org.spoofax.jsglr;

import java.io.Serializable;
import java.util.Arrays;

public class Goto implements Serializable {

    static final long serialVersionUID = 4361136767191244085L;
    
    private final Range[] ranges;
    public final int nextState;
    
    public Goto(Range[] ranges, int nextState) {
        this.ranges = ranges;
        this.nextState = nextState;
    }

    public boolean hasProd(int label) {
        for (Range r : ranges) {
            if(r.within(label)) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof Goto))
            return false;
        Goto o = (Goto)obj;
        if(nextState != o.nextState)
            return false;
        if(ranges.length != o.ranges.length)
            return false;
        return Arrays.deepEquals(ranges, o.ranges);
    }
    
    @Override
    public int hashCode() {
        return Arrays.deepHashCode(ranges);
    }
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("goto([");
        for(int i = 0;i < ranges.length;i++) {
            sb.append(ranges[i].toString());
            if(i < ranges.length - 1) 
                sb.append(",");
        }
        sb.append("], ");
        sb.append(nextState);
        sb.append(")");
        return sb.toString(); 
    }
}
