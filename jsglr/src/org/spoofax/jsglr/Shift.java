/*
 * Created on 06.des.2005
 *
 * Copyright (c) 2005, Karl Trygve Kalleberg <karltk@ii.uib.no>
 * 
 * Licensed under the GNU General Public License, v2
 */
package org.spoofax.jsglr;

public class Shift extends ActionItem {

    public final int nextState;
    
    public Shift(int nextState) {
        super(SHIFT);
        this.nextState = nextState;
    }

    public String toString() {
        return "shift(" + nextState + ")";
    }
}
