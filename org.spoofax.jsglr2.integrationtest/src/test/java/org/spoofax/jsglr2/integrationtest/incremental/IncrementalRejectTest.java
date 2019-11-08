package org.spoofax.jsglr2.integrationtest.incremental;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.spoofax.jsglr2.integrationtest.BaseTestWithSdf3ParseTables;
import org.spoofax.terms.ParseError;

public class IncrementalRejectTest extends BaseTestWithSdf3ParseTables {

    public IncrementalRejectTest() {
        super("reject.sdf3");
    }

    // This test only fails when running `./b build all`, not when running the tests separately.
    @Disabled @Test public void incrementalReject() throws ParseError {
        //@formatter:off
        testIncrementalSuccessByExpansions(
            new String[] { "foo", "for",         "foo" },
            new String[] { "Foo", "Id(\"for\")", "Foo" }
        );
        //@formatter:on
    }

}
