package org.spoofax.jsglr2.integrationtest.incremental;

import java.util.stream.Stream;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.spoofax.jsglr2.integrationtest.BaseTestWithSdf3ParseTables;
import org.spoofax.terms.ParseError;

public class IncrementalRejectTest extends BaseTestWithSdf3ParseTables {

    public IncrementalRejectTest() {
        super("reject.sdf3");
    }

    @TestFactory public Stream<DynamicTest> incrementalReject() throws ParseError {
        //@formatter:off
        return testIncrementalSuccessByExpansions(
            new String[] { "foo",         "for",                 "foo" },
            new String[] { "List([Foo])", "List([Id(\"for\")])", "List([Foo])" }
        );
        //@formatter:on
    }

    @TestFactory public Stream<DynamicTest> incrementalRejectSplit() throws ParseError {
        //@formatter:off
        return testIncrementalSuccessByExpansions(
            new String[] { "getbarkers",                 "return markers",                 "return" },
            new String[] { "List([Id(\"getbarkers\")])", "List([Return,Id(\"markers\")])", "List([Return])" }
        );
        //@formatter:on
    }

}
