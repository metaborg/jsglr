package org.spoofax.jsglr2.integrationtest.incremental;

import java.util.stream.Stream;

import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.spoofax.jsglr2.integrationtest.BaseTestWithSdf3ParseTables;
import org.spoofax.terms.ParseError;

public class IncrementalRejectSplitTest extends BaseTestWithSdf3ParseTables {

    public IncrementalRejectSplitTest() {
        super("reject-split.sdf3");
    }

    // This test only fails when running `./b build all`, not when running the tests separately.
    @TestFactory public Stream<DynamicTest> incrementalReject() throws ParseError {
        //@formatter:off
        return testIncrementalSuccessByExpansions(
            new String[] { "foo",         "for",                 "foo" },
            new String[] { "List([Foo])", "List([Id(\"for\")])", "List([Foo])" }
        );
        //@formatter:on
    }

    @TestFactory public Stream<DynamicTest> incrementalRejectReturn() throws ParseError {
        //@formatter:off
        return testIncrementalSuccessByExpansions(
                new String[] { "return",         "peturp",                 "return" },
                new String[] { "List([Return])", "List([Id(\"peturp\")])", "List([Return])" }
        );
        //@formatter:on
    }

    // TODO maybe disable??
    @TestFactory public Stream<DynamicTest> incrementalRejectSplit() throws ParseError {
        //@formatter:off
        return testIncrementalSuccessByExpansions(
            new String[] { "getbarkers",                 "return markers",                 "return" },
            new String[] { "List([Id(\"getbarkers\")])", "List([Return,Id(\"markers\")])", "List([Return])" }
        );
        //@formatter:on
    }

    public static void main(String[] args) throws Exception {
        BaseTestWithSdf3ParseTables test = new IncrementalRejectSplitTest();
        IncrementalSGLRThesisExampleTest.logIncrementalParse(test, "foo", "for");
    }

}
