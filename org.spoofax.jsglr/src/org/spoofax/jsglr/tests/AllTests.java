/*
 * Created on 17.apr.2006
 *
 * Copyright (c) 2005, Karl Trygve Kalleberg <karltk@ii.uib.no>
 * 
 * Licensed under the GNU General Public License, v2
 */
package org.spoofax.jsglr.tests;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

    public static Test suite() {
        TestSuite suite = new TestSuite("Test for org.spoofax.jsglr.tests");
        //$JUnit-BEGIN$
        suite.addTestSuite(TestG_ambi.class);
        suite.addTestSuite(TestG1.class);
        suite.addTestSuite(TestStratego.class);
        suite.addTestSuite(TestBooleans.class);
        suite.addTestSuite(TestG2.class);
        //$JUnit-END$
        return suite;
    }

}
