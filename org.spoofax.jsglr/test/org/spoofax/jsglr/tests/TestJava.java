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

public class TestJava extends ParseTestCase {

    @Override
	public void gwtSetUp() throws ParserException, InvalidParseTableException {
        super.gwtSetUp("Java-15", "java");
    }

    public void testJava0() throws FileNotFoundException, IOException { doParseTest("java0"); }
//    public void testJava1() throws FileNotFoundException, IOException { doParseTest("java1"); }
    public void testJava2() throws FileNotFoundException, IOException { doParseTest("java2"); }
//    public void testJava3() throws FileNotFoundException, IOException { doParseTest("java3"); }
    public void testJava4() throws FileNotFoundException, IOException { doParseTest("java4"); }
    
}
