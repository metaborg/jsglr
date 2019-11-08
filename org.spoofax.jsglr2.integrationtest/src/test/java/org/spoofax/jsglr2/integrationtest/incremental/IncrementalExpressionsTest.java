package org.spoofax.jsglr2.integrationtest.incremental;

import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.spoofax.jsglr2.integrationtest.BaseTestWithSdf3ParseTables;
import org.spoofax.terms.ParseError;

import java.util.stream.Stream;

public class IncrementalExpressionsTest extends BaseTestWithSdf3ParseTables {

    public IncrementalExpressionsTest() {
        super("expressions.sdf3");
    }

    @TestFactory public Stream<DynamicTest> changingIdentifier() throws ParseError {
        //@formatter:off
        return testIncrementalSuccessByExpansions(
            new String[] { "abc + def",                        "agc + def" },
            new String[] { "[Add(Var(\"abc\"),Var(\"def\"))]", "[Add(Var(\"agc\"),Var(\"def\"))]" }
        );
        //@formatter:on
    }

    @TestFactory public Stream<DynamicTest> reusingSubtreesNoLayout() {
        //@formatter:off
        return testIncrementalSuccessByExpansions(
            new String[] { "xx+x x+x",                                                 "xy+x x+x" },
            new String[] { "[Add(Var(\"xx\"),Var(\"x\")),Add(Var(\"x\"),Var(\"x\"))]", "[Add(Var(\"xy\"),Var(\"x\")),Add(Var(\"x\"),Var(\"x\"))]" }
        );
        //@formatter:on
    }

    @TestFactory public Stream<DynamicTest> reusingSubtreesWithLayout() {
        //@formatter:off
        return testIncrementalSuccessByExpansions(
            new String[] { "xx + x x + x",                                             "xy + x x + x" },
            new String[] { "[Add(Var(\"xx\"),Var(\"x\")),Add(Var(\"x\"),Var(\"x\"))]", "[Add(Var(\"xy\"),Var(\"x\")),Add(Var(\"x\"),Var(\"x\"))]" }
        );
        //@formatter:on
    }

    @TestFactory public Stream<DynamicTest> incrementalAmbiguity() {
        //@formatter:off
        return testIncrementalSuccessByExpansions(
            new String[] { "x+x",                          "x+x+x",                                                                                          "x+x" },
            new String[] { "[Add(Var(\"x\"),Var(\"x\"))]", "[amb([Add(Var(\"x\"),Add(Var(\"x\"),Var(\"x\"))),Add(Add(Var(\"x\"),Var(\"x\")),Var(\"x\"))])]", "[Add(Var(\"x\"),Var(\"x\"))]" }
        );
        //@formatter:on
    }

}
