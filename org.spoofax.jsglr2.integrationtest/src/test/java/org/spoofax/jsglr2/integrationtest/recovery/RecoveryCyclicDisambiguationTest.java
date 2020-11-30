package org.spoofax.jsglr2.integrationtest.recovery;

import java.util.stream.Stream;

import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.spoofax.jsglr2.integrationtest.BaseTestWithRecoverySdf3ParseTables;
import org.spoofax.terms.ParseError;

public class RecoveryCyclicDisambiguationTest extends BaseTestWithRecoverySdf3ParseTables {

    public RecoveryCyclicDisambiguationTest() {
        super("recovery-cyclic-disambiguation.sdf3", false);
    }

    @TestFactory public Stream<DynamicTest> testSuccess() throws ParseError {
        return testSuccessByAstString("a", "A");
    }

    @TestFactory public Stream<DynamicTest> testCycle() throws ParseError {
        return testRecovery("b", "B");
    }

}
