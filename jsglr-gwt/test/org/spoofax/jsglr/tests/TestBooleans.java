/*
 * Created on 05.des.2005
 *
 * Copyright (c) 2005, Karl Trygve Kalleberg <karltk near strategoxt.org>
 *
 * Licensed under the GNU Lesser General Public License, v2.1
 */
package org.spoofax.jsglr.tests;

public class TestBooleans extends ParseTestCase {

    @Override
    protected void gwtSetUp() throws Exception {
        super.gwtSetUp("Booleans", "txt");
    }

    public void testB0() {
        doParseTest("b0");
    }

    public void testB1() {
        doParseTest("b1");
    }

    public void testB2() {
        doParseTest("b2");
    }

    public void testB3() {
        doParseTest("b3");
    }

    public void testB4() {
        doParseTest("b4");
    }

    public void testB5() {
        doParseTest("b5");
    }

    public void testB6() {
        doParseTest("b6");
    }
}
