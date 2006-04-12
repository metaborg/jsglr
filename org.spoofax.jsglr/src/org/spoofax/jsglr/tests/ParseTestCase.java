/*
 * Created on 13.des.2005
 *
 * Copyright (c) 2005, Karl Trygve Kalleberg <karltk@ii.uib.no>
 * 
 * Licensed under the GNU Lesser General Public License, v2.1
 */
package org.spoofax.jsglr.tests;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.FileOutputStream;
import java.io.PrintWriter;

import junit.framework.TestCase;

import org.spoofax.jsglr.FatalException;
import org.spoofax.jsglr.InvalidParseTableException;
import org.spoofax.jsglr.SGLR;
import org.spoofax.jsglr.Tools;

import aterm.ATerm;

public abstract class ParseTestCase extends TestCase {

    SGLR sglr;
    String suffix;
    
    public void setUp(String grammar, String suffix) throws FileNotFoundException, IOException, FatalException, InvalidParseTableException {
        this.suffix = suffix;
        sglr = new SGLR("tests/grammars/" + grammar + ".tbl");
    }

    public void doParseTest(String s) throws FileNotFoundException, IOException {
        Tools.setOutput("tests/jsglr-full-trace-" + s);
        ATerm parsed = sglr.parse(new FileInputStream("tests/data/" + s + "." + suffix));
        ATerm loaded = sglr.getFactory().readFromFile("tests/data/" + s + ".trm");
    
        assertNotNull(parsed);
        assertNotNull(loaded);

        if(parsed.match(loaded) == null) {
            PrintWriter printWriter = new PrintWriter(new FileOutputStream("tests/data/" + s + ".trm.parsed"));
            printWriter.print(parsed.toString());
            printWriter.flush();
            assertTrue(false);
        }
    }

}
