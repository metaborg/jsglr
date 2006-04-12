/*
 * Created on 30.mar.2006
 *
 * Copyright (c) 2005, Karl Trygve Kalleberg <karltk@ii.uib.no>
 * 
 * Licensed under the GNU General Public License, v2
 */
package org.spoofax.jsglr;

import java.util.List;
import java.util.Vector;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import aterm.ATerm;
import aterm.ATermFactory;
import aterm.ATermList;
import aterm.pure.PureFactory;

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
            List<ATerm> r = new Vector<ATerm>();
            for(IParseNode n : kids)
                r.add(n.toParseTree(pt));

            ATermList l1 = makeList((PureFactory)factory, r);
            ATerm appl = factory.makeAppl(pt.applAFun, pt.getProduction(label), l1);
            r.clear();
            return appl;
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

    /**
     * todo: stolen from TAFReader; move elsewhere
     */
    public static ATermList makeList(PureFactory factory, List<ATerm> terms) {
        ATermList result = factory.getEmpty();
        for (int i = terms.size() - 1; i >= 0; i--) {
            result = factory.makeList(terms.get(i), result);
        }
        return result;
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
