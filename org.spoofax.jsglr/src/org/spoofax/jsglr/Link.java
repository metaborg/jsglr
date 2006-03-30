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

    public IParseNode label;

    private boolean rejected;

    public Link(Frame destination, IParseNode t) {
        this.parent = destination;
        label = t;
        rejected = false;
    }

    public void addAmbiguity(IParseNode t) {
        label = new Amb(label);
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