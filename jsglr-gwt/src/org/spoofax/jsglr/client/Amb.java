/*
 * Created on 30.mar.2006
 *
 * Copyright (c) 2005, Karl Trygve Kalleberg <karltk near strategoxt.org>
 *
 * Licensed under the GNU General Public License, v2
 */
package org.spoofax.jsglr.client;

import java.util.ArrayList;
import java.util.List;

import org.spoofax.jsglr.client.imploder.TopdownTreeBuilder;


public class Amb extends AbstractParseNode {

	private final AbstractParseNode[] alternatives;

	private int cachedHashCode = NO_HASH_CODE;

	Amb(AbstractParseNode left, AbstractParseNode right) {
		alternatives = new AbstractParseNode[2];
		alternatives[0] = left;
		alternatives[1] = right;
	}

	public Amb(AbstractParseNode[] alternatives) {
		this.alternatives = alternatives;
	}

	@Override
	public Object toTreeBottomup(BottomupTreeBuilder builder) {
    	ArrayList<Object> collect = new ArrayList<Object>();
    	addToTree(builder, collect);
    	return builder.buildAmb(collect);
    }
    
    @Override
    public Object toTreeTopdown(TopdownTreeBuilder builder) {
    	return builder.buildTreeAmb(this);
    }
    
    private void addToTree(BottomupTreeBuilder builder, List<Object> collect) {
    	for (int i = alternatives.length - 1; i >= 0; i--) {
    		AbstractParseNode alt = alternatives[i];
    		if (alt instanceof Amb) {
    			((Amb) alt).addToTree(builder, collect);
    		} else {
    			collect.add(alt.toTreeBottomup(builder));
    		}
    	}
    }

	@Override
	public String toString() {
		return "amb(" + alternatives + ")";
	}

	public boolean hasAmbiguity(AbstractParseNode newNode) {
		throw new NotImplementedException();
	}

	public AbstractParseNode[] getAlternatives() {
		return alternatives;
	}

	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof Amb)) {
			return false;
		}
		if (obj == this) {
			return true;
		}
		final Amb o = (Amb)obj;
		if(o.alternatives.length != alternatives.length
				|| o.hashCode() != hashCode()) {
			return false;
		}
		for(int i = 0; i < alternatives.length; i++) {
			if(!alternatives[i].equals(o.alternatives[i])) {
				return false;
			}
		}
		return true;
	}

	@Override
	public int hashCode() {
		if (cachedHashCode != NO_HASH_CODE) {
			assert cachedHashCode == alternatives.hashCode();
			return cachedHashCode;
		}
		final int result = cachedHashCode = alternatives.hashCode();
		return result;
	}

	@Override
	public String toStringShallow() {
		return "Amb";
	}
}
