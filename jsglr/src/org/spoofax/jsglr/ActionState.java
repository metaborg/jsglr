/*
 * Created on 04.des.2005
 *
 * Copyright (c) 2005, Karl Trygve Kalleberg <karltk@ii.uib.no>
 * 
 * Licensed under the GNU General Public License, v2
 */
package org.spoofax.jsglr;


public class ActionState {

    public final Frame st;
    public final State s;
    
    public ActionState(Frame st, State s) {
        this.st = st;
        this.s = s;
    }
}
