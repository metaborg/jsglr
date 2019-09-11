package org.spoofax.jsglr2.integrationtest.grammars;

import org.junit.Test;
import org.spoofax.jsglr2.integrationtest.BaseTestWithRecoverySdf3ParseTables;
import org.spoofax.terms.ParseError;

public class RecoveryTest extends BaseTestWithRecoverySdf3ParseTables {

    public RecoveryTest() {
        super("recovery.sdf3");
    }

    @Test public void testX() throws ParseError {
        testParseSuccess("x");
    }

    @Test public void testY() throws ParseError {
        testRecovery("y");
    }

}
