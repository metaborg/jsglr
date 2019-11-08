package org.spoofax.jsglr2.integrationtest.incremental;

import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.spoofax.jsglr2.integrationtest.BaseTestWithSdf3ParseTables;
import org.spoofax.terms.ParseError;

import java.util.stream.Stream;

public class LookaheadIncrementalTest extends BaseTestWithSdf3ParseTables {

    public LookaheadIncrementalTest() {
        super("lookahead-incremental.sdf3");
    }

    @TestFactory public Stream<DynamicTest> oneCharFollowRestricted1() throws ParseError {
        return testSuccessByExpansions("1[x]", "OneCharFollowRestricted(\"1[x]\")");
    }

    @TestFactory public Stream<DynamicTest> oneCharFollowRestricted2() throws ParseError {
        return testSuccessByExpansions("1[ax]", "OneCharPrefix(\"1[ax]\")");
    }

    @TestFactory public Stream<DynamicTest> oneCharFollowRestricted3() throws ParseError {
        return testSuccessByExpansions("1[abx]", "OneCharPrefix(\"1[abx]\")");
    }

    @TestFactory public Stream<DynamicTest> oneCharFollowRestricted4() throws ParseError {
        return testSuccessByExpansions("1[abcx]", "OneCharPrefix(\"1[abcx]\")");
    }

    @TestFactory public Stream<DynamicTest> twoCharFollowRestricted1() throws ParseError {
        return testSuccessByExpansions("2[x]", "TwoCharFollowRestricted(\"2[x]\")");
    }

    @TestFactory public Stream<DynamicTest> twoCharFollowRestricted2() throws ParseError {
        return testSuccessByExpansions("2[ax]", "TwoCharFollowRestricted(\"2[ax]\")");
    }

    @TestFactory public Stream<DynamicTest> twoCharFollowRestricted3() throws ParseError {
        return testSuccessByExpansions("2[abx]", "TwoCharPrefix(\"2[abx]\")");
    }

    @TestFactory public Stream<DynamicTest> twoCharFollowRestricted4() throws ParseError {
        return testSuccessByExpansions("2[abcx]", "TwoCharPrefix(\"2[abcx]\")");
    }

    @TestFactory public Stream<DynamicTest> threeCharFollowRestricted1() throws ParseError {
        return testSuccessByExpansions("3[x]", "ThreeCharFollowRestricted(\"3[x]\")");
    }

    @TestFactory public Stream<DynamicTest> threeCharFollowRestricted2() throws ParseError {
        return testSuccessByExpansions("3[ax]", "ThreeCharFollowRestricted(\"3[ax]\")");
    }

    @TestFactory public Stream<DynamicTest> threeCharFollowRestricted3() throws ParseError {
        return testSuccessByExpansions("3[abx]", "ThreeCharFollowRestricted(\"3[abx]\")");
    }

    @TestFactory public Stream<DynamicTest> threeCharFollowRestricted4() throws ParseError {
        return testSuccessByExpansions("3[abcx]", "ThreeCharPrefix(\"3[abcx]\")");
    }

    @TestFactory public Stream<DynamicTest> incrementalOneCharFollowRestricted() throws ParseError {
        //@formatter:off
        return testIncrementalSuccessByExpansions(
            new String[] { "1[x]",                           "1[ax]",                       "1[x]" },
            new String[] { "OneCharFollowRestricted(\"1[x]\")", "OneCharPrefix(\"1[ax]\")", "OneCharFollowRestricted(\"1[x]\")" }
        );
        //@formatter:on
    }

    @TestFactory public Stream<DynamicTest> incrementalTwoCharFollowRestricted() throws ParseError {
        //@formatter:off
        return testIncrementalSuccessByExpansions(
            new String[] { "2[ax]",                              "2[abx]",                     "2[ax]" },
            new String[] { "TwoCharFollowRestricted(\"2[ax]\")", "TwoCharPrefix(\"2[abx]\")", "TwoCharFollowRestricted(\"2[ax]\")" }
        );
        //@formatter:on
    }

    @TestFactory public Stream<DynamicTest> incrementalThreeCharFollowRestricted() throws ParseError {
        //@formatter:off
        return testIncrementalSuccessByExpansions(
            new String[] { "3[abx]",                                "3[abcx]",                      "3[abx]" },
            new String[] { "ThreeCharFollowRestricted(\"3[abx]\")", "ThreeCharPrefix(\"3[abcx]\")", "ThreeCharFollowRestricted(\"3[abx]\")" }
        );
        //@formatter:on
    }

}
