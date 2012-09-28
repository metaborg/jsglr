/*
 * Created on 05.des.2005
 *
 * Copyright (c) 2005, Karl Trygve Kalleberg <karltk near strategoxt.org>
 *
 * Licensed under the GNU Lesser General Public License, v2.1
 */
package org.spoofax.jsglr.tests;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.spoofax.jsglr.client.InvalidParseTableException;
import org.spoofax.jsglr.client.ParserException;

public class TestWebDSL extends ParseTestCase {

    @Override
	public void gwtSetUp() throws ParserException, InvalidParseTableException {
        super.gwtSetUp("WebDSL", "app");
    }

    public void testWebDSL0() throws FileNotFoundException, IOException {
        doParseTest("webdsl0");
    }
}
