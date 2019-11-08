package org.spoofax.jsglr2.integrationtest.incremental;

import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.spoofax.jsglr2.integrationtest.BaseTestWithSdf3ParseTables;

import java.util.stream.Stream;

public class IncrementalExpressionPrioritiesTest extends BaseTestWithSdf3ParseTables {

    public IncrementalExpressionPrioritiesTest() {
        super("exp-priorities.sdf3");
    }

    @TestFactory public Stream<DynamicTest> changingPriorities() {
        //@formatter:off
        return testIncrementalSuccessByExpansions(
            new String[] {
                "x+x+x",
                "x*x+x",
                "x*x*x",
                "x+x*x",
                "x+x+x"
            },
            new String[] {
                "Add(Add(Term(),Term()),Term())",
                "Add(Mult(Term(),Term()),Term())",
                "Mult(Mult(Term(),Term()),Term())",
                "Add(Term(),Mult(Term(),Term()))",
                "Add(Add(Term(),Term()),Term())"
            }
        );
        //@formatter:on
    }

    @TestFactory public Stream<DynamicTest> largerPrioritiesTest() {
        //@formatter:off
        return testIncrementalSuccessByExpansions(
            new String[] {
                "x*x+x*x+x*x",
                "x*x+x*x+x+x",
                "x*x*x*x+x+x"
            },
            new String[] {
                "Add(Add(Mult(Term,Term),Mult(Term,Term)),Mult(Term,Term))",
                "Add(Add(Add(Mult(Term,Term),Mult(Term,Term)),Term),Term)",
                "Add(Add(Mult(Mult(Mult(Term,Term),Term),Term),Term),Term)"
            }
        );
        //@formatter:on
    }

    @TestFactory public Stream<DynamicTest> nothingChangedTest() {
        //@formatter:off
        return testIncrementalSuccessByExpansions(
            new String[] {
                "x*x+x*x+x*x",
                "x*x+x*x+x*x"
            },
            new String[] {
                "Add(Add(Mult(Term,Term),Mult(Term,Term)),Mult(Term,Term))",
                "Add(Add(Mult(Term,Term),Mult(Term,Term)),Mult(Term,Term))"
            }
        );
        //@formatter:on
    }

}
