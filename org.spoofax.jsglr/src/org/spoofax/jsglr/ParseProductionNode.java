/*
 * Created on 30.mar.2006
 *
 * Copyright (c) 2005, Karl Trygve Kalleberg <karltk@ii.uib.no>
 * 
 * Licensed under the GNU General Public License, v2
 */
package org.spoofax.jsglr;

import aterm.ATerm;

public class ParseProductionNode extends IParseNode {

    public final int prod;
    
    ParseProductionNode(int prod) {
        this.prod = prod;
    }
    
    public ATerm toParseTree(ParseTable pt) {
    	return pt.getProduction(prod);
    }
    @Override
    public String toString() {
        return "aprod(" + prod + ")";
    }
    
    public int getProduction() { return prod; }
}
