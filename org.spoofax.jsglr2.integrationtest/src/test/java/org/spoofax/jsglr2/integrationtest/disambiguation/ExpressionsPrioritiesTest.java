package org.spoofax.jsglr2.integrationtest.disambiguation;

import java.util.stream.Stream;

import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.spoofax.jsglr2.integrationtest.BaseTestWithSdf3ParseTables;
import org.spoofax.terms.ParseError;

public class ExpressionsPrioritiesTest extends BaseTestWithSdf3ParseTables {

    public ExpressionsPrioritiesTest() {
        super("exp-priorities.sdf3");
    }

    @TestFactory public Stream<DynamicTest> oneTerm() throws ParseError {
        return testSuccessByExpansions("x", "\"x\"");
    }

    @TestFactory public Stream<DynamicTest> oneAdd() throws ParseError {
        return testSuccessByExpansions("x+x", "Add(\"x\",\"x\")");
    }

    @TestFactory public Stream<DynamicTest> oneMul() throws ParseError {
        return testSuccessByExpansions("x*x", "Mul(\"x\",\"x\")");
    }

    @TestFactory public Stream<DynamicTest> twoAdd() throws ParseError {
        return testSuccessByExpansions("x+x+x", "Add(Add(\"x\",\"x\"),\"x\")");
    }

    @TestFactory public Stream<DynamicTest> mulAdd() throws ParseError {
        return testSuccessByExpansions("x*x+x", "Add(Mul(\"x\",\"x\"),\"x\")");
    }

    @TestFactory public Stream<DynamicTest> addMul() throws ParseError {
        return testSuccessByExpansions("x+x*x", "Add(\"x\",Mul(\"x\",\"x\"))");
    }

    @TestFactory public Stream<DynamicTest> twoMul() throws ParseError {
        return testSuccessByExpansions("x*x*x", "Mul(Mul(\"x\",\"x\"),\"x\")");
    }

}
