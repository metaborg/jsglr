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

import aterm.ATerm;
import aterm.ATermFactory;
import aterm.ATermList;
import aterm.pure.PureFactory;

public class Node extends IParseNode {

    public final int label;
    protected List<IParseNode> kids;
    
    private Node() { label = 0; }
    
    public Node(int label, List<IParseNode> kids) {
        this.label = label;
        this.kids = kids;
    }
    
    public ATerm toParseTree(ParseTable pt) {
        ATermFactory factory = pt.getFactory();

        List<ATerm> r = new Vector<ATerm>();
        for(IParseNode n : kids)
            r.add(n.toParseTree(pt));

        ATermList l1 = makeList((PureFactory)factory, r);
        ATerm appl = factory.makeAppl(pt.applAFun, pt.getProduction(label), l1);
        r.clear();
        return appl;
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
        return "regular(" + label + "," + kids + ")";
    }
    
    public int getLabel() { return label; }
    public List<IParseNode> getKids() { return kids; }
}
