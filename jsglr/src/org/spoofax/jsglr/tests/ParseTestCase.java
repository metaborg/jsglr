/*
 * Created on 13.des.2005
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

public abstract class ParseTestCase extends TestCase {

    SGLR sglr;
    PureFactory pf;
    String suffix;
    
    public void setUp(String grammar, String suffix) throws FileNotFoundException, IOException, FatalException, InvalidParseTableException {
        this.suffix = suffix;
        pf = new PureFactory();
        sglr = new SGLR(pf, new FileInputStream("tests/grammars/" + grammar + ".tbl"));
    }

    public void doParseTest(String s) throws FileNotFoundException, IOException {
        Tools.setOutput("tests/jsglr-full-trace-" + s);
        ATerm parsed = sglr.parse(new FileInputStream("tests/data/" + s
                + "." + suffix));
        ATerm loaded = pf.readFromFile("tests/data/" + s + ".trm");
    
        Tools.debug(parsed);
    
        assertNotNull(parsed);
        assertNotNull(loaded);
        System.out.println(parsed);
        // assertTrue(parsed.match(loaded) != null);
    }

}
