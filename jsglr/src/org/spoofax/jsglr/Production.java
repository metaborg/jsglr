/*
 * Created on 04.des.2005
 *
 * Copyright (c) 2005, Karl Trygve Kalleberg <karltk@ii.uib.no>
 * 
 * Licensed under the GNU General Public License, v2
 */
package org.spoofax.jsglr;

import java.util.List;

import aterm.ATerm;

public class Production {

    public final int arity;
    public final int label;
    public final int status;
    
    // FIXME: These are most certainly wrong. Must check SGLR parse table
    // definition to get the real numbers otherwise, all heck is loose.
    public static final int PREFER = 1;
    public static final int AVOID = 2; 
    public static final int REJECT = 3;
    public static final int NORMAL = 4;
    
    public Production(int arity, int label, int status) {
        this.arity = arity;
        this.label = label;
        this.status = status;
    }

    public ATerm apply(List<ATerm> kids) {
        // TODO Auto-generated method stub
        return null;
    }

}
