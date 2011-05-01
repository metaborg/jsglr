/*
 * Created on 30.mar.2006
 *
 * Copyright (c) 2005, Karl Trygve Kalleberg <karltk near strategoxt.org>
 *
 * Licensed under the GNU General Public License, v2
 */
package org.spoofax.jsglr.client;

import org.spoofax.jsglr.client.imploder.TopdownTreeBuilder;


public class ParseProductionNode extends AbstractParseNode {

	private static final AbstractParseNode[] NO_CHILDREN =
		new AbstractParseNode[0];

	public final int prod;

    public ParseProductionNode(int prod) {
        this.prod = prod;
    }
    
    @Override
	public boolean isParseProductionChain() {
    	return true;
    }

    @Override
	public Object toTreeBottomup(BottomupTreeBuilder builder) {
    	return builder.buildProduction(prod);
    }
    
    @Override
    public Object toTreeTopdown(TopdownTreeBuilder builder) {
    	return builder.buildTreeProduction(this);
    }

    @Override
    public String toString() {
        return "" + prod;
    }

    public int getProduction() { return prod; }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof ParseProductionNode))
            return false;
        return prod == ((ParseProductionNode)obj).prod;
    }

    @Override
    public int hashCode() {
        return 6359 * prod;
    }

    @Override
    public String toStringShallow() {
        return "prod*(" + prod + ")";
    }

	@Override
	public int getNodeType() {
		return AbstractParseNode.PARSE_PRODUCTION_NODE;
	}

	@Override
	public AbstractParseNode[] getChildren() {
		return NO_CHILDREN;
	}
}
