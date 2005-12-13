/*
 * Created on 04.des.2005
 *
 * Copyright (c) 2005, Karl Trygve Kalleberg <karltk@ii.uib.no>
 * 
 * Licensed under the GNU General Public License, v2
 */
package org.spoofax.jsglr;

import java.util.List;

public class State {

    private final int stateNumber;
    private final List<Goto> gotos;
    private final List<Action> actions;
    
    public State(int stateNumber, List<Goto> gotos, List<Action> actions) {
        this.stateNumber = stateNumber;
        this.gotos = gotos;
        this.actions = actions;
    }

    public List<Action> getActions(int currentToken) {
        // TODO Auto-generated method stub
        return null;
    }

    public State go(Production prod) {
        // TODO Auto-generated method stub
        return null;
    }

}
