package org.spoofax.jsglr2.integrationtest.grammars;

import org.junit.Test;
import org.spoofax.jsglr2.integrationtest.BaseTestWithSdf3ParseTables;
import org.spoofax.terms.ParseError;

public class ExpressionsTest extends BaseTestWithSdf3ParseTables {

    public ExpressionsTest() {
        super("expressions.sdf3");
    }

    @Test public void changingIdentifier() throws ParseError {
        testIncrementalSuccessByExpansions(new String[] { "abc + def", "agc + def" },
            new String[] { "[Add(Var(\"abc\"),Var(\"def\"))]", "[Add(Var(\"agc\"),Var(\"def\"))]" });
    }

    @Test public void reusingSubtreesNoLayout() {
        testIncrementalSuccessByExpansions(new String[] { "xx+x x+x", "xy+x x+x" },
            new String[] { "[Add(Var(\"xx\"),Var(\"x\")),Add(Var(\"x\"),Var(\"x\"))]",
                "[Add(Var(\"xy\"),Var(\"x\")),Add(Var(\"x\"),Var(\"x\"))]" });
    }

    @Test public void reusingSubtreesWithLayout() {
        testIncrementalSuccessByExpansions(new String[] { "xx + x x + x", "xy + x x + x" },
            new String[] { "[Add(Var(\"xx\"),Var(\"x\")),Add(Var(\"x\"),Var(\"x\"))]",
                "[Add(Var(\"xy\"),Var(\"x\")),Add(Var(\"x\"),Var(\"x\"))]" });
    }

    @Test public void incrementalAmbiguity() {
        testIncrementalSuccessByExpansions(new String[] { "x+x", "x+x+x", "x+x" },
            new String[] { "[Add(Var(\"x\"),Var(\"x\"))]",
                "[amb([Add(Var(\"x\"),Add(Var(\"x\"),Var(\"x\"))),Add(Add(Var(\"x\"),Var(\"x\")),Var(\"x\"))])]",
                "[Add(Var(\"x\"),Var(\"x\"))]" });
    }

}
