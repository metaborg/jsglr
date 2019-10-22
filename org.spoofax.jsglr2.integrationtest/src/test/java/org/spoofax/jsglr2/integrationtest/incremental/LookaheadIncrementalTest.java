package org.spoofax.jsglr2.integrationtest.incremental;

import org.junit.Test;
import org.spoofax.jsglr2.integrationtest.BaseTestWithSdf3ParseTables;
import org.spoofax.terms.ParseError;

public class LookaheadIncrementalTest extends BaseTestWithSdf3ParseTables {

    public LookaheadIncrementalTest() {
        super("lookahead-incremental.sdf3");
    }

    @Test public void oneCharFollowRestricted() throws ParseError {
        testSuccessByExpansions("1[x]", "OneCharFollowRestricted(\"1[x]\")");
        testSuccessByExpansions("1[ax]", "OneCharPrefix(\"1[ax]\")");
        testSuccessByExpansions("1[abx]", "OneCharPrefix(\"1[abx]\")");
        testSuccessByExpansions("1[abcx]", "OneCharPrefix(\"1[abcx]\")");
    }

    @Test public void twoCharFollowRestricted() throws ParseError {
        testSuccessByExpansions("2[x]", "TwoCharFollowRestricted(\"2[x]\")");
        testSuccessByExpansions("2[ax]", "TwoCharFollowRestricted(\"2[ax]\")");
        testSuccessByExpansions("2[abx]", "TwoCharPrefix(\"2[abx]\")");
        testSuccessByExpansions("2[abcx]", "TwoCharPrefix(\"2[abcx]\")");
    }

    @Test public void threeCharFollowRestricted() throws ParseError {
        testSuccessByExpansions("3[x]", "ThreeCharFollowRestricted(\"3[x]\")");
        testSuccessByExpansions("3[ax]", "ThreeCharFollowRestricted(\"3[ax]\")");
        testSuccessByExpansions("3[abx]", "ThreeCharFollowRestricted(\"3[abx]\")");
        testSuccessByExpansions("3[abcx]", "ThreeCharPrefix(\"3[abcx]\")");
    }

    @Test public void incrementalOneCharFollowRestricted() throws ParseError {
        //@formatter:off
        testIncrementalSuccessByExpansions(
            new String[] { "1[x]",                           "1[ax]",                       "1[x]" },
            new String[] { "OneCharFollowRestricted(\"1[x]\")", "OneCharPrefix(\"1[ax]\")", "OneCharFollowRestricted(\"1[x]\")" }
        );
        //@formatter:on
    }

    @Test public void incrementalTwoCharFollowRestricted() throws ParseError {
        //@formatter:off
        testIncrementalSuccessByExpansions(
            new String[] { "2[ax]",                              "2[abx]",                     "2[ax]" },
            new String[] { "TwoCharFollowRestricted(\"2[ax]\")", "TwoCharPrefix(\"2[abx]\")", "TwoCharFollowRestricted(\"2[ax]\")" }
        );
        //@formatter:on
    }

    @Test public void incrementalThreeCharFollowRestricted() throws ParseError {
        //@formatter:off
        testIncrementalSuccessByExpansions(
            new String[] { "3[abx]",                                "3[abcx]",                      "3[abx]" },
            new String[] { "ThreeCharFollowRestricted(\"3[abx]\")", "ThreeCharPrefix(\"3[abcx]\")", "ThreeCharFollowRestricted(\"3[abx]\")" }
        );
        //@formatter:on
    }

}
