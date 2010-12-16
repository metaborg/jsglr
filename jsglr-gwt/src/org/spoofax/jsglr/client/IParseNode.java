/*
 * Created on 30.mar.2006
 *
 * Copyright (c) 2005, Karl Trygve Kalleberg <karltk near strategoxt.org>
 *
 * Licensed under the GNU General Public License, v2
 */
package org.spoofax.jsglr.client;

import org.spoofax.jsglr.shared.terms.ATerm;

public abstract class IParseNode {

    protected static final int NO_HASH_CODE = 0;

    public abstract ATerm toParseTree(ParseTable pt);

    @Override
	abstract public boolean equals(Object obj);
    @Override
	abstract public int hashCode();

    abstract public String toStringShallow();
    @Override
	abstract public String toString();
}
