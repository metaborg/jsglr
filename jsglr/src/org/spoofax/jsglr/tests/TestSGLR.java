/*
 * Created on 05.des.2005
 *
 * Copyright (c) 2005, Karl Trygve Kalleberg <karltk@ii.uib.no>
 * 
 * Licensed under the GNU General Public License, v2
 */
package org.spoofax.jsglr.tests;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.spoofax.jsglr.FatalException;
import org.spoofax.jsglr.InvalidParseTableException;
import org.spoofax.jsglr.SGLR;
import org.spoofax.jsglr.Tools;

import aterm.ATerm;
import aterm.pure.PureFactory;

import junit.framework.TestCase;

public class TestSGLR extends TestCase {

    SGLR sglr;

    PureFactory pf;

    public void setUp() throws FileNotFoundException, IOException,
            FatalException, InvalidParseTableException {
        pf = new PureFactory();
        sglr = new SGLR(pf, new FileInputStream("tests/grammars/Booleans.tbl"));
    }


    public void testB0() throws FileNotFoundException, IOException {
        doParseTest("b0");
    }

    public void testB1() throws FileNotFoundException, IOException {
        doParseTest("b1");
    }

    public void testB2() throws FileNotFoundException, IOException {
        doParseTest("b2");
    }

    public void testB3() throws FileNotFoundException, IOException {
        doParseTest("b3");
    }

    public void testB4() throws FileNotFoundException, IOException {
        doParseTest("b4");
    }

    public void doParseTest(String s) throws FileNotFoundException, IOException {
        ATerm parsed = sglr.parse(new FileInputStream("tests/data/" + s
                + ".txt"));
        //ATerm loaded = pf.readFromFile("tests/data/" + s + ".txt");

        Tools.debug(parsed);

        assertNotNull(parsed);
        //assertNotNull(loaded);
        // assertTrue(parsed.match(loaded) != null);
    }
}
