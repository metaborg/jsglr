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

public class TestG_right_assoc extends ParseTestCase {

    @Override
	public void gwtSetUp() throws 
            ParserException, InvalidParseTableException {
        super.gwtSetUp("G-right-assoc", "txt");
    }


    public void testG_right_assoc_1() {
        doParseTest("g-right-assoc-1");
    }
}
