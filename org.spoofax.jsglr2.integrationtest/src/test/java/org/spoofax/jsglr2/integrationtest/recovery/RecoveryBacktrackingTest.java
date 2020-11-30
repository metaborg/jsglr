package org.spoofax.jsglr2.integrationtest.recovery;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.spoofax.jsglr2.integrationtest.Util.newlines;

import java.util.Arrays;
import java.util.stream.Stream;

import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.spoofax.jsglr2.integrationtest.BaseTestWithRecoverySdf3ParseTables;
import org.spoofax.terms.ParseError;

public class RecoveryBacktrackingTest extends BaseTestWithRecoverySdf3ParseTables {

    public RecoveryBacktrackingTest() {
        super("recovery.sdf3", false, false);
    }

    @TestFactory public Stream<DynamicTest> testSingleLineX() throws ParseError {
        return testParseSuccess("x");
    }

    @TestFactory public Stream<DynamicTest> testSingleLineYRecovering() throws ParseError {
        return testRecovery("y");
    }

    @TestFactory public Stream<DynamicTest> testSingleLineYTraced() throws ParseError {
        return testRecoveryTraced("y", recoveryTrace -> {
            assertEquals(Arrays.asList(0), recoveryTrace.backtrackChoicePoints);
            assertEquals(Arrays.asList(1), recoveryTrace.started);
            assertEquals(Arrays.asList(), recoveryTrace.ended);
            assertEquals(Arrays.asList(new RecoverIteration(0, 1, 0)), recoveryTrace.iterations);
        });
    }

    @TestFactory public Stream<DynamicTest> testSingleLineYCompleting() throws ParseError {
        return testRecoveryTraced("y     ", recoveryTrace -> {
            assertEquals(Arrays.asList(0), recoveryTrace.backtrackChoicePoints);
            assertEquals(Arrays.asList(1), recoveryTrace.started);
            assertEquals(Arrays.asList(6), recoveryTrace.ended);
            assertEquals(Arrays.asList(new RecoverIteration(0, 1, 0)), recoveryTrace.iterations);
        });
    }

    @TestFactory public Stream<DynamicTest> testSingleLineYYRecovering() throws ParseError {
        return testRecovery("y y");
    }

    @TestFactory public Stream<DynamicTest> testSingleLineYYTraced() throws ParseError {
        return testRecoveryTraced("y     y", recoveryTrace -> {
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
    }

    @TestFactory public Stream<DynamicTest> testSingleLineYYCompleting() throws ParseError {
        return testRecoveryTraced("y     y     ", recoveryTrace -> {
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

    @TestFactory public Stream<DynamicTest> test1MultiLineX() throws ParseError {
        return testParseSuccess("x" + newlines(1) + "x");
    }

    @TestFactory public Stream<DynamicTest> test10MultiLineX() throws ParseError {
        return testParseSuccess("x" + newlines(10) + "x");
    }

    @TestFactory public Stream<DynamicTest> test1MultiLineY() throws ParseError {
        return testRecovery("" + newlines(1) + "y");
    }

    @TestFactory public Stream<DynamicTest> test3MultiLineY() throws ParseError {
        return testRecovery("" + newlines(3) + "y");
    }

    @TestFactory public Stream<DynamicTest> testMultiLineY1() throws ParseError {
        return testRecoveryTraced(newlines(1) + "y     ", recoveryTrace -> {
            assertEquals(Arrays.asList(0, 1), recoveryTrace.backtrackChoicePoints);
            assertEquals(Arrays.asList(2), recoveryTrace.started);
            assertEquals(Arrays.asList(7), recoveryTrace.ended);
            assertEquals(Arrays.asList(new RecoverIteration(0, 2, 1)), recoveryTrace.iterations);
        });
    }

    @TestFactory public Stream<DynamicTest> testMultiLineY2() throws ParseError {
        return testRecoveryTraced(newlines(1) + " y     ", recoveryTrace -> {
            assertEquals(Arrays.asList(0, 1), recoveryTrace.backtrackChoicePoints);
            assertEquals(Arrays.asList(3), recoveryTrace.started);
            assertEquals(Arrays.asList(8), recoveryTrace.ended);
            assertEquals(Arrays.asList(new RecoverIteration(0, 3, 1)), recoveryTrace.iterations);
        });
    }

    @TestFactory public Stream<DynamicTest> testMultiLineY3() throws ParseError {
        return testRecoveryTraced(newlines(3) + "y     ", recoveryTrace -> {
            assertEquals(Arrays.asList(0, 1, 2, 3), recoveryTrace.backtrackChoicePoints);
            assertEquals(Arrays.asList(4), recoveryTrace.started);
            assertEquals(Arrays.asList(9), recoveryTrace.ended);
            assertEquals(Arrays.asList(new RecoverIteration(0, 4, 3)), recoveryTrace.iterations);
        });
    }

    @TestFactory public Stream<DynamicTest> testMultiLineY4() throws ParseError {
        return testRecoveryTraced(newlines(3) + "y" + newlines(5), recoveryTrace -> {
            assertEquals(Arrays.asList(0, 1, 2, 3, 5, 6, 7, 8, 9), recoveryTrace.backtrackChoicePoints);
            assertEquals(Arrays.asList(4), recoveryTrace.started);
            assertEquals(Arrays.asList(9), recoveryTrace.ended);
            assertEquals(Arrays.asList(new RecoverIteration(0, 4, 3)), recoveryTrace.iterations);
        });
    }

    @TestFactory public Stream<DynamicTest> test1MultiLineYY() throws ParseError {
        return testRecovery("" + newlines(1) + "yy");
    }

    @TestFactory public Stream<DynamicTest> test2MultiLineYY() throws ParseError {
        return testRecovery("" + newlines(2) + "yy");
    }

    @TestFactory public Stream<DynamicTest> test3MultiLineYY() throws ParseError {
        return testRecovery("" + newlines(3) + "yy");
    }

    @TestFactory public Stream<DynamicTest> testMultiLineYYTraced() throws ParseError {
        return testRecoveryTraced(newlines(2) + "yy     ", recoveryTrace -> {
            assertEquals(Arrays.asList(0, 1, 2), recoveryTrace.backtrackChoicePoints);
            assertEquals(Arrays.asList(
            //@formatter:off
                new RecoverIteration(0, 3, 2),
                new RecoverIteration(1, 3, 1)
            //@formatter:on
            ), recoveryTrace.iterations);
        });
    }

    @TestFactory public Stream<DynamicTest> testMultiLineYYYRecovering() throws ParseError {
        return testRecovery("" + newlines(3) + "yyy");
    }

    @TestFactory public Stream<DynamicTest> testMultiLineYYYTraced() throws ParseError {
        return testRecoveryTraced(newlines(3) + "yyy     ", recoveryTrace -> {
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

    @TestFactory public Stream<DynamicTest> testMultiLineYYYYRecovering() throws ParseError {
        return testRecovery("" + newlines(3) + "yyyy", false, null);
    }

    @TestFactory public Stream<DynamicTest> testMultiLineYYYYTraced() throws ParseError {
        return testRecoveryTraced(newlines(3) + "yyyy     ", recoveryTrace -> {
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
