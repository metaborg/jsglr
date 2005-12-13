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

    private List<Range> ranges;

    private List<ActionItem> items;

    public Action(List<Range> ranges, List<ActionItem> items) {
        this.ranges = ranges;
        this.items = items;
    }

    // TODO: Should this be List<Production> ?
    public Production getProduction() {
        // TODO Auto-generated method stub
        return null;
    }

    public List<ActionItem> getActionItems() {
        return items;
    }

    public boolean accepts(int currentToken) {
        for (Range r : ranges)
            if (r.within(currentToken))
                return true;
        return false;
    }

}
