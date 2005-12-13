/*
 * Created on 05.des.2005
 *
 * Copyright (c) 2005, Karl Trygve Kalleberg <karltk@ii.uib.no>
 * 
 * Licensed under the GNU General Public License, v2
 */
package org.spoofax.jsglr;

public class Reduce extends ActionItem {

    public final int arity;

    public final int label;

    public final int status;

    public Reduce(int arity, int label, int status) {
        
        super(REDUCE);
        
        this.arity = arity;
        this.label = label;
        this.status = status;
    }

}
