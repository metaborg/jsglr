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

    public final int type;

    public final static int REJECT = 1;
    
    public Production(int type) {
        this.type = type;
    }
    public int getArity() {
        // TODO Auto-generated method stub
        return 0;
    }

    public ATerm apply(List<ATerm> kids) {
        // TODO Auto-generated method stub
        return null;
    }

}
