/*
 * Created on 05.des.2005
 *
 * Copyright (c) 2005, Karl Trygve Kalleberg <karltk@ii.uib.no>
 * 
 * Licensed under the GNU Lesser General Public License, v2.1
 */
package org.spoofax.jsglr;

public abstract class ActionItem {

    public final int type;
    
    public static final int REDUCE = 1;
    public static final int SHIFT = 2;
    public static final int ACCEPT = 3;
    
    public ActionItem(int type) {
        this.type = type;
    }

}
