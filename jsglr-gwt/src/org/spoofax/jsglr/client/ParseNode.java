/*
 * Created on 30.mar.2006
 *
 * Copyright (c) 2005, Karl Trygve Kalleberg <karltk near strategoxt.org>
 *
 * Licensed under the GNU General Public License, v2
 */
package org.spoofax.jsglr.client;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.spoofax.jsglr.client.imploder.TopdownTreeBuilder;
import org.spoofax.jsglr.shared.terms.ATerm;
import org.spoofax.jsglr.shared.terms.ATermFactory;
import org.spoofax.jsglr.shared.terms.ATermList;

public class ParseNode extends AbstractParseNode {

    final int label;

    final AbstractParseNode[] kids;
    
    private final boolean isParseProductionChain;

    private int cachedHashCode;

    public ParseNode(int label, AbstractParseNode[] kids) {
        this.label = label;
        this.kids = kids;
        switch (kids.length) {
        	case 2:
        		isParseProductionChain =
        			kids[0] instanceof ParseProductionNode /*kids[0].isParseProductionChain()*/
        			&& kids[1].isParseProductionChain();
        		break;
        	case 1:
        		isParseProductionChain = kids[0].isParseProductionChain();
        		break;
        	default:
        		isParseProductionChain = false;
        }
        // TODO: Optimize - create compact representation for parse production chains
    }
    
    @Override
    public boolean isParseProductionChain() {
		return isParseProductionChain;
	}
    
    @Override
    public Object toTreeTopdown(TopdownTreeBuilder builder) {
    	return builder.buildTreeNode(this);
    }

    @Override
	public Object toTreeBottomup(BottomupTreeBuilder builder) {
    	builder.visitLabel(label);

        ArrayList<Object> subtrees = new ArrayList<Object>(kids.length);
        for (int i = 0; i < kids.length; i++) {
        	subtrees.add(kids[i].toTreeBottomup(builder));
        }

        Object result = builder.buildNode(label, subtrees);
        builder.endVisitLabel(label);
		return result;
    }

    /**
     * todo: stolen from TAFReader; move elsewhere
     */
    public static ATermList makeList(ATermFactory factory, List<ATerm> terms) {
        ATermList result = factory.makeList();
        for (int i = terms.size() - 1; i >= 0; i--) {
        	result = factory.makeList(terms.get(i), result);
        }
        return result;
    }

    @Override
    public String toString() {
        return "regular(aprod(" + label + ")," + Arrays.toString(kids) + ")";
    }

    public int getLabel() {
    	return label;
    }
    
    public AbstractParseNode[] getChildren() {
		return kids;
	}

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof ParseNode))
            return false;
        if (obj == this)
            return true;
        ParseNode o = (ParseNode)obj;
        if(label != o.label || kids.length != o.kids.length
                || hashCode() != o.hashCode())
            return false;
        for(int i = 0; i < kids.length; i++) {
            if(!kids[i].equals(o.kids[i]))
                return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        if (cachedHashCode != NO_HASH_CODE)
            return cachedHashCode;
        final int prime = 31;
        int result = prime * label;
        for(AbstractParseNode n : kids)
            result += (prime * n.hashCode());
        cachedHashCode = result;
        return result;
    }

    @Override
    public String toStringShallow() {
        return "regular*(" + label + ", {" +  kids.length + "})";
    }
}
