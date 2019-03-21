package org.spoofax.jsglr2.integrationtest.grammars;

import org.junit.Test;
import org.spoofax.jsglr2.incremental.EditorUpdate;
import org.spoofax.jsglr2.integrationtest.BaseTestWithSdf3ParseTables;

public class PreferAvoidTest extends BaseTestWithSdf3ParseTables {

    public PreferAvoidTest() {
        super("prefer-avoid.sdf3");
    }

    @Test public void testAvoid() {
        testSuccessByExpansions("a x", "Avoid(amb([X2, X3]))");
    }

    @Test public void testPrefer() {
        testSuccessByExpansions("p x", "Prefer(X1)");
    }

    @Test public void testIncrementalPreferAvoid() {
        // TODO leading space not necessary, but tree updater is still broken
        testIncrementalSuccessByExpansions(" p x ",
            new EditorUpdate[] { new EditorUpdate(1, 2, "a"), new EditorUpdate(1, 2, "p") },
            new String[] { "Prefer(X1)", "Avoid(amb([X2,X3]))", "Prefer(X1)" });
    }
}