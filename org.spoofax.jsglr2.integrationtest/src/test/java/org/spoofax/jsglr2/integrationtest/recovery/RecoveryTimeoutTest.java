package org.spoofax.jsglr2.integrationtest.recovery;

import java.util.stream.Stream;

import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.spoofax.jsglr2.integrationtest.BaseTestWithRecoverySdf3ParseTables;
import org.spoofax.jsglr2.parser.result.ParseFailureCause;
import org.spoofax.terms.ParseError;

public class RecoveryTimeoutTest extends BaseTestWithRecoverySdf3ParseTables {

    public RecoveryTimeoutTest() {
        super("recovery.sdf3", false, false, false);
    }

    @Override protected int recoveryTimeout() {
        return 0;
    }

    @TestFactory public Stream<DynamicTest> success() throws ParseError {
        return testParseSuccess("x");
    }

    @TestFactory public Stream<DynamicTest> timeout() throws ParseError {
        return testRecoveryFails("y", ParseFailureCause.Type.RecoveryTimeout);
    }

}
