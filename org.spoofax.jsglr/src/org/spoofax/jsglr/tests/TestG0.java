/*
 * Created on 05.des.2005
 *
 * Copyright (c) 2005, Karl Trygve Kalleberg <karltk@ii.uib.no>
 * 
 * Licensed under the GNU Lesser General Public License, v2.1
 */
package org.spoofax.jsglr.tests;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.spoofax.jsglr.FatalException;
import org.spoofax.jsglr.InvalidParseTableException;

public class TestG0 extends ParseTestCase {

    public void setUp() throws FileNotFoundException, IOException,
            FatalException, InvalidParseTableException {
        super.setUp("G0", "txt");
    }


    public void testG0_1() throws FileNotFoundException, IOException {
        doParseTest("g0_1");
    }

    public void testG0_2() throws FileNotFoundException, IOException {
        doParseTest("g0_2");
    }
}
