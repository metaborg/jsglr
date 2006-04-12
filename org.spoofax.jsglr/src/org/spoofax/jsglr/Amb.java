/*
 * Created on 30.mar.2006
 *
 * Copyright (c) 2005, Karl Trygve Kalleberg <karltk@ii.uib.no>
 * 
 * Licensed under the GNU General Public License, v2
 */
package org.spoofax.jsglr;

import aterm.ATerm;

public class Amb implements IParseNode {

    public final IParseNode left, right;
    
    Amb(IParseNode left, IParseNode right) {
        this.left = left;
        this.right = right;
    }
    
    public boolean isLiteral() {
        return false;
    }
    
    public ATerm toParseTree(ParseTable pt) {
        return pt.getFactory().makeAppl(pt.ambAFun, left.toParseTree(pt), right.toParseTree(pt));
    }
    
    @Override
    public String toString() {
        return "amb(" + left + "," + right + ")";
    }

    public boolean hasAmbiguity(IParseNode newNode) {
        boolean found = false;
        
        if(left instanceof Amb) {
            found = found && ((Amb)left).hasAmbiguity(newNode);
        }
        
        if(right instanceof Amb) {
            found = found && ((Amb)right).hasAmbiguity(newNode);
        }
        
        return found || (left == newNode) || (right == newNode); 
    }
}
