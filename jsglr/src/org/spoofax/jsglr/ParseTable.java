/*
 * Created on 04.des.2005
 *
 * Copyright (c) 2005, Karl Trygve Kalleberg <karltk@ii.uib.no>
 * 
 * Licensed under the GNU General Public License, v2
 */
package org.spoofax.jsglr;

import aterm.ATerm;
import aterm.ATermAppl;
import aterm.ATermList;


public class ParseTable {

    public ParseTable(ATerm pt) {
        parse(pt);
    }

    private boolean parse(ATerm pt) {
        int version = Term.intAt(pt, 0);
        int startSymbol = Term.intAt(pt, 1);
        ATermList labels = Term.listAt(pt, 2);
        ATermAppl states = Term.applAt(pt, 3);
        ATermAppl priorities = Term.applAt(pt, 4);
        
        if(version != 4) {
            return false;
        }
        
        return true;
    }

    public State getInitialState() {
        return null;
    }
    
    

}
