package org.spoofax.jsglr2.integrationtest.regression;

import java.util.stream.Stream;

import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.spoofax.jsglr2.integrationtest.BaseTestWithSdf3ParseTables;

public class Regression20210104 extends BaseTestWithSdf3ParseTables {

    /*
     * This regression test exercises the edge case in which a rejected stack link points to a stack into which another
     * non-rejected stack link gets merged. Therefore, the merged stack is considered for further processing (only
     * stacks that have _all_ stacks rejected are ignored). Before the fix (not considering derivations with rejected
     * nodes), the parse result could contain rejected derivations.
     */

    public Regression20210104() {
        super("regression-20210104.sdf3");
    }

    @TestFactory public Stream<DynamicTest> xxy() {
        // Without filtering out derivations that contain rejected derivations, the result would be ambiguous:
        // `amb([S("x","xy"),S(S("x","x"),"y")])`, i.e., with a derivation for `A = [y] {reject}`.
        return testSuccessByAstString("xxy", "S(\"x\",\"xy\")");
    }

}
