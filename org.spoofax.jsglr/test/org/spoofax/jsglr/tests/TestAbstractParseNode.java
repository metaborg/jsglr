/*
 * Created on 21.apr.2006
 *
 * Copyright (c) 2005, Karl Trygve Kalleberg <karltk near strategoxt.org>
 * 
 * Licensed under the GNU General Public License, v2
 */
package org.spoofax.jsglr.tests;

import junit.framework.TestCase;

import org.spoofax.jsglr.client.AbstractParseNode;
import org.spoofax.jsglr.client.ParseNode;
import org.spoofax.jsglr.client.ParseProductionNode;

public class TestAbstractParseNode extends TestCase {

    private AbstractParseNode pn0; 
    private AbstractParseNode pn1;
    
    @Override
    protected void setUp() throws Exception {
        AbstractParseNode[]  r0 = { new ParseProductionNode(123, 0, 0) };
        pn0 = new ParseNode(233, r0, AbstractParseNode.PARSENODE, 0, 0, false, false, false, false, false, false, false);

        AbstractParseNode[] r1 = { new ParseProductionNode(123, 0, 0) };
        pn1 = new ParseNode(233, r1, AbstractParseNode.PARSENODE, 0, 0, false, false, false, false, false, false, false);
    }

    public void testHashCode() {
        assertTrue(pn0.hashCode() == pn1.hashCode());
    }

    public void testEquals() {
        assertTrue(pn0.equals(pn1));
    }


}
