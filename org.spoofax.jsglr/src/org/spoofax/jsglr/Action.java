/*
 * Created on 04.des.2005
 *
 * Copyright (c) 2005, Karl Trygve Kalleberg <karltk@ii.uib.no>
 * 
 * Licensed under the GNU Lesser General Public License, v2.1
 */
package org.spoofax.jsglr;


public class Action {

    private Range[] ranges;

    private ActionItem[] items;

    public Action(Range[] ranges, ActionItem[] items) {
        this.ranges = ranges;
        this.items = items;
    }

    public ActionItem[] getActionItems() {
        return items;
    }

    public boolean accepts(int currentToken) {
        for (Range r : ranges)
            if (r.within(currentToken))
                return true;
        return false;
    }

    public boolean rejectable() {
        for(ActionItem ai : items) {
            if(ai instanceof Reduce) {
                Reduce r = (Reduce) ai;
                if(r.status == Reduce.REJECT)
                    return true;
            }
                
        }
        return false;
    }

    public boolean hasPrefer() {
        for(ActionItem ai : items)
            if(ai instanceof Reduce) {
                Reduce r = (Reduce) ai;
                if(r.status == Reduce.PREFER)
                    return true;
            }
        return false;
    }

    public boolean hasAvoid() {
        for(ActionItem ai : items)
            if(ai instanceof Reduce) {
                Reduce r = (Reduce) ai;
                if(r.status == Reduce.AVOID) {
                    Tools.debug(this);
                    return true;                    
                }

            }
        return false;
    }

    @Override
    public String toString() {
     return "action(" + ranges + "," + items + ")";
    }
}
