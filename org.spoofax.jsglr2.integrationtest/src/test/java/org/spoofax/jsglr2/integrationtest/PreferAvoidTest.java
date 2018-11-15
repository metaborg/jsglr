package org.spoofax.jsglr2.integrationtest;

import org.junit.Test;

public class PreferAvoidTest extends BaseTestWithSpoofaxCoreSdf3 {

    public PreferAvoidTest() {
        super("prefer-avoid.sdf3");
    }

    @Test
    public void testAvoid() {
        testSuccessByExpansions("a x", "Avoid(amb([X2, X3]))");
    }

    @Test
    public void testPrefer() {
        testSuccessByExpansions("p x", "Prefer(X1)");
    }

}