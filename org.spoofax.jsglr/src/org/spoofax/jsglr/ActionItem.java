/*
 * Created on 05.des.2005
 *
 * Copyright (c) 2005, Karl Trygve Kalleberg <karltk@ii.uib.no>
 * 
 * Licensed under the GNU Lesser General Public License, v2.1
 */
package org.spoofax.jsglr;

import java.io.Serializable;

public abstract class ActionItem implements Serializable {

    static final long serialVersionUID = -340934714889427356L;
    
    public final int type;

    // FIXME do we need these? instanceof is too slow?
    public static final int REDUCE = 1;

    public static final int SHIFT = 2;

    public static final int ACCEPT = 3;

    public static final int REDUCE_LOOKAHEAD = 4;
    
    public ActionItem(int type) {
        this.type = type;
    }

    @Override
    public String toString() {
        if (type == REDUCE)
            return "reduce";
        else if (type == SHIFT)
            return "shift";
        else if (type == ACCEPT)
            return "accept";
        else if (type == REDUCE_LOOKAHEAD)
            return "reduce_la";
        return null;
    }
}
