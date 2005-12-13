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
import org.spoofax.jsglr.SGLR;

import junit.framework.TestCase;

public class TestSGLR extends TestCase {

    SGLR sglr;

    public void setUp() throws FileNotFoundException, IOException, FatalException {
        sglr = new SGLR(new FileInputStream("tests/grammars/Booleans.tbl"));
    }

    public void testBoolean() throws FileNotFoundException, IOException {

        sglr.parse(new FileInputStream("tests/data/b0.txt"));

    }
}
