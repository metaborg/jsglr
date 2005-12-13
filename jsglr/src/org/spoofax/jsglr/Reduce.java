/*
 * Created on 05.des.2005
 *
 * Copyright (c) 2005, Karl Trygve Kalleberg <karltk@ii.uib.no>
 * 
 * Licensed under the GNU General Public License, v2
 */
package org.spoofax.jsglr;

public class Reduce extends ActionItem {

    public final int arity;

    public final int label;

    public final int status;

    public final Production production;
    
    // FIXME: Must check that these are correct
    public static final int NORMAL = 0;
    public static final int PREFER = 1;
    public static final int AVOID = 2;
    public static final int REJECT = 3;
    
    public Reduce(int arity, int label, int status) {
        
        super(REDUCE);
        
        this.arity = arity;
        this.label = label;
        this.status = status;
      
        production = new Production(arity, label, status);
    }
    
    public String toString() {
        return "reduce(" + arity + ", " + label + ", " + status + ")";
    }
}
