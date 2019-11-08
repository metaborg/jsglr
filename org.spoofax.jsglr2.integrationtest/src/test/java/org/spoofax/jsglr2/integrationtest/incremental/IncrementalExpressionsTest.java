package org.spoofax.jsglr2.integrationtest.incremental;

import org.junit.jupiter.api.Test;
import org.spoofax.jsglr2.integrationtest.BaseTestWithSdf3ParseTables;
import org.spoofax.terms.ParseError;

public class IncrementalExpressionsTest extends BaseTestWithSdf3ParseTables {

    public IncrementalExpressionsTest() {
        super("expressions.sdf3");
    }

    @Test public void changingIdentifier() throws ParseError {
        //@formatter:off
        testIncrementalSuccessByExpansions(
            new String[] { "abc + def",                        "agc + def" },
            new String[] { "[Add(Var(\"abc\"),Var(\"def\"))]", "[Add(Var(\"agc\"),Var(\"def\"))]" }
        );
        //@formatter:on
    }

    @Test public void reusingSubtreesNoLayout() {
        //@formatter:off
        testIncrementalSuccessByExpansions(
            new String[] { "xx+x x+x",                                                 "xy+x x+x" },
            new String[] { "[Add(Var(\"xx\"),Var(\"x\")),Add(Var(\"x\"),Var(\"x\"))]", "[Add(Var(\"xy\"),Var(\"x\")),Add(Var(\"x\"),Var(\"x\"))]" }
        );
        //@formatter:on
    }

    @Test public void reusingSubtreesWithLayout() {
        //@formatter:off
        testIncrementalSuccessByExpansions(
            new String[] { "xx + x x + x",                                             "xy + x x + x" },
            new String[] { "[Add(Var(\"xx\"),Var(\"x\")),Add(Var(\"x\"),Var(\"x\"))]", "[Add(Var(\"xy\"),Var(\"x\")),Add(Var(\"x\"),Var(\"x\"))]" }
        );
        //@formatter:on
    }

    @Test public void incrementalAmbiguity() {
        //@formatter:off
        testIncrementalSuccessByExpansions(
            new String[] { "x+x",                          "x+x+x",                                                                                          "x+x" },
            new String[] { "[Add(Var(\"x\"),Var(\"x\"))]", "[amb([Add(Var(\"x\"),Add(Var(\"x\"),Var(\"x\"))),Add(Add(Var(\"x\"),Var(\"x\")),Var(\"x\"))])]", "[Add(Var(\"x\"),Var(\"x\"))]" }
        );
        //@formatter:on
    }

}
