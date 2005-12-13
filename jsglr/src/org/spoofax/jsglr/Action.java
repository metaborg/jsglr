/*
 * Created on 04.des.2005
 *
 * Copyright (c) 2005, Karl Trygve Kalleberg <karltk@ii.uib.no>
 * 
 * Licensed under the GNU General Public License, v2
 */
package org.spoofax.jsglr;

import java.util.List;

public class Action {

    // FIXME: Must go in ActionItem
    public final int type;
    
    public static final int SHIFT = 1;
    public static final int REDUCE = 2;
    public static final int ACCEPT = 3;

    private List<Range> ranges;
    private List<ActionItem> items;

    public Action(int type) {
        this.type = type;
    }

    public Action(List<Range> ranges, List<ActionItem> items) {
        // FIXME: Not correct
        type = 0;
        this.ranges = ranges;
        this.items = items;
    }

    // TODO: Should this be List<Production> ?
    public Production getProduction() {
        // TODO Auto-generated method stub
        return null;
    }

}
