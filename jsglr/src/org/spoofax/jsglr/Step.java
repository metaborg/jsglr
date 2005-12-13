/*
 * Created on 04.des.2005
 *
 * Copyright (c) 2005, Karl Trygve Kalleberg <karltk@ii.uib.no>
 * 
 * Licensed under the GNU General Public License, v2
 */
package org.spoofax.jsglr;

import aterm.ATerm;

public class Step {

    public final ATerm label;
    
    public Step(ATerm t) {
       label = t;
    }

    public void addAmbiguity(ATerm t) {
        // TODO Auto-generated method stub
        
    }

    public void reject() {
        // TODO Auto-generated method stub
        
    }

}
