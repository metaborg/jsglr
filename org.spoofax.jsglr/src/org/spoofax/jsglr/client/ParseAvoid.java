/*
 * Created on 17.apr.2006
 *
 * Copyright (c) 2005, Karl Trygve Kalleberg <karltk near strategoxt.org>
 *
 * Licensed under the GNU General Public License, v2
 */
package org.spoofax.jsglr.client;


public class ParseAvoid extends ParseNode {

    public ParseAvoid(int label, AbstractParseNode[] kids) {
        super(label, kids);
    }

    @Override
    public String toString() {
        return "avoid(aprod(" + label + "), " + kids + ")";
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof ParseAvoid))
            return false;
        return super.equals(obj);
    }
}
