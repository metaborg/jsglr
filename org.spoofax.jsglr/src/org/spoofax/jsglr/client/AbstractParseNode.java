/*
 * Created on 30.mar.2006
 *
 * Copyright (c) 2005, Karl Trygve Kalleberg <karltk near strategoxt.org>
 *
 * Licensed under the GNU General Public License, v2
 */
package org.spoofax.jsglr.client;

import org.spoofax.jsglr.client.imploder.TopdownTreeBuilder;


public abstract class AbstractParseNode {

    protected static final int NO_HASH_CODE = 0;

    public abstract Object toTreeBottomup(BottomupTreeBuilder builder);
    
    public abstract Object toTreeTopdown(TopdownTreeBuilder builder);
    
    @Override
	abstract public boolean equals(Object obj);
    @Override
	abstract public int hashCode();

    abstract public String toStringShallow();
    @Override
	abstract public String toString();
    
    /**
     * Returns true if this is either:
     * - a {@link ParseProductionNode}.
     * - a ParseNode with a {@link ParseProductionNode} child
     *   and an {@link #isParseProductionChain()} child.
     * - a ParseNode with a single {@link #isParseProductionChain()}
     *   child.
     */
    public abstract boolean isParseProductionChain();
}
