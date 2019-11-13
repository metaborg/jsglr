package org.spoofax.jsglr2.integrationtest.incremental;

import java.util.stream.Stream;

import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.spoofax.jsglr2.integrationtest.BaseTestWithSdf3ParseTables;
import org.spoofax.terms.ParseError;

public class IncrementalLayoutConflictTest extends BaseTestWithSdf3ParseTables {

    public IncrementalLayoutConflictTest() {
        super("incremental-layout-conflict.sdf3");
    }

    /**
     * The parse node corresponding to the first layout (three spaces) is shifted onto a stack with state 11 because it
     * is the intermediate layout between two IDs. The parse node corresponding to the second layout (one space) is
     * shifted onto a stack with state 13, because it is the layout before the EOF. This first layout is reused, but
     * then the parser expects to parse a second ID, which is not there. Instead, the first layout should be marked as
     * "parsed in multiple states" and be broken down during the second parse.
     */
    @TestFactory public Stream<DynamicTest> test() throws ParseError {
        //@formatter:off
        return testIncrementalSuccessByExpansions(
            new String[] {
                "a   b ",
                "a   "
            },
            new String[] {
                "Binary(\"a\",\"b\")",
                "Unary(\"a\")"
            }
        );
        //@formatter:on
    }

}
