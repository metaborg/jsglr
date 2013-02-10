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

public class TestStrategoStratego extends ParseTestCase {

    @Override
	public void gwtSetUp() throws ParserException, InvalidParseTableException {
        super.gwtSetUp("StrategoStratego", "strstr");
    }

    public void testSS0() throws FileNotFoundException, IOException {
        doParseTest("ss0");
    }

    public void testSS1() throws FileNotFoundException, IOException {
        doParseTest("ss1");
    }
 
    public void testSS2() throws FileNotFoundException, IOException {
        doParseTest("ss2");
    }

    public void testSS3() throws FileNotFoundException, IOException {
        doParseTest("ss3");
    }

    public void testSS0_Implode() throws FileNotFoundException, IOException {
        sglr.setTreeBuilder(new TreeBuilder());
        doParseTest("ss0");
    }

    public void testSS1_Implode() throws FileNotFoundException, IOException {
        sglr.setTreeBuilder(new TreeBuilder());
        //doParseTest("ss1");
    }

    public void testSS2_Implode() throws FileNotFoundException, IOException {
        sglr.setTreeBuilder(new TreeBuilder());
        //doParseTest("ss2");
    }

    public void testSS3_Implode() throws FileNotFoundException, IOException {
        sglr.setTreeBuilder(new TreeBuilder());
        //doParseTest("ss3");
    }

}
