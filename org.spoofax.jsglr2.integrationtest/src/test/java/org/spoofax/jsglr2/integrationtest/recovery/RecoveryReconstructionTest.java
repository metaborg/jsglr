package org.spoofax.jsglr2.integrationtest.recovery;

import java.util.stream.Stream;

import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.spoofax.jsglr2.integrationtest.BaseTestWithRecoverySdf3ParseTables;
import org.spoofax.terms.ParseError;

public class RecoveryReconstructionTest extends BaseTestWithRecoverySdf3ParseTables {

    public RecoveryReconstructionTest() {
        super("recovery-disambiguation.sdf3", false, false, true);
    }

    @TestFactory public Stream<DynamicTest> testBInsertion() throws ParseError {
        return testRecoveryReconstruction("a", "ab", 1, 0);
    }

    @TestFactory public Stream<DynamicTest> testAInsertion() throws ParseError {
        return testRecoveryReconstruction("b", "ab", 1, 0);
    }

    @TestFactory public Stream<DynamicTest> testWater() throws ParseError {
        return testRecoveryReconstruction("c", " ", 0, 1);
    }

    @TestFactory public Stream<DynamicTest> testAInsertionWithLayout() throws ParseError {
        return testRecoveryReconstruction("b ", "ab ", 1, 0);
    }

}
