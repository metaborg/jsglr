package org.spoofax.jsglr2.integrationtest.recovery;

import org.junit.jupiter.api.Test;
import org.spoofax.jsglr2.integrationtest.BaseTestWithRecoverySdf3ParseTables;
import org.spoofax.terms.ParseError;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.spoofax.jsglr2.integrationtest.Util.newlines;

public class RecoveryBacktrackingTest extends BaseTestWithRecoverySdf3ParseTables {

    public RecoveryBacktrackingTest() {
        super("recovery.sdf3");
    }

    @Test public void testSingleLineX() throws ParseError {
        testParseSuccess("x");
    }

    @Test public void testSingleLineY() throws ParseError {
        testRecovery("y");

        testRecoveryTraced("y", recoveryTrace -> {
            assertEquals(Arrays.asList(0), recoveryTrace.backtrackChoicePoints);
            assertEquals(Arrays.asList(1), recoveryTrace.started);
            assertEquals(Arrays.asList(), recoveryTrace.ended);
            assertEquals(Arrays.asList(new RecoverIteration(0, 1, 0)), recoveryTrace.iterations);
        });

        testRecoveryTraced("y     ", recoveryTrace -> {
            assertEquals(Arrays.asList(0), recoveryTrace.backtrackChoicePoints);
            assertEquals(Arrays.asList(1), recoveryTrace.started);
            assertEquals(Arrays.asList(6), recoveryTrace.ended);
            assertEquals(Arrays.asList(new RecoverIteration(0, 1, 0)), recoveryTrace.iterations);
        });
    }

    @Test public void testSingleLineYY() throws ParseError {
        testRecovery("y y");

        testRecoveryTraced("y     y", recoveryTrace -> {
            assertEquals(Arrays.asList(1, 7), recoveryTrace.started);
            assertEquals(Arrays.asList(6), recoveryTrace.ended);
            assertEquals(Arrays.asList(
            //@formatter:off
                new RecoverIteration(0, 1, 0),
                new RecoverIteration(0, 7, 0),
                new RecoverIteration(1, 7, 0)
            //@formatter:on
            ), recoveryTrace.iterations);
        });

        testRecoveryTraced("y     y     ", recoveryTrace -> {
            assertEquals(Arrays.asList(1, 7), recoveryTrace.started);
            assertEquals(Arrays.asList(6, 12), recoveryTrace.ended);
            assertEquals(Arrays.asList(
            //@formatter:off
                new RecoverIteration(0, 1, 0),
                new RecoverIteration(0, 7, 0),
                new RecoverIteration(1, 7, 0)
            //@formatter:on
            ), recoveryTrace.iterations);
        });
    }

    @Test public void testMultiLineX() throws ParseError {
        testParseSuccess("x" + newlines(1) + "x");
        testParseSuccess("x" + newlines(10) + "x");
    }

    @Test public void testMultiLineY() throws ParseError {
        testRecovery("" + newlines(1) + "y");
        testRecovery("" + newlines(3) + "y");

        testRecoveryTraced(newlines(1) + "y     ", recoveryTrace -> {
            assertEquals(Arrays.asList(0, 1), recoveryTrace.backtrackChoicePoints);
            assertEquals(Arrays.asList(2), recoveryTrace.started);
            assertEquals(Arrays.asList(7), recoveryTrace.ended);
            assertEquals(Arrays.asList(new RecoverIteration(0, 2, 1)), recoveryTrace.iterations);
        });
        testRecoveryTraced(newlines(1) + " y     ", recoveryTrace -> {
            assertEquals(Arrays.asList(0, 1), recoveryTrace.backtrackChoicePoints);
            assertEquals(Arrays.asList(3), recoveryTrace.started);
            assertEquals(Arrays.asList(8), recoveryTrace.ended);
            assertEquals(Arrays.asList(new RecoverIteration(0, 3, 1)), recoveryTrace.iterations);
        });
        testRecoveryTraced(newlines(3) + "y     ", recoveryTrace -> {
            assertEquals(Arrays.asList(0, 1, 2, 3), recoveryTrace.backtrackChoicePoints);
            assertEquals(Arrays.asList(4), recoveryTrace.started);
            assertEquals(Arrays.asList(9), recoveryTrace.ended);
            assertEquals(Arrays.asList(new RecoverIteration(0, 4, 3)), recoveryTrace.iterations);
        });
        testRecoveryTraced(newlines(3) + "y" + newlines(5), recoveryTrace -> {
            assertEquals(Arrays.asList(0, 1, 2, 3, 5, 6, 7, 8, 9), recoveryTrace.backtrackChoicePoints);
            assertEquals(Arrays.asList(4), recoveryTrace.started);
            assertEquals(Arrays.asList(9), recoveryTrace.ended);
            assertEquals(Arrays.asList(new RecoverIteration(0, 4, 3)), recoveryTrace.iterations);
        });
    }

    @Test public void testMultiLineYY() throws ParseError {
        testRecovery("" + newlines(1) + "yy");
        testRecovery("" + newlines(2) + "yy");
        testRecovery("" + newlines(3) + "yy");

        testRecoveryTraced(newlines(2) + "yy     ", recoveryTrace -> {
            assertEquals(Arrays.asList(0, 1, 2), recoveryTrace.backtrackChoicePoints);
            assertEquals(Arrays.asList(
            //@formatter:off
                new RecoverIteration(0, 3, 2),
                new RecoverIteration(1, 3, 1)
            //@formatter:on
            ), recoveryTrace.iterations);
        });
    }

    @Test public void testMultiLineYYY() throws ParseError {
        testRecovery("" + newlines(3) + "yyy");

        testRecoveryTraced(newlines(3) + "yyy     ", recoveryTrace -> {
            assertEquals(Arrays.asList(0, 1, 2, 3), recoveryTrace.backtrackChoicePoints);
            assertEquals(Arrays.asList(
            //@formatter:off
                new RecoverIteration(0, 4, 3),
                new RecoverIteration(1, 4, 2),
                new RecoverIteration(2, 4, 1)
            //@formatter:on
            ), recoveryTrace.iterations);
        });
    }

    @Test public void testMultiLineYYYY() throws ParseError {
        testRecovery("" + newlines(3) + "yyyy", false);

        testRecoveryTraced(newlines(3) + "yyyy     ", recoveryTrace -> {
            assertEquals(Arrays.asList(0, 1, 2, 3), recoveryTrace.backtrackChoicePoints);
            assertEquals(Arrays.asList(
            //@formatter:off
                new RecoverIteration(0, 4, 3),
                new RecoverIteration(1, 4, 2),
                new RecoverIteration(2, 4, 1)
            //@formatter:on
            ), recoveryTrace.iterations);
        }, false);
    }

}
