package org.spoofax.jsglr.tests.layout;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllLayoutTests {

  public static Test suite() {
    TestSuite suite = new TestSuite(AllLayoutTests.class.getName());
    //$JUnit-BEGIN$
    suite.addTestSuite(TestHaskell.class);
    suite.addTestSuite(TestHaskellDoaitse.class);
    //$JUnit-END$
    return suite;
  }

}
