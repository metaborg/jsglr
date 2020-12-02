package org.spoofax.jsglr2.integrationtest.recovery;

import java.util.stream.Stream;

import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.spoofax.jsglr2.integrationtest.BaseTestWithRecoverySdf3ParseTables;
import org.spoofax.terms.ParseError;

public class RecoveryDisambiguationTest extends BaseTestWithRecoverySdf3ParseTables {

    public RecoveryDisambiguationTest() {
        super("recovery-disambiguation.sdf3", false, false);
    }

    @TestFactory public Stream<DynamicTest> testSuccess() throws ParseError {
        return testSuccessByAstString("ab", "Some(AB(A,B))");
    }

    @TestFactory public Stream<DynamicTest> testBInsertion() throws ParseError {
        return testRecovery("a", "Some(AB(A,INSERTION))");
    }

    @TestFactory public Stream<DynamicTest> testAInsertion() throws ParseError {
        return testRecovery("b", "Some(AB(INSERTION,B))");
    }

    @TestFactory public Stream<DynamicTest> testWater() throws ParseError {
        return testRecovery("c", "None()");
    }

}
