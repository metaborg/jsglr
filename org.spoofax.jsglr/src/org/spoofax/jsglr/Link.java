/*
 * Created on 04.des.2005
 *
 * Copyright (c) 2005, Karl Trygve Kalleberg <karltk@ii.uib.no>
 * 
 * Licensed under the GNU Lesser General Public License, v2.1
 */
package org.spoofax.jsglr;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;


public class Link {

    public Frame parent;

    protected IParseNode label;

    private boolean rejected;
    
    private final int length;
    

    public Link(Frame destination, IParseNode t, int length) {
        this.parent = destination;
        label = t;
        rejected = false;
        this.length = length;
    }

    public void reject() {
        rejected = true;
    }

    public boolean isRejected() {
        return rejected;
    }

    public int getLabelNumber() {
        throw new NotImplementedException();
    }
    
    public String toString() {
        return "" + parent.state.stateNumber;
    }

    public int getLength() {
        return length;
    }

    public void clear() {
        if(parent != null) {
            parent = null;//todo.clear();
        }
        this.label = null;
    }
}