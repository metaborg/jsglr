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

    public final Frame destination;
    public ATerm label;
    private boolean rejected;
    
    public Step(Frame destination, ATerm t) {
        this.destination = destination;
        label = t;
        rejected = false;
    }

    public void addAmbiguity(ATerm t) {
        // FIXME: Speed up
        label = label.getFactory().parse("amb(" + t + "," + label + ")");
    }

    public void reject() {
        rejected = true;
    }
    
    public boolean isRejected() {
        return rejected;
    }

}
