package org.spoofax.jsglr2.integrationtest.incremental;

import static org.spoofax.jsglr2.integrationtest.incremental.IncrementalSGLRThesisExampleTest.logIncrementalParse;

import java.util.stream.Stream;

import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.spoofax.jsglr2.integrationtest.BaseTestWithSdf3ParseTables;
import org.spoofax.jsglr2.integrationtest.ParseNodeDescriptor;

public class IncrementalExpressionPrioritiesTest extends BaseTestWithSdf3ParseTables {

    public IncrementalExpressionPrioritiesTest() {
        super("exp-priorities.sdf3");
    }

    @TestFactory public Stream<DynamicTest> changingPriorities() {
        return testIncrementalSuccessByExpansions(new String[] { //
            "x+y+z", "x*y+z", "x*y*z", "x+y*z", //
            "x+y+z", "x+y*z", "x*y*z", "x*y+z", "x+y+z", //
        }, new String[] { //
            "Add(Add(\"x\",\"y\"),\"z\")", "Add(Mul(\"x\",\"y\"),\"z\")", //
            "Mul(Mul(\"x\",\"y\"),\"z\")", "Add(\"x\",Mul(\"y\",\"z\"))", //
            "Add(Add(\"x\",\"y\"),\"z\")", "Add(\"x\",Mul(\"y\",\"z\"))", //
            "Mul(Mul(\"x\",\"y\"),\"z\")", "Add(Mul(\"x\",\"y\"),\"z\")", //
            "Add(Add(\"x\",\"y\"),\"z\")", });
    }

    @TestFactory public Stream<DynamicTest> largerPrioritiesTest() {
        return testIncrementalSuccessByExpansions(new String[] { //
            "x*x+x*x+x*x", //
            "x*x+x*x+x+x", //
            "x*x*x*x+x+x", //
            "x*x*x*x+x*x", //
        }, new String[] { //
            "Add(Add(Mul(\"x\",\"x\"),Mul(\"x\",\"x\")),Mul(\"x\",\"x\"))",
            "Add(Add(Add(Mul(\"x\",\"x\"),Mul(\"x\",\"x\")),\"x\"),\"x\")",
            "Add(Add(Mul(Mul(Mul(\"x\",\"x\"),\"x\"),\"x\"),\"x\"),\"x\")",
            "Add(Mul(Mul(Mul(\"x\",\"x\"),\"x\"),\"x\"),Mul(\"x\",\"x\"))", });
    }

    @TestFactory public Stream<DynamicTest> nothingChangedTest() {
        return testIncrementalSuccessByExpansions(new String[] { //
            "x*x+x*x+x*x", "x*x+x*x+x*x", //
        }, new String[] { //
            "Add(Add(Mul(\"x\",\"x\"),Mul(\"x\",\"x\")),Mul(\"x\",\"x\"))",
            "Add(Add(Mul(\"x\",\"x\"),Mul(\"x\",\"x\")),Mul(\"x\",\"x\"))", });
    }

    @TestFactory public Stream<DynamicTest> testStateMatchingTest() {
        String[] inputStrings = { "x+y*z", "x*y*z" };
        return Stream.concat(
            testIncrementalSuccessByExpansions(inputStrings,
                new String[] { "Add(\"x\",Mul(\"y\",\"z\"))", "Mul(Mul(\"x\",\"y\"),\"z\")" }),
            testParseNodeReuse(inputStrings[0], inputStrings[1], //
                new ParseNodeDescriptor(4, 1, "Exp", "Var")));
    }

    @TestFactory public Stream<DynamicTest> testLookaheadTest() {
        String[] inputStrings = { "x+y+z", "x+y*z" };
        return Stream.concat(
            testIncrementalSuccessByExpansions(inputStrings,
                new String[] { "Add(Add(\"x\",\"y\"),\"z\")", "Add(\"x\",Mul(\"y\",\"z\"))" }),
            testParseNodeReuse(inputStrings[0], inputStrings[1], //
                new ParseNodeDescriptor(0, 1, "Exp", "Var"), //
                new ParseNodeDescriptor(1, 1, "\"+\"", null)));
    }

    public static void main(String[] args) {
        IncrementalExpressionPrioritiesTest test = new IncrementalExpressionPrioritiesTest();

        // §3.3.1: Shows the working of the "state matching test"
        logIncrementalParse(test, "x+y*z", "x*y*z");
        // §3.3.2: Shows the working of the "lookahead test" (i.e. marking the character preceding an edit as changed)
        logIncrementalParse(test, "x+y+z", "x+y*z");
    }

}
