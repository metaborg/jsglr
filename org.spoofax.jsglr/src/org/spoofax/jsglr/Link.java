/*
 * Created on 04.des.2005
 *
 * Copyright (c) 2005, Karl Trygve Kalleberg <karltk@ii.uib.no>
 * 
 * Licensed under the GNU Lesser General Public License, v2.1
 */
package org.spoofax.jsglr;

import aterm.ATerm;

public class Link {

    public final Frame parent;

    public ATerm label;

    private boolean rejected;

    public Link(Frame destination, ATerm t) {
        this.parent = destination;
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

    public String toString() {
        return "" + parent.state.stateNumber;
    }
}