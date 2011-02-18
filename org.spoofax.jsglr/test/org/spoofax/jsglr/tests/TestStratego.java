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
import org.spoofax.jsglr.client.imploder.TreeBuilder;

public class TestStratego extends ParseTestCase {

    @Override
	public void gwtSetUp() throws ParserException, InvalidParseTableException {
        super.gwtSetUp("Stratego", "str");
    }


    public void testS0() throws FileNotFoundException, IOException {
        doParseTest("s0");
    }

    public void testS1() throws FileNotFoundException, IOException {
        doParseTest("s1");
    }

    public void testS2() throws FileNotFoundException, IOException {
        doParseTest("s2");
    }

    public void testS3() throws FileNotFoundException, IOException {
        doParseTest("s3");
    }

    public void testS4() throws FileNotFoundException, IOException {
        doParseTest("s4");
    }

    public void testS5() throws FileNotFoundException, IOException {
        doParseTest("s5");
    }

    public void testS6() throws FileNotFoundException, IOException {
        doParseTest("s6");
    }
    
    /*Test if an ambiguity correctly filtered*/
    public void testS8() throws FileNotFoundException, IOException {
        doParseTest("s8");
    }

    public void testS7() throws Exception {
        sglr.setUseStructureRecovery(true);
        sglr.setTreeBuilder(new TreeBuilder());
        doCompare = false;
    	suffix = "str.recover";
        doParseTest("s7");
    }

}
