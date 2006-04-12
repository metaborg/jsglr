/*
 * Created on 04.des.2005
 *
 * Copyright (c) 2005, Karl Trygve Kalleberg <karltk@ii.uib.no>
 * 
 * Licensed under the GNU Lesser General Public License, v2.1
 */
package org.spoofax.jsglr;

import java.io.Serializable;


public class ActionState implements Serializable {
    static final long serialVersionUID = 2059615768756265051L;

    public final Frame st;
    public final State s;

    public ActionState(Frame st, State s) {
        this.st = st;
        this.s = s;
    }
}
