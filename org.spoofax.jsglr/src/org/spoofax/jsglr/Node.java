/*
 * Created on 30.mar.2006
 *
 * Copyright (c) 2005, Karl Trygve Kalleberg <karltk@ii.uib.no>
 * 
 * Licensed under the GNU General Public License, v2
 */
package org.spoofax.jsglr;

import java.util.List;

public class Node implements IParseNode {

    public final int label;
    public final int type;
    protected List<IParseNode> kids;
    
    private Node() { label = 0; type = 0; }
    
    public Node(int type, int label, List<IParseNode> kids) {
        this.type = type;
        this.label = label;
        this.kids = kids;
    }
    
    @Override
    public String toString() {
        String s;
        if(type == Production.NORMAL) {
            s = "regular";
        } else if (type == Production.AVOID) {
            s = "avoid";
        } else if (type == Production.REJECT) {
            s = "reject";
        } else if (type == Production.PREFER) {
            s = "prefer";
        } else {
           return null;
        }
        return s + "(" + label + "," + kids + ")";
    }
}
