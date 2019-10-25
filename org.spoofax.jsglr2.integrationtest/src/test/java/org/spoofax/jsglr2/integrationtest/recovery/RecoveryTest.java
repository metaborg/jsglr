package org.spoofax.jsglr2.integrationtest.recovery;

import org.junit.Test;
import org.spoofax.jsglr2.integrationtest.BaseTestWithRecoverySdf3ParseTables;
import org.spoofax.terms.ParseError;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.Assert.assertEquals;

public class RecoveryTest extends BaseTestWithRecoverySdf3ParseTables {

    public RecoveryTest() {
        super("recovery.sdf3");
    }

    @Test public void testX() throws ParseError {
        testParseSuccess("x");
    }

    @Test public void testSingleY() throws ParseError {
        testRecovery("y");

        testRecoveryTraced("y", recoveryTrace -> {
            assertEquals(Arrays.asList(1), recoveryTrace.started);
            assertEquals(Collections.emptyList(), recoveryTrace.ended);
        });

        testRecoveryTraced("y ", recoveryTrace -> {
            assertEquals(Arrays.asList(1), recoveryTrace.started);
            assertEquals(Arrays.asList(2), recoveryTrace.ended);
        });
    }

    @Test public void testYY() throws ParseError {
        testRecovery("y y");

        testRecoveryTraced("y y", recoveryTrace -> {
            assertEquals(Arrays.asList(1, 3), recoveryTrace.started);
            assertEquals(Arrays.asList(2), recoveryTrace.ended);
        });

        testRecoveryTraced("y y ", recoveryTrace -> {
            assertEquals(Arrays.asList(1, 3), recoveryTrace.started);
            assertEquals(Arrays.asList(2, 4), recoveryTrace.ended);
        });
    }

    @Test public void testYXY() throws ParseError {
        testRecovery("y x y");

        testRecoveryTraced("y x y", recoveryTrace -> {
            assertEquals(Arrays.asList(1, 5), recoveryTrace.started);
            assertEquals(Arrays.asList(2), recoveryTrace.ended);
        });

        testRecoveryTraced("y x y ", recoveryTrace -> {
            assertEquals(Arrays.asList(1, 5), recoveryTrace.started);
            assertEquals(Arrays.asList(2, 6), recoveryTrace.ended);
        });
    }

}
