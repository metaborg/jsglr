package org.spoofax.jsglr2.integrationtest.recovery;

import org.junit.Test;
import org.spoofax.jsglr2.integrationtest.BaseTestWithRecoverySdf3ParseTables;
import org.spoofax.terms.ParseError;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.spoofax.jsglr2.integrationtest.Util.newlines;

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
            assertEquals(Arrays.asList(0), recoveryTrace.backtrackChoicePoints);
            assertEquals(Arrays.asList(1), recoveryTrace.started);
            assertEquals(Arrays.asList(), recoveryTrace.ended);
            assertEquals(Arrays.asList(new RecoverIteration(0, 1, 0)), recoveryTrace.iterations);
        });

        testRecoveryTraced("y ", recoveryTrace -> {
            assertEquals(Arrays.asList(0), recoveryTrace.backtrackChoicePoints);
            assertEquals(Arrays.asList(1), recoveryTrace.started);
            assertEquals(Arrays.asList(2), recoveryTrace.ended);
            assertEquals(Arrays.asList(new RecoverIteration(0, 1, 0)), recoveryTrace.iterations);
        });
    }

    @Test public void testYY() throws ParseError {
        testRecovery("y y");

        testRecoveryTraced("y y", recoveryTrace -> {
            assertEquals(Arrays.asList(0), recoveryTrace.backtrackChoicePoints);
            assertEquals(Arrays.asList(1, 3), recoveryTrace.started);
            assertEquals(Arrays.asList(2), recoveryTrace.ended);
            assertEquals(Arrays.asList(
            //@formatter:off
                new RecoverIteration(0, 1, 0),
                new RecoverIteration(0, 3, 0),
                new RecoverIteration(1, 3, 0)
            //@formatter:on
            ), recoveryTrace.iterations);
        });

        testRecoveryTraced("y y ", recoveryTrace -> {
            assertEquals(Arrays.asList(0), recoveryTrace.backtrackChoicePoints);
            assertEquals(Arrays.asList(1, 3), recoveryTrace.started);
            assertEquals(Arrays.asList(2, 4), recoveryTrace.ended);
            assertEquals(Arrays.asList(
            //@formatter:off
                new RecoverIteration(0, 1, 0),
                new RecoverIteration(0, 3, 0),
                new RecoverIteration(1, 3, 0)
            //@formatter:on
            ), recoveryTrace.iterations);
        });
    }

    @Test public void testYXY() throws ParseError {
        testRecovery("yxy");

        testRecoveryTraced("yxy", recoveryTrace -> {
            assertEquals(Arrays.asList(0), recoveryTrace.backtrackChoicePoints);
            assertEquals(Arrays.asList(1, 3), recoveryTrace.started);
            assertEquals(Arrays.asList(2), recoveryTrace.ended);
            assertEquals(Arrays.asList(
            //@formatter:off
                new RecoverIteration(0, 1, 0),
                new RecoverIteration(0, 3, 0),
                new RecoverIteration(1, 3, 0)
            //@formatter:on
            ), recoveryTrace.iterations);
        });

        testRecoveryTraced("yxy ", recoveryTrace -> {
            assertEquals(Arrays.asList(0), recoveryTrace.backtrackChoicePoints);
            assertEquals(Arrays.asList(1, 3), recoveryTrace.started);
            assertEquals(Arrays.asList(2, 4), recoveryTrace.ended);
            assertEquals(Arrays.asList(
            //@formatter:off
                new RecoverIteration(0, 1, 0),
                new RecoverIteration(0, 3, 0),
                new RecoverIteration(1, 3, 0)
            //@formatter:on
            ), recoveryTrace.iterations);
        });
    }

}
