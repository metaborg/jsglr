/*
 * Created on 05.des.2005
 *
 * Copyright (c) 2005, Karl Trygve Kalleberg <karltk near strategoxt.org>
 *
 * Licensed under the GNU Lesser General Public License, v2.1
 */
package org.spoofax.jsglr.tests;

import org.spoofax.jsglr.client.InvalidParseTableException;
import org.spoofax.jsglr.client.ParserException;

public class TestG_ambi extends ParseTestCase {

    @Override
	public void gwtSetUp() throws ParserException, InvalidParseTableException {
        super.gwtSetUp("G-ambi", "txt");
    }


    public void testG_ambi_1() {
    	doParseTest("g-ambi-1");
    }

    public void testG_ambi_2() throws InterruptedException {
        doParseTest("g-ambi-2");
    }
}
