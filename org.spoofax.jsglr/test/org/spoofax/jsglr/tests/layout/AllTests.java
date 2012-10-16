package org.spoofax.jsglr.tests.layout;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

  public static Test suite() {
    TestSuite suite = new TestSuite(AllTests.class.getName());
    //$JUnit-BEGIN$
    suite.addTestSuite(TestSimple1.class);
    suite.addTestSuite(TestSimple2.class);
    suite.addTestSuite(TestHaskell.class);
    suite.addTestSuite(TestHaskellDoaitse.class);
    //$JUnit-END$
    return suite;
  }

}
