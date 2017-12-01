package org.spoofax.jsglr2.tests.parser;

import java.io.IOException;
import java.net.URISyntaxException;

import org.junit.Test;
import org.spoofax.jsglr.client.InvalidParseTableException;
import org.spoofax.jsglr2.parsetable.ParseTableReadException;
import org.spoofax.jsglr2.tests.util.BaseTest;
import org.spoofax.jsglr2.util.WithGrammar;
import org.spoofax.terms.ParseError;

public class LookaheadTest extends BaseTest implements WithGrammar {

    public LookaheadTest() throws ParseError, ParseTableReadException, IOException, InvalidParseTableException,
        InterruptedException, URISyntaxException {
        setupParseTableFromDefFile("lookahead");
    }

    @Test
    public void oneCharFollowRestricted() throws ParseError, ParseTableReadException, IOException {
        testSuccessByExpansions("1[x]", "OneCharFollowRestricted(\"1[x]\")");
        testParseFailure("1[ax]");
        testParseFailure("1[abx]");
        testParseFailure("1[abcx]");
    }

    @Test
    public void twoCharFollowRestricted() throws ParseError, ParseTableReadException, IOException {
        testSuccessByExpansions("2[x]", "TwoCharFollowRestricted(\"2[x]\")");
        testSuccessByExpansions("2[ax]", "TwoCharFollowRestricted(\"2[ax]\")");
        testParseFailure("2[abx]");
        testParseFailure("2[abcx]");
    }

    @Test
    public void threeCharFollowRestricted() throws ParseError, ParseTableReadException, IOException {
        testSuccessByExpansions("3[x]", "ThreeCharFollowRestricted(\"3[x]\")");
        testSuccessByExpansions("3[ax]", "ThreeCharFollowRestricted(\"3[ax]\")");
        testSuccessByExpansions("3[abx]", "ThreeCharFollowRestricted(\"3[abx]\")");
        testParseFailure("3[abcx]");
    }

}