/*
 * Created on 30.mar.2006
 *
 * Copyright (c) 2005, Karl Trygve Kalleberg <karltk@ii.uib.no>
 * 
 * Licensed under the GNU General Public License, v2
 */
package org.spoofax.jsglr;

import java.util.LinkedList;
import java.util.List;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import aterm.ATerm;
import aterm.ATermFactory;

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
    
    public boolean isLiteral() {
        return false;
    }
    
    public ATerm toParseTree(ParseTable pt) {
    	ATermFactory factory = pt.getFactory();
    	if(type == Production.NORMAL) {
    		List<ATerm> r = new LinkedList<ATerm>();
    		for(IParseNode n : kids)
    			r.add(n.toParseTree(pt));
    		
    		return factory.parse("appl(" + pt.getProduction(label) + "," + r + ")");
    	} else if (type == Production.AVOID) {
            // FIXME Extremely temporary hack: never use sun.*
    	    throw new NotImplementedException();
        } else if (type == Production.PREFER) {
            // FIXME Extremely temporary hack: never use sun.*
            throw new NotImplementedException();
        } else if (type == Production.REJECT) {
            // FIXME Extremely temporary hack: never use sun.*
            throw new NotImplementedException();
        }
        
    	return null;
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
